#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <sys/sem.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <signal.h>
#include <math.h>
#include <float.h>
#include <time.h>

#define EXIT_SEQ(ex_val) free_sysV(); \
                         exit(ex_val)
#define ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Errore | pid: %d | file: %s | line: %d]\n-> %s\n-> [%d] %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_MAGENTA, getpid(), __FILE__, __LINE__, str, errno, strerror(errno), ANSI_COLOR_RESET)
#define ERROR_EXIT_SEQ(str) ERROR_MESSAGE(str); \
                                    EXIT_SEQ(EXIT_FAILURE)

#ifdef DEBUG
#define DEBUG_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define DEBUG_ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_CYAN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define SIGNAL_MESSAGE(str, signum) fprintf(stderr, "%s%s[Signal | pid: %d | file: %s | line: %d]\n-> %s [%d]%s\n", ANSI_COLOR_RESET, ANSI_COLOR_YELLOW, getpid(), __FILE__, __LINE__, str, signum, ANSI_COLOR_RESET)
#else
#define DEBUG_MESSAGE(str) /* foo */
#define DEBUG_ERROR_MESSAGE(str) /* foo */
#define SIGNAL_MESSAGE(str, signum) /* foo */
#endif

#include "headers/global.h"
#include "headers/shm.h"
#include "headers/sem.h"
#include "headers/msg_report.h"
#include "headers/msg_request.h"
#include "headers/conf_file.h"

/* Protipi delle funzioni */
void free_sysV();
void free_mem();
void signals_handler(int signum);
struct point find_source(struct point current_position, struct shm* shm_p);
void move_to(struct point *current_position, struct point destination, struct shm* shm_p, int sems_access_id, int sems_counter_id, int* n_crossed_cells, long* time, int so_timeout);
void check_and_set_position(struct point *start, struct point destination, short is_x);
struct point update_position(struct point current_position, struct point destination, struct shm* shm_p, int* flag);
void move(struct point next_cell, struct point *current_position, struct shm* shm_p, int sems_access_id, int sems_counter_id, int so_timeout);

int msg_reports_id = -1; /* Identificativo della coda di messaggi per la gestione dei report */
int sem_start_id = -1; /* identificativo del semaforo start */
int last_signal;
short int state;

int main(int argc, char* argv[])
{
    int shm_id; /* Identificativo della shared memory */
    struct shm* shm_p; /* Puntatore alla shared memory */
    int msg_requests_id; /* Identificativo della coda di messaggi per la gestione delle richieste */
    struct sigaction sa; /* Struttura per la gestione dei segnali */
    int sem_start_value; /* Memorizza il valore del semaforo start */
    struct point current_position; /* Posizione attuale del taxi */
    struct point destination; /* Destinazione verso la quale il taxi si sta spostando */
    struct msg_request msg_request; /* Struttura usata per la memorizzazione del messaggio ricevuto dalla coda di request */
    struct msg_report msg_report; /* Struttura utilizzata per l'invio dei messaggi di report */
    int n_crossed_cells; /* Numero di celle attraversate in un viaggio */
    long time; /* Tempo necessario al completamento di un viaggio */
    int sems_access_id; /* Identificativo dell'array di semafori per l'accesso alle celle */
    int sems_counter_id; /* Identificativo dell'array di semafori per l'accesso al contatore degli accessi alle celle */
    struct conf conf; /* Configurazione della simulazione */

    /* == INIZIALIZZAZIONE == */
    if(argc < 3) {
        ERROR_EXIT_SEQ("Taxi STARTING_ROW STARTING_COL");
    }
    /* Coordinate di partenza del taxi */
    current_position.row = atoi(argv[1]);
    current_position.col = atoi(argv[2]);

    srand(getpid());

    if(load_conf(&conf) < 0) {
        ERROR_EXIT_SEQ("errore nella lettura del file di configurazione");
    }

#ifdef DEBUG
    fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> coordinate di partenza: [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position.row, current_position.col, ANSI_COLOR_RESET);
#endif

    state = STATE_INIT;

    /* Intercettazione del segnale SIGINT e SIGALRM */
    memset(&sa, 0, sizeof(sa));
    sa.sa_handler = signals_handler;
    if(sigaction(SIGINT, &sa, NULL) < 0 ||
       sigaction(SIGALRM, &sa, NULL) < 0) {
        ERROR_EXIT_SEQ("errore nella creazione dell'handler dei segnali");
    }

    /* Acquisizione dell'id del semaforo start */
    sem_start_id = semget(SEM_START_KEY, 1, 0);
    if(sem_start_id < 0) {
        ERROR_EXIT_SEQ("impossibile ottenere il semaforo start");
    }
    /* Acquisizione dell'id dell'array dei semafori per l'accesso alle celle */
    sems_access_id = semget(SEMS_ACCESS_KEY, SO_HEIGHT * SO_WIDTH - conf.so_holes, 0);
    if(sems_access_id < 0){
        ERROR_EXIT_SEQ("Impossibile ottenere l'array di semafori per l'accesso alle celle");
    }
    /* Acquisizione dell'id dell'array dei semafori per l'accesso al contatore degli accessi delle celle */
    sems_counter_id = semget(SEMS_COUNTER_ID, SO_HEIGHT * SO_WIDTH - conf.so_holes, 0);
    if(sems_access_id < 0) {
        ERROR_EXIT_SEQ("impossibile ottenere l'array di semafori per i contatori degli attraversamenti delle celle");
    }
    /* Acquisizione dell'id della shared memory */
    shm_id = shmget(SHM_KEY, sizeof(struct shm), 0);
    if(shm_id < 0) {
        ERROR_EXIT_SEQ("impossibile ottenere la shared memory");
    }
    /* Attaccamento alla shared memory */
    shm_p = shmat(shm_id, NULL, 0);
    if(shm_p == (void*)-1) {
        ERROR_EXIT_SEQ("impossibile collegarsi alla shared memory");
    }
    /* Acquisizione dell'id della coda di messaggi di richieste */
    msg_requests_id = msgget(MSG_REQUESTS_KEY, 0);
    if(msg_requests_id < 0) {
        ERROR_EXIT_SEQ("impossibile ottenre la coda di messaggi per l'invio delle richieste");
    }
    /* Acquisizione dell'id della coda di messaggi dei report */
    msg_reports_id = msgget(MSG_REPORTS_KEY, 0);
    if(msg_reports_id < 0) {
        ERROR_EXIT_SEQ("impossibile ottenere la coda dei messaggi per l'invio dei report");
    }

    DEBUG_MESSAGE("pronto, attesa sul semaforo...");
    /* Unlock del semaforo start
     * l'unlock viene eseguito solamente se il semaforo non ha già valore 0.
     * Se un taxi viene ricreato dopo l'inizio della simulazione, potrebbe non
     * dover sbloccare il semaforo */
    sem_start_value = semctl(sem_start_id, 0, GETVAL);
    if(sem_start_value < 0) {
        ERROR_EXIT_SEQ("impossibile prelevare le informazioni del semaforo start");
    }
    if(sem_start_value != 0 && sem_lock(sem_start_id, 0) < 0) {
        ERROR_EXIT_SEQ("errore nell'unlock del semaforo start");
    }
    /* ====================== */

    /* Attesa dello sblocco del semaforo da parte degli altri processi */
    if(sem_wait_for_0(sem_start_id, 0) < 0) {
        ERROR_EXIT_SEQ("errore nell'attesa dello sblocco del semaforo start");
    }

    state = STATE_FREE;

    /* Posizionamento sulla cella iniziale */
    alarm(conf.so_timeout);
    while(sem_lock(sems_access_id, shm_p->map[current_position.row][current_position.col].sem_access_idx) < 0) {
        if(errno ==  EINTR) {
            /* La lock del semaforo è stata interrotta dalla ricezione di un segnale */
            if(last_signal == SIGALRM) {
                /* Il segnale ricevuto è di tipo SIGALRM: il processo deve morire e inviare un messaggio al padre con di
                 * tipo MSG_TYPE_ABORTED */
                msg_report_constructor(&msg_report,  MSG_REPORT_TYPE_ABORTED, 0, 0, getpid(), state);
                while(msgsnd(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0) < 0)
                    if(errno != EINTR) {
                        ERROR_EXIT_SEQ("impossibile inviare un messaggio di report");
                    }
#ifdef DEBUG
#ifdef ENHANCED_PRINT
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> 📤 inviato un messaggio di report\n-> 📧 {type = %ld | n_crossed_cells = %d | time = %ld | state = %d}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, msg_report.state, ANSI_COLOR_RESET);
#else
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> inviato un messaggio di report\n-> {type = %ld | n_crossed_cells = %d | time = %ld | state = %d}\n%s",
                            ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, msg_report.state, ANSI_COLOR_RESET);
#endif
#endif
                EXIT_SEQ(1);
            } else {
                /* Il segnale ricevuto non è di tipo SIGALRM: il processo deve riprovare a effettuare la lock */
                continue;
            }
        } else {
            ERROR_EXIT_SEQ("errore nella lock del semaforo dell'accesso alla cella successiva");
        }
    }
    /* Rimozione dell'alarm precedentemente impostato */
    alarm(0);

    /* Il processo taxi inizia a spostarsi sulla mappa sulla base della sua posizione e di una
     * eventuale richiesta da gestire */
    while(1) {
#ifdef DEBUG
        fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> posizione attuale: [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position.row, current_position.col, ANSI_COLOR_RESET);
#endif
        if(shm_p->map[current_position.row][current_position.col].type == CELL_TYPE_SOURCE &&
           msg_request_rcv(msg_requests_id, &msg_request, shm_p->map[current_position.row][current_position.col].sem_access_idx + 1) == 0) {
#ifdef DEBUG
#ifdef ENHANCED_PRINT
            fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> 📥 preso in carico una richiesta\n-> 📧 {type = %ld | destination = [%d][%d]}\n%s",
                    ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_request.type, msg_request.destination.row, msg_request.destination.col, ANSI_COLOR_RESET);
#else
            fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> preso in carico una richiesta\n-> {type = %ld | destination = [%d][%d]}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_request.type, msg_request.destination.row, msg_request.destination.col, ANSI_COLOR_RESET);
#endif
#endif
            /* Il taxi si trova su una sorgente ed è presente una richiesta da gestire */
            destination = msg_request.destination;

            state = STATE_SERVING_REQ;
        } else {
            destination = find_source(current_position, shm_p);
            DEBUG_MESSAGE("Mi sposto verso la prossima source");
        }
        /* Spostamento verso la destinazione designata */
        move_to(&current_position, destination, shm_p, sems_access_id, sems_counter_id, &n_crossed_cells, &time, conf.so_timeout);
        if(state == STATE_SERVING_REQ) {
            msg_report_constructor(&msg_report, MSG_REPORT_TYPE_SUCCESSFULL, n_crossed_cells, time, getpid(), state);
            while(msgsnd(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0) < 0)
                if(errno != EINTR) {
                    ERROR_EXIT_SEQ("impossibile inviare un messaggio di report");
                }
#ifdef DEBUG
#ifdef ENHANCED_PRINT
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> 📤 inviato un messaggio di report\n-> 📧 {type = %ld | n_crossed_cells = %d | time = %ld}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, ANSI_COLOR_RESET);
#else
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> inviato un messaggio di report\n-> {type = %ld | n_crossed_cells = %d | time = %ld}\n%s",
                            ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, ANSI_COLOR_RESET);
#endif
#endif
            state = STATE_FREE;
        }
    }

    return 0;
}

/* La funzione sposta un taxi dalla sua posizione attuale verso una destinazione sulla mappa */
void move_to(struct point *current_position, struct point destination, struct shm* shm_p, int sems_access_id, int sems_counter_id, int* n_crossed_cells, long* time, int so_timeout)
{
    struct point next_cell; /* Struttura usata per memorizzare la prossima cella in cui il taxi deve spostarsi*/
    int flag = 0;
    *n_crossed_cells = 0;
    *time = 0;

    /* Il ciclo si ripete spostandosi di cella in cella fino a che non viene raggiunta la destinazione */
    while(current_position->row != destination.row || current_position->col != destination.col) {
        /* Prelevamento della prossima cella dove spostarsi */
        next_cell = update_position(*current_position, destination, shm_p, &flag);
        if (next_cell.row != current_position->row || next_cell.col != current_position->col) {
            /* La posizione ritornata è diversa dalla posizione attuale */
            /* Spostamento sulla prossima cella */
            /* *current_position = next_cell; */
            move(next_cell, current_position, shm_p, sems_access_id, sems_counter_id, so_timeout);
            /* Una cella è stata attraversata */
            *n_crossed_cells += 1;
            *time += shm_p->map[current_position->row][current_position->col].crossing_time;
#ifdef DEBUG
            if(state == STATE_SERVING_REQ)
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> spostamento verso la destinazione in [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position->row, current_position->col, ANSI_COLOR_RESET);
            else
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> spostamento verso la source in [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position->row, current_position->col, ANSI_COLOR_RESET);
#endif
        }
    }
#ifdef DEBUG
    if(state == STATE_SERVING_REQ)
        fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> arrivo alla destinazione in [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position->row, current_position->col, ANSI_COLOR_RESET);
    else
        fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> arrivo alla source in [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, current_position->row, current_position->col, ANSI_COLOR_RESET);
#endif
}

/* Funzione che effettua lo spostamento del taxi in next_cell partendo da current_x e un current_y
 * Lancia un alarm, prima della lock, che uccide il taxi in caso di superamento del SO_TIMEOUT
 * Tenta una sem_lock sul semaforo della prossima cella e in caso di successo resetta l'alarm e libera il semaforo della cella corrente
 * Simula il tempo di attraversamento tramite una nanosleep
 */
void move(struct point next_cell, struct point *current_position, struct shm* shm_p, int sems_access_id, int sems_counter_id, int so_timeout)
{
    struct timespec crossing_sleep; /* Struttura usata dalla nanosleep */
    struct msg_report msg_report;
#ifdef DEBUG
    int sem_cell_value;

    while((sem_cell_value =  semctl(sems_access_id, shm_p->map[next_cell.row][next_cell.col].sem_access_idx, GETVAL)) < 0) {
        if(errno == EINTR) continue;
        ERROR_EXIT_SEQ("impossibile ottenere le informazioni dal semaforo");
    }
    fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> la cella [%d][%d] in cui sto entrando ha semaforo con valore [%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, next_cell.row, next_cell.col, sem_cell_value, ANSI_COLOR_RESET);
#endif
    /* Impostazione di un timer di distruzione: oltre quest'intervallo di tempo, se il processo non è riuscito
     * ad accedere alla cella muore e marca la richiesta come aborted */
    alarm(so_timeout);
    while(sem_lock(sems_access_id, shm_p->map[next_cell.row][next_cell.col].sem_access_idx) < 0) {
        if(errno ==  EINTR) {
            /* La lock del semaforo è stata interrotta dalla ricezione di un segnale */
            if(last_signal == SIGALRM) {
                /* Uscita dalla cella precedentemente occupata */
                while(sem_unlock(sems_access_id, shm_p->map[current_position->row][current_position->col].sem_access_idx) < 0) {
                        if (errno != EINTR) {
                            ERROR_EXIT_SEQ("Errore nella unlock del semaforo dell'accesso alla cella precedente del taxi");
                        }
                }
                /* Il segnale ricevuto è di tipo SIGALRM: il processo deve morire e inviare un messaggio al padre con di
                 * tipo MSG_TYPE_ABORTED */
                msg_report_constructor(&msg_report,  MSG_REPORT_TYPE_ABORTED, 0, 0, getpid(), state);
                while(msgsnd(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0) < 0)
                    if(errno != EINTR) {
                        ERROR_EXIT_SEQ("impossibile inviare un messaggio di report");
                    }
#ifdef DEBUG
#ifdef ENHANCED_PRINT
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> 📤 inviato un messaggio di report\n-> 📧 {type = %ld | n_crossed_cells = %d | time = %ld | state = %d}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, msg_report.state, ANSI_COLOR_RESET);
#else
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> inviato un messaggio di report\n-> {type = %ld | n_crossed_cells = %d | time = %ld | state = %d}\n%s",
                            ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_report.type, msg_report.n_crossed_cells, msg_report.time, msg_report.state, ANSI_COLOR_RESET);
#endif
#endif
                /* fprintf(stderr, "%d Inviato messaggio di morte!\n", getpid()); */
                EXIT_SEQ(1);
            } else {
                /* Il segnale ricevuto non è di tipo SIGALRM: il processo deve riprovare a effettuare la lock */
                continue;
            }
        } else {
            printf("{[%d][%d]}\n", current_position->row, current_position->col); fflush(stdout);
            ERROR_EXIT_SEQ("errore nella lock del semaforo dell'accesso alla cella successiva");
        }
    }
    /* Rimozione dell'alarm precedentemente impostato */
    alarm(0);
    /* Uscita dalla cella precedentemente occupata */
    while(sem_unlock(sems_access_id, shm_p->map[current_position->row][current_position->col].sem_access_idx) < 0) {
        if (errno != EINTR) {
            ERROR_EXIT_SEQ("errore nella unlock del semaforo dell'accesso alla cella precedente del taxi");
        }
    }
    /* Aggiornamento delle coordinate del taxi */
    *current_position = next_cell;
    /* Viene rilasciato il semaforo della cella in cui il taxi si trovava */
    /* Incremento del contatore dei passaggi nella cella */
    while(sem_unlock(sems_counter_id, shm_p->map[current_position->row][current_position->col].sem_access_idx) /* +1 */ < 0) {
        if(errno != EINTR) {
            ERROR_EXIT_SEQ("errore nella unlock (incremento) del semaforo contatore degli accessi alla cella");
        }
    }
    /* Nanosleep del tempo di attraverso della cella */
    crossing_sleep.tv_sec = 0;
    crossing_sleep.tv_nsec = shm_p->map[current_position->row][current_position->col].crossing_time;
    nanosleep(&crossing_sleep, (void *) NULL);
}

/* La funzione ritorna la successiva cella sulla quale spostarsi dalla la posizione attuale e la destinazione */
struct point update_position(struct point current_position, struct point destination, struct shm* shm_p, int* flag) {
    struct point start;
    start.row = current_position.row;
    start.col = current_position.col;
    /* controllo se non si è arrivati a destinazione */
    if(start.row != destination.row || start.col != destination.col){
	    /*Se la riga corrente equivale a quella di destinazione oppure il flag è maggiore di 0 lo spostamento avverà sulle colonne*/
        if(start.row == destination.row || *flag > 0){
	        /* se lo spostamento sulle colonne è dato dal flag lo decremento*/		
            if (*flag>0) *flag -= 1;
   	        /*controllo se la colonna di destinazione è minore di quella corrente e mi sposto verso la destinazione*/
            if(start.col < destination.col) {
                /*controllo che la colonna successiva non sia un hole*/
                if(shm_p->map[start.row][start.col + 1].type != CELL_TYPE_HOLE){
                    start.col += 1;
                }else{
                    /*se è un hole richiamo un metodo che effettua uno spostamento sulle righe in modo da evitare l'hole*/
                    check_and_set_position(&start,destination,0);
                    /*imposto il flag=2 in modo tale che nei prossimi due richiami della funzione avvenga uno spostamento sulle colonne perchè in caso contrario al successivo richiamo della funzione lo spostamento avverrebbe sulle righe*/
		            /*il flag viene impostato a 2 così da evitare che il taxi torni in una cella precendentemente attraversata a causa di un altro hole*/
                    *flag = 2;
                }
            }else{ 
		        /*start->col > destination.col*/
                /*lo spostamento sulle colonne è opposto se la destinazione è maggiore della cella corrente*/
                /*controllo che la colonna successiva non sia un hole*/
                if (shm_p->map[start.row][start.col - 1].type != CELL_TYPE_HOLE){
                    start.col -= 1;
                } else {
                    /* se è un hole richiamo un metodo che effettua uno spostamento sulle righe in modo da evitare l'hole*/
                    check_and_set_position(&start,destination,0);
                    /*imposto il flag=2 in modo tale che nei prossimi due richiami della funzione avvenga uno spostamento sulle colonne perchè in caso contrario al successivo richiamo della funzione lo spostamento avverrebbe sulle righe*/
		            /*il flag viene impostato a 2 così da evitare che il taxi torni in una cella precendentemente attraversata a causa di un altro hole*/
                    *flag = 2;
                }
            }
        }else{
	        /*quando la funzione viene chiamata lo spostamento prioritario è sempre sulle righe, ovvero finchè la riga corrente non equivale a quella di destinazione o il flag è >0 lo spostamento non avverrà sulle righe*/
            /* controllo in che direzione spostarmi sulle righe*/
            if(start.row < destination.row){
                /* controllo diagonali affini alla direzione in cui mi sto spostando solo quando la riga successiva equivale a quella di destinazione e la colonna attuale non è la colonna di destinazione al fine di evitare spostamenti inutili o ripetuti*/
                if(start.row + 1 == destination.row && start.col != destination.col &&
                   (shm_p->map[start.row + 1][start.col + 1].type == CELL_TYPE_HOLE || shm_p->map[start.row + 1][start.col - 1].type == CELL_TYPE_HOLE)){
                    /* se in questa casistica è presente un hole nelle diagonali interessate swappo lo spostamento alle colonne */
                    *flag = 1;
                /*controllo che la riga successiva non sia un hole*/
                }else if(shm_p->map[start.row + 1][start.col].type != CELL_TYPE_HOLE){
                    start.row += 1;

                }else{
                    /* se è un hole richiamo un metodo che effettua uno spostamento sulle colonne in modo da evitare l'hole*/
                    check_and_set_position(&start,destination,1);
                }
            }else{
                /* start->row > destination.row */
                /* lo spostamento sulle righe è opposto se la destinazione è maggiore della cella corrente*/
                /* controllo diagonali affini alla direzione in cui mi sto spostando solo quando la riga successiva equivale a quella di destinazione e la colonna attuale non è la colonna di destinazione al fine di evitare spostamenti inutili o ripetuti*/
                if(start.row - 1 == destination.row && start.col != destination.col &&
                   (shm_p->map[start.row - 1][start.col - 1].type == CELL_TYPE_HOLE || shm_p->map[start.row - 1][start.col + 1].type == CELL_TYPE_HOLE)){
                    /* se in questa casistica è presente un hole nelle diagonali interessate swappo lo spostamento alle colonne */
                    *flag = 1;
                /*controllo che la riga successiva non sia un hole*/
                }else if(shm_p->map[start.row - 1][start.col].type != CELL_TYPE_HOLE){
                    start.row -= 1;

                }else{
                    /* se è un hole richiamo un metodo che effettua uno spostamento sulle colonne in modo da evitare l'hole*/
                    check_and_set_position(&start,destination,1);
                }
            }
        }
    }
    return start;
}
/* La funzione controlla la validità della posizione su cui spostarsi */
void check_and_set_position(struct point *start,struct point destination,short is_x) {
    /* Variabile di controllo se il blocco è stato trovato sulle x */
    if(is_x) {
        /* Spostamento sulle colonne in base alla destinazione */
        if(start->col <= destination.col && start->col + 1 != SO_WIDTH) start->col++;
        else start->col--;
    /* Blocco sulle colonne */
    }else{
        /* Spostamento sulle righe in base alla destinazione */
        if(start->row <= destination.row && start->row + 1 != SO_HEIGHT) start->row++;
        else start->row--;
    }
}

/* La funzione calcola utilizzando il teorema di Pitagora la sorgente più vicina
 * (esclusa eventualmente quella su cui già ci si trova) */
struct point find_source(struct point current_position, struct shm* shm_p) {
    struct point source_destination = current_position;
    int i, j;
    double min = DBL_MAX;
    double distance;

    /* Scorrimento della mappa alla ricerca delle source */
    for(i = 0; i < SO_HEIGHT; i++){
        for(j = 0; j < SO_WIDTH; j++) {
            if(shm_p->map[i][j].type == CELL_TYPE_SOURCE) {
                if (current_position.row != i || current_position.col != j) {
                    distance = sqrt((double) ((pow(i - current_position.row, 2)) +
                                              (pow(j - current_position.col, 2))));

                    if (distance < min) {
                        min = distance;
                        source_destination.row = i;
                        source_destination.col = j;
                    }
                }
            }
        }
    }

    return source_destination;
}

/* Procedura per l'eliminazione di tutte le strutture sysV create.
 * Aggiungere una riga per ogni nuova struttura creata */
void free_sysV()
{
    int sem_start_value;
    /* Se il processo si trova in STATE_INIT, significa che la sua terminazione potrebbe andare a
     * bloccare l'inizio della simulazione. Deve essere eseguita la lock del semaforo di start.
     * Se l'errore è legato all'impossibilità di prelevare l'id del semaforo di start, nessuna procedura
     * può essere messa in atto per risolvere il problema in questo punto */
    if(state == STATE_INIT && sem_start_id >= 0) {
        /* Controllo se il semaforo è già a zero */
        sem_start_value = semctl(sem_start_id, 0, GETVAL);
        if (sem_start_value < 0) {
            ERROR_MESSAGE("Impossibile prelevare le informazioni dal semaforo di start");
        } else if (sem_start_value > 0) {
            if (sem_lock(sem_start_id, 0) < 0) {
                ERROR_MESSAGE(
                        "La procedura di cancellazione delle strutture sysV non è riuscita a eseguire la lock sul semaforo start");
            }
        }
    }
}

/* Handler dei segnali.
 * L'handler intercetta il segnale SIGINT */
void signals_handler(int signum)
{
    struct msg_report msg_report;
    last_signal = signum;

    SIGNAL_MESSAGE("ricevuto segnale", signum);
    if(signum == SIGINT) {
        alarm(0);
        /* Se il processo stava eseguendo una corsa, il viaggio deve essere segnalato come aborted e il processo terminare */
        msg_report_constructor(&msg_report, MSG_REPORT_TYPE_ABORTED, 0, 0, getpid(), state);
        if(state == STATE_SERVING_REQ &&
           msg_reports_id > 0 &&
           msgsnd(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0) < 0) {
            if(errno != EINTR) ERROR_MESSAGE("impossibile inviare un messaggio di report");
        }
        EXIT_SEQ(0);
    }
    /* Il segnale SIGALRM viene gestitio per comodità dentro al codice verificando il contenuto
     * di last_signal */
}
