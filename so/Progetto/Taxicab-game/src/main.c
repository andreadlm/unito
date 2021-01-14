#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <signal.h>

/* Procedura di uscita */
#define EXIT_SEQ(ex_val) kill_kids(); \
                         wait_kids(); \
                         free_sysV(); \
                         free_mem();  \
                         exit(ex_val)
#define ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Errore | pid: %d | file: %s | line: %d]\n-> %s\n-> [%d] %s\n%s\n", ANSI_COLOR_RESET, ANSI_COLOR_MAGENTA, getpid(), __FILE__, __LINE__, str, errno, strerror(errno), ANSI_COLOR_RESET)
#define ERROR_EXIT_SEQ(str) ERROR_MESSAGE(str); \
                            EXIT_SEQ(EXIT_FAILURE)

#ifdef DEBUG
#define DEBUG_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define DEBUG_ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_CYAN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define SIGNAL_MESSAGE(str, signum) fprintf(stderr, "%s%s[Signal | pid: %d | file: %s | line: %d]\n-> %s [%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_YELLOW, getpid(), __FILE__, __LINE__, str, signum, ANSI_COLOR_RESET)
#else
#define DEBUG_MESSAGE(str) /* foo */
#define DEBUG_ERROR_MESSAGE(str) /* foo */
#define SIGNAL_MESSAGE(str, signum) /* foo */
#endif

#include "headers/global.h"
#include "headers/shm.h"
#include "headers/sem.h"
#include "headers/msg_report.h"
#include "headers/processes_info_list.h"

void free_sysV();
void free_mem();
void kill_kids();
void wait_kids();
void pause_taxi_kids();
void resume_taxi_kids();
void signals_handler(int signum);
int create_taxi(pid_t old_pid);
int create_source();
#ifdef DEBUG
int count_active_taxi();
#endif

struct conf conf; /* Configurazione della simulazione */

/* sysV */
int shm_id = -1; /* Identificativo della shared memory */
struct shm* shm_p; /* Puntatore alla shared memory */
int sem_start_id = -1; /* Identificativo del semaforo di start dei processi figli */
int sems_access_id = -1; /* Identificativo dell'array di semafori di accesso alle celle */
int sems_counter_id = -1; /* Identificativo dell'array di semafori dei contatori degli accessi alle celle */
int msg_requests_id = -1; /* Identificativo della coda di messaggi deputata alle richieste */
int msg_reports_id = -1; /* Identificativo della coda di messaggi deputata ai report */
int end_simulation = 0; /* Viene impostata a 1 quando la simulazione deve essere teminata */

/* Variabili globali: utili per essere utilizzate nell'handler dei segnali */
short int map_model[SO_HEIGHT][SO_WIDTH]; /* Modello della mappa */
struct processes_info_list* kids_list; /* Lista dei processi figli creati */
pid_t main_pid; /* Pid del processo main. Evita che processi figli che non
                 * hanno ancora eseguito execve() possano eseguire le operazioni
                 * previste in caso di intercettazione dei segnali */

int main()
{
    /* == DICHIARAZIONE DELLE VARIABILI == */
    int i; /* Indice generico di scorrimento */
#ifdef DEBUG
    int j; /* Indice generico di scorrimento */
#endif
    int n_successfull_trips; /* Numero di viaggi terminati con successo */
    int n_aborted_trips; /* Numero di viaggi abortiti */
    int n_not_addressed_requests; /* Numero di viaggi non evasi */
    struct sigaction sa; /* Struttura per la gestione dei segnali */
    struct msg_report msg_report; /* Struttura dove vengono memorizzati i messaggi letti */
    struct msqid_ds msg_requests_info; /* Struttura dove vengono memorizzate le informazioni relative alla coda dei messaggi di richiesta */
    struct msqid_ds msg_reports_info; /* Struttura dove vengono memorizzate le informazioni relative alla coda dei messaggi di report */
    struct processes_info_list* process_info; /* Struttura dove vengono memorizzate le informazioni di un singolo processo figlio */
    pid_t pid_best_longest_ride, pid_best_n_crossed_cells, pid_best_n_requests; /* Record associati alle corse dei taxi */
    long best_longest_ride; /* Record corsa più lunga (tempo) */
    int best_n_crossed_cells, best_n_requests; /* Record corse dei taxi (numero di celle, numero di richieste) */
    /* =================================== */

    main_pid = getpid();
    srand(getpid());

    /* Lettura del file di configurazione */
    switch(load_conf(&conf)) {
        case -1:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione");
        case -2:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione: valori negativi");
        case -3:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione: superamento limiti imposti dalla dimensione della mappa");
        case -4:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione: i campi so_*_min superano per valore i campi so_*_max");
        case -5:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione: valori di so_sources o so_taxi uguali a 0");
        case -6:
            ERROR_EXIT_SEQ("errore nella lettura del file di configurazione: durata della simulazione uguale a 0");
        default: break;
    }

    /* == IMPOSTAZIONE HANDLER DEI SEGNALI == */
    /* Intercettazione dei segnali SIGTERM, SIGINT e SIGALRM */
    memset(&sa, 0, sizeof(sa));
    sa.sa_handler = signals_handler;
    if(sigaction(SIGINT, &sa, NULL) < 0 || /* SIGINT -> permette di ripulire la memoria in caso di interruzione anomala */
       sigaction(SIGALRM, &sa, NULL) < 0 || /* SIGALRM -> permette di gestire gli allarmi perdiodici e l'allarme finale */
       sigaction(SIGTERM, &sa, NULL) < 0) { /* SIGTERM -> permette di ripulire la memoria in caso di interruzione anomala */
        ERROR_EXIT_SEQ("errore nella creazione dell'handler dei segnali");
    }
    /* ====================================== */

    /* == CREAZIONE DEI SEMAFORI == */
    /* Creazione del semaforo di start dei processi figli */
    sem_start_id = semget(SEM_START_KEY, 1, IPC_CREAT|IPC_EXCL|0600);
    if(sem_start_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare il semaforo start");
    }
    /* Inizializzazione del semaforo di start dei processi figli */
    if(semctl(sem_start_id, 0, SETVAL, conf.so_taxi + conf.so_sources) < 0) {
        ERROR_EXIT_SEQ("impossibile inizializzare il semaforo start");
    }
    /* Creazione dei semafori che regolano l'accesso alle celle */
    sems_access_id = semget(SEMS_ACCESS_KEY, SO_HEIGHT * SO_WIDTH - conf.so_holes, IPC_CREAT|IPC_EXCL|0600);
    if(sems_access_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare l'array di semafori di accesso alle celle");
    }
    /* Creazione dei semafori che regolano l'accesso alla cella contatore degli attraversamenti di una cella */
    sems_counter_id = semget(SEMS_COUNTER_ID, SO_HEIGHT * SO_WIDTH - conf.so_holes, IPC_CREAT|IPC_EXCL|0600);
    if(sems_counter_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare l'array di semafori per i contatori degli attraversamenti di una cella");
    }
    /* L'inizializzazione di ciascun semaforo degli array sems_access e sems_counter
     * viene eseguita in fase di inizializzazione delle celle */
    /* ============================ */

    /* == CREAZIONE DELLA MAPPA IN SHARED MEMORY == */
    /* Generazione del modello della mappa */
    generate_map_model(map_model, conf.so_holes, conf.so_sources);
    /* Costruzione di una shared memory che contenga SO_HEIGHT * SO_WIDTH celle */
    shm_id = shmget(SHM_KEY, sizeof(struct shm), IPC_CREAT | IPC_EXCL | 0600);
    if(shm_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare la shared memory");
    }
    shm_p = shmat(shm_id, NULL, 0);
    if(shm_p == (void*)-1) {
        ERROR_EXIT_SEQ("impossibile collegarsi alla shared memory");
    }
    /* Inzializzazione delle celle sulla base del modello e stampa del risultato */
    if(shm_constructor(shm_p, map_model, sems_access_id, sems_counter_id, conf) == -1) {
        ERROR_EXIT_SEQ("impossibile inizializzare la mappa");
    }
#ifdef DEBUG
    /* Stampa delle informazioni associate a ciascuna cella */
    printf("Struttura delle celle della shared memory:\n%s%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN);
    for(i = 0; i < SO_HEIGHT; i++) for(j = 0; j < SO_WIDTH; j++) cell_print(&shm_p->map[i][j]);
    printf("%s", ANSI_COLOR_RESET);
#endif
    /* ============================================ */

    /* == CREAZIONE DELLE CODE DI MESSAGGI == */
    /* Creazione della coda dei messaggi per la gestione delle richieste */
    msg_requests_id = msgget(MSG_REQUESTS_KEY, IPC_CREAT|IPC_EXCL|0600);
    if(msg_requests_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare la coda di messaggi per l'invio di richieste");
    }
    /* Creazione della coda dei messaggi per la gestione dei report */
    msg_reports_id = msgget(MSG_REPORTS_KEY, IPC_CREAT|IPC_EXCL|0600);
    if(msg_reports_id < 0) {
        ERROR_EXIT_SEQ("impossibile creare la coda di messaggi per l'invio di report");
    }
    /* ====================================== */

    /* == CREAZIONE DEI PROCESSI FIGLI == */
    /* Vengono creati tutti i processi taxi e sorgente */
    for(i = 0; i < conf.so_taxi; i++) {
        if (i < conf.so_taxi) {
            /* Creazione di un taxi */
            if (create_taxi(0 /* Non sostituisce nessun figlio nella lista */) < 0)
                ERROR_MESSAGE("impossibile creare un processo taxi");
        }
    }
    /* Creazione delle source */
    if(create_source() < 0) {
        ERROR_MESSAGE("impossibile creare un processo source");
    }

    /* Stampa delle informazioni associate a ciascun processo all'inizio della simulazione */
    DEBUG_MESSAGE("processi generati all'inizio della simulazione:");
#ifdef DEBUG
    printf("%s%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN);
    processes_info_list_print(kids_list);
    printf("%s", ANSI_COLOR_RESET);
    /* Stampa dello stato di occupazione delle celle della mappa */
    shm_map_print_state(shm_p, sems_access_id);
#endif
    /* ================================== */

    /* Attesa dell'inizio della simulazione: la simulazione ha inizio solo quando tutti
     * i processi figli hanno terminato l'inizializzazione. Anche il main attende su questo
     * semaforo, in modo tale che anche il contatore dei secondi abbia inizio quando tutti
     * i processi sono pronti e inizializzati */
    if(sem_wait_for_0(sem_start_id, 0) < 0) {
        ERROR_EXIT_SEQ("errore nell'attesa dello sblocco del semaforo start");
    }

    /* == INIZIO DELLA SIMULAZIONE == */
#ifdef ENHANCED_PRINT
    printf("   SERVIZIO TAXI DELLA CITTA' DI CUNEO\n");
    printf("\n🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴\n⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪\n🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴\n⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪\n🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴\n");
    printf("\n - 🌍 Posizione: 44°23′N 7°33′E\n - 🚖 Numero di taxi in circolazione: %d\n - 🏰 Numero di luoghi di interesse: %d\n\n", conf.so_taxi, conf.so_sources);
#else
    printf("   SERVIZIO TAXI DELLA CITTA' DI CUNEO\n");
    printf("\n - Posizione: 44°23′N 7°33′E\n - Numero di taxi in circolazione: %d\n - Numero di luoghi di interesse: %d\n\n", conf.so_taxi, conf.so_sources);
#endif
    shm_map_print_type(shm_p); /* Stampa della mappa */
    printf("\n");

    n_successfull_trips = 0;
    n_aborted_trips = 0;
    n_not_addressed_requests = 0;
    /* Viene impostato il primo alarm, i successivi a intervalli di 1 secondo verranno
     * impostati all'interno dell'handler che gestice il segnale SIGALRM */
    alarm(1);
    /* Il processo legge i messaggi che gli sono inviati di report
     * fintanto che la simulazione non termina */
    while(end_simulation != 1) {
        /* Lettura di un messaggio dalla coda dei report */
        if(msgrcv(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0 /* Qualunque messaggio */, 0) < 0 ) {
            if (errno != EINTR) {
                /* Altro errore di lettura: viene segnalato ma la simulazione non termina */
                ERROR_MESSAGE("errore nella lettura di un messaggio di report");
            }
            /* Qualunque sia l'errore, il processo riprova a leggere ricominciando il ciclo
             * e controllando che la simulazione sia terminata. In questo modo si evita che il
             * processo si blocchi per un tempo indefinito sulla lettura dei messaggi, assicurando
             * una terminazione della simulazione */
            continue;
        }
#ifdef ENHANCED_PRINT
        DEBUG_MESSAGE("📥 ricevuto un messaggio di report");
#else
        DEBUG_MESSAGE("ricevuto un messaggio di report");
#endif
#ifdef DEBUG
        printf("%s%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN);
        /* Se non ci sono stati errori, il messaggio viene stampato */
        msg_report_print(&msg_report);
        printf("%s", ANSI_COLOR_RESET);
#endif
        if(msg_report.type == MSG_REPORT_TYPE_ABORTED) {
            if(msg_report.state == STATE_SERVING_REQ) n_aborted_trips++;
            /* Se il messaggio è di tipo abortito, il taxi corrispondente ha terminato
             * la sua esecuzione.
             * wait del processo (evita la creazione di processi zombie) */
            while(waitpid(msg_report.sender_pid, NULL, 0) < 0) {
                if (errno != EINTR) {
                    ERROR_MESSAGE("impossibile eseguire la wait di un processo");
                    /* In caso di errore il processo continua la sua esecuzione */
                    break;
                }
            }
            /* Creazione di un nuovo processo taxi */
            if (create_taxi(msg_report.sender_pid /* Pid del processo che ha terminato */) < 0)
                ERROR_MESSAGE("impossibile creare un taxi dopo la ricezione di un messaggio aborted");
        } else {
            /* Se il messaggio non è di tipo abortito, i record vengono aggiornati */
            n_successfull_trips++;
            process_info = processes_info_list_get_from_pid(kids_list, msg_report.sender_pid);
            if(process_info == NULL) {
                ERROR_MESSAGE("impossibile trovare il pid richiesto nella lista dei processi figli");
            } else {
                /* Aggiornamento delle statistiche associate al taxi nella lista dei processi figli */
                process_info->n_requests++;
                process_info->n_crossed_cells += msg_report.n_crossed_cells;
                if(process_info->longest_ride < msg_report.time) process_info->longest_ride = msg_report.time;
            }
        }
    }
    /* ============================== */

    /* == FINE DELLA SIMULAZIONE == */
#ifdef ENHANCED_PRINT
    printf("⏰ TIMEOUT ⏰\n");
#else
    printf("== TIMEOUT ==\n");
#endif

    DEBUG_MESSAGE("processi generati alla fine della simulazione:");
#ifdef DEBUG
    printf("%s%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN);
    processes_info_list_print(kids_list);
    printf("%s", ANSI_COLOR_RESET);
#endif

    /* Uccisione dei figli */
    kill_kids();

    /* Il processo verifica che non ci siano più messaggi di report da leggere */
    do {
        if(msgctl(msg_reports_id, IPC_STAT, &msg_reports_info) < 0) {
            ERROR_MESSAGE("impossibile prelevare le informazioni dalla coda dei messaggi di report");
            break; /* Uscita dal ciclo e chiusura del programma */
        } else {
            /* Lettura di uno dei messaggi rimasti */
            if(msg_reports_info.msg_qnum != 0) {
                DEBUG_MESSAGE("❓ Rimangono messaggi da leggere");
                if (msgrcv(msg_reports_id, &msg_report, sizeof(msg_report) - sizeof(msg_report.type), 0 /* Qualunque messaggio */, 0) < 0) {
                    ERROR_MESSAGE("Impossibile ricevere un messaggio dalla coda");
                    break; /* Uscita dal ciclo e chiusura del programma */
                }
#ifdef ENHANCED_PRINT
                DEBUG_MESSAGE("📥 ricevuto un messaggio di report");
#else
                DEBUG_MESSAGE("ricevuto un messaggio di report");
#endif
#ifdef DEBUG
                printf("%s%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN);
                msg_report_print(&msg_report);
                printf("%s", ANSI_COLOR_RESET);
#endif

                if(msg_report.type == MSG_REPORT_TYPE_ABORTED) {
                    if(msg_report.state == STATE_SERVING_REQ)
                        n_aborted_trips++;
                    /* La wait del taxi è già stata effettuata nella chiamata a kill_kids() precedente.
                     * Essendo la simulazione terminata, il taxi non viene più ricreato */
                } else {
                    /* Se il messaggio non è di tipo abortito, i record vengono aggiornati */
                    n_successfull_trips++;
                    process_info = processes_info_list_get_from_pid(kids_list, msg_report.sender_pid);
                    if(process_info == NULL) {
                        ERROR_MESSAGE("impossibile trovare il pid richiesto nella lista dei processi figli");
                    } else {
                        /* Aggiornamento delle statistiche associate al taxi nella lista dei processi figli */
                        process_info->n_requests++;
                        process_info->n_crossed_cells += msg_report.n_crossed_cells;
                        if(process_info->longest_ride < msg_report.time) process_info->longest_ride = msg_report.time;
                    }
                }
            }
        }
    } while(msg_reports_info.msg_qnum != 0); /* Fintanto che rimangono messaggi da leggere */

    wait_kids();

    /* Lettura del numero di messaggi di richieste presenti nella coda.
     * Il numero di questi messaggi rappresenta il numero di viaggi inevasi */
    if(msgctl(msg_requests_id, IPC_STAT, &msg_requests_info) < 0) {
        ERROR_MESSAGE("impossibile prelevare le informazioni dalla coda dei messaggi di richieste");
    } else {
        n_not_addressed_requests = msg_requests_info.msg_qnum;
    }
    /* Acquisizione dei record */
    process_info_list_get_best_n_crossed_cells(kids_list, &pid_best_n_crossed_cells, &best_n_crossed_cells);
    if(best_n_crossed_cells < 0)
        ERROR_MESSAGE("impossibile calcolare il taxi che ha percorso più strada");
    process_info_list_get_best_longest_ride(kids_list, &pid_best_longest_ride, &best_longest_ride);
    if(best_longest_ride < 0) /* Implementare meglio */
        ERROR_MESSAGE("impossibile calcolare il taxi che fatto la corsa più lunga");
    process_info_list_get_best_n_requests(kids_list, &pid_best_n_requests, &best_n_requests);
    if(best_n_requests < 0) /* Implementare meglio */
        ERROR_MESSAGE("impossibile calcolare il taxi che evaso più richieste");

    /* Stampa del record finale */
#ifdef ENHANCED_PRINT
    printf("\n📈 REPORT FINALE 📈\n");
    printf("  📋 Informazioni generali 📋\n");
    printf("    ✅ Numero di viaggi portati a termine: %d\n", n_successfull_trips);
    printf("    ❌ Numero di viaggi abortiti: %d\n", n_aborted_trips);
    printf("    ⭕ Numero di viaggi inevasi: %d\n", n_not_addressed_requests);
    printf("  🚖 Record taxi 🚖\n");
    printf("    ⚫ Taxi che ha percorso più strada: %d (%d celle)\n", pid_best_n_crossed_cells, best_n_crossed_cells);
    printf("    ⌚ Taxi che ha compiuto la corsa più lunga: %d (%ld nanosec)\n", pid_best_longest_ride, best_longest_ride);
    printf("    📨 Taxi che ha evaso più richieste: %d (%d richieste)\n", pid_best_n_requests, best_n_requests);
    printf("  🔝 Celle top 🔝\n");
#else
    printf("\n== REPORT FINALE ==\n");
    printf("  - Informazioni generali\n");
    printf("    - Numero di viaggi portati a termine: %d\n", n_successfull_trips);
    printf("    - Numero di viaggi abortiti: %d\n", n_aborted_trips);
    printf("    - Numero di viaggi inevasi: %d\n", n_not_addressed_requests);
    printf("  - Record taxi\n");
    printf("    - Taxi che ha percorso più strada: %d (%d celle)\n", pid_best_n_crossed_cells, best_n_crossed_cells);
    printf("    - Taxi che ha compiuto la corsa più lunga: %d (%ld nanosec)\n", pid_best_longest_ride, best_longest_ride);
    printf("    - Taxi che ha evaso più richieste: %d (%d richieste)\n", pid_best_n_requests, best_n_requests);
        printf("  - Celle top\n");
#endif
    if(shm_map_print_top_cells(shm_p, sems_counter_id, conf.so_top_cells) < 0)
        ERROR_MESSAGE("impossibile stampare le celle più attraversate della mappa");

    free_sysV(); /* Cancellazione delle strutture sysV create */
    free_mem(); /* Liberazione dalla memoria delle strutture dati allocate dinamicamente */
    exit(0);
    /* ============================ */
}

/* La funzione crea un nuovo taxi.
 * Una nuova struttura per memorizzare i record del taxi viene inserita all'interno
 * della lista apposita. old_pid specifica il pid del taxi che viene rimpiazzato.
 * Se old_pid = 0 nessun taxi viene rimpiazzato */
int create_taxi(pid_t old_pid)
{
    char* taxi_argv[] = {"build/bin/taxi", NULL, NULL, NULL}; /* argv di taxi */
    struct point starting; /* Coordinate di partenza di un taxi, generate casualmente */
    pid_t kid_pid;

    taxi_argv[1] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1 */
    taxi_argv[2] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1*/

    switch(kid_pid = fork()) {
        case -1: /* Errore */
            return -1;
        case 0: /* Figlio */
            /* Creazione di un processo taxi */
            /* Formato di argv da passare a taxi:
             * [0] taxi
             * [1] posizione row iniziale del taxi
             * [2] posizione col iniziale del taxi */
            /* Generazione di coordinate casuali che non siano buche */
            srand(getpid()); /* Necessario, altrimenti tutti i processi genererebbero le stesse coordinate */
            do {
                starting.row = rand() % SO_HEIGHT;
                starting.col = rand() % SO_WIDTH;
                /* Nuove coordinate vengono generate se quelle attuali farebbero nascere il taxi sopra
                 * ad una buca */
            } while(shm_p->map[starting.row][starting.col].type == CELL_TYPE_HOLE);
            /* Cella non buca */
            sprintf(taxi_argv[1], "%d", starting.row);
            sprintf(taxi_argv[2], "%d", starting.col);
            execve(taxi_argv[0], taxi_argv, NULL);
            ERROR_MESSAGE("impossibile creare un taxi");
            return -1;
        default: /* Padre */
            /* Impostazione del flag di terminazione del processo old_pid */
            processes_info_list_set_state(kids_list, old_pid, PROCESS_INFO_STATE_TERMINATED);
            /* Inserimento del nuovo processo nella lista */
            kids_list = processes_info_list_insert(kids_list, kid_pid, PROCESS_INFO_TYPE_TAXI);
            /* Liberazione della memoria */
            if(taxi_argv[1] != NULL) free(taxi_argv[1]);
            if(taxi_argv[2] != NULL) free(taxi_argv[2]);
            DEBUG_MESSAGE("creato un processo taxi");
            break;
    }

    return 0;
}

/* La funzione crea una nuova source */
int create_source()
{
    char* source_argv[] = {"build/bin/source", NULL, NULL, NULL}; /* argv di source */
    static int counter = 0; /* Numero della lista creata */
    pid_t kid_pid;
    int i, j;

    /* Scorrimento della mappa alla ricerca delle posizioni delle source */
    for(i = 0; i < SO_HEIGHT; i++) {
        for(j = 0; j < SO_WIDTH; j++) {
            if(shm_p->map[i][j].type == CELL_TYPE_SOURCE) {
                source_argv[1] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1 */
                source_argv[2] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1*/

                switch(kid_pid = fork()) {
                    case -1: /* Errore */
                        return -1;
                    case 0: /* Figlio */
                        /* Creazione di un processo source */
                        /* Formato di argv da passare a source:
                         * [0] source
                         * [1] posizione row della source
                         * [2] posizione col della source */
                        source_argv[1] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1 */
                        source_argv[2] = (char*)malloc(11 * sizeof(char)); /* 11 = numero caratteri INT_MAX + 1*/
                        sprintf(source_argv[1], "%d", i);
                        sprintf(source_argv[2], "%d", j);
                        execve(source_argv[0], source_argv, NULL);
                        ERROR_MESSAGE("impossibile creare una source");
                        return -1;
                    default: /* Padre */
                        /* Inserimento del nuovo processo nella lista.
                         * Le source per semplicità convivono nella stessa lista dei taxi.
                         * Le informazioni sui record dei taxi vengono ignorate nel caso delle source. */
                        kids_list = processes_info_list_insert(kids_list, kid_pid, PROCESS_INFO_TYPE_SOURCE);
                        if(source_argv[1] != NULL) free(source_argv[1]);
                        if(source_argv[2] != NULL) free(source_argv[2]);
                        counter++;
                        DEBUG_MESSAGE("creato un processo source");
                        break;
                }
            }
        }
    }

    return 0;
}

/* Procedura per l'eliminazione di tutte le strutture sysV create.
 * Aggiungere una riga per ogni nuova struttura creata */
void free_sysV()
{
    /* Rimozione del semaforo di start */
    if(sem_start_id >= 0 && semctl(sem_start_id, 0 /* Ignorato */, IPC_RMID) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere il semaforo start");

    /* Rimozione dell'array di semafori di accesso alle celle */
    if(sems_access_id >= 0 && semctl(sems_access_id, 0 /* Ignorato */, IPC_RMID) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere l'array di semafori di accesso alle celle");

    /* Rimozione dell'array di semafori per i contatori di accessi delle celle  */
    if(sems_counter_id >= 0 && semctl(sems_counter_id, 0 /* Ignorato */, IPC_RMID) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere l'array di semafori per i contatori di accesso alle celle");

    /* Rimozione della shared memory */
    if(shm_id >= 0 && shmctl(shm_id, IPC_RMID, NULL) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere la shared memory");

    /* Rimozione della coda di messaggi per la gestione delle richieste */
    if(msg_requests_id >= 0 && msgctl(msg_requests_id, IPC_RMID, NULL) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere la coda di messaggi per la gestione delle richeste");

    /* Rimozione della coda di messaggi per la gestione dei report */
    if(msg_reports_id >= 0 && msgctl(msg_reports_id, IPC_RMID, NULL) < 0)
        ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a rimuovere la coda di messaggi per la gestione dei report");
}

/* Procedura per la liberazione della heap.
 * Aggiungere una riga per ogni struttura memorizzata nella heap */
void free_mem()
{
    processes_info_list_free(kids_list);
}

/* Procedura di terminazione dei figli generati */
void kill_kids()
{
    struct processes_info_list* proc = kids_list;
    /* Terminazione di tutti i processi impostati come running */
    for(; proc != NULL; proc = proc->next) {
        if(proc->state == PROCESS_INFO_STATE_RUNNING) {
            if(kill(proc->pid, SIGINT) >= 0 || errno ==  ESRCH) {
                /* Viene permesso l'errore ESRCH. Infatti un processo potrebbe aver
                 * terminato ma la segnalazione tramite messaggio non essere ancora stata
                 * letta dal main. In tal caso bisogna effettuare la wait del processo
                 * e segnalare nella lista la sua terminazione */
                DEBUG_MESSAGE("ucciso un processo");
            } else {
                if(errno == EINTR) continue;
                ERROR_MESSAGE("impossibile inviare il segnale di terminazione al processo figlio");
            }
        }
    }
}

/* Procedura di attesa dei figli generati */
void wait_kids()
{
    struct processes_info_list* proc = kids_list;
    for(; proc != NULL; proc = proc->next)
        if(proc->state == PROCESS_INFO_STATE_RUNNING) {
            waitpid(proc->pid, NULL, 0);
            proc->state = PROCESS_INFO_STATE_TERMINATED;
        }
}

/* Procedura di pausa dei figli di tipo taxi */
void pause_taxi_kids()
{
    struct processes_info_list* proc = kids_list;
    /* Terminazione di tutti i processi impostati come running */
    for(; proc != NULL; proc = proc->next) {
        if(proc->state == PROCESS_INFO_STATE_RUNNING && proc->type == PROCESS_INFO_TYPE_TAXI) {
            if(kill(proc->pid, SIGSTOP) >= 0 || errno == ESRCH) {
                /* Viene permesso l'errore ESRCH. Infatti un processo potrebbe aver
                 * terminato ma la segnalazione tramite messaggio non essere ancora stata
                 * letta dal main */
                DEBUG_MESSAGE("messo in pausa un processo");
            } else {
                if(errno == EINTR) continue;
                ERROR_MESSAGE("impossibile inviare il segnale di pausa al processo figlio");
            }
        }
    }
}

/* Procedura di riesumazione dei figli di tipo taxi */
void resume_taxi_kids()
{
    struct processes_info_list* proc = kids_list;
    /* Terminazione di tutti i processi impostati come running */
    for(; proc != NULL; proc = proc->next) {
        if(proc->state == PROCESS_INFO_STATE_RUNNING && proc->type == PROCESS_INFO_TYPE_TAXI) {
            if(kill(proc->pid, SIGCONT) >= 0 || errno == ESRCH) {
                /* Viene permesso l'errore ESRCH. Infatti un processo potrebbe aver
                 * terminato ma la segnalazione tramite messaggio non essere ancora stata
                 * letta dal main */
                DEBUG_MESSAGE("riesumanto un processo");
            } else {
                if(errno == EINTR) continue;
                ERROR_MESSAGE("impossibile il segnale di riesumazione al processo figlio");
            }
        }
    }
}

/* Handler dei segnali.
 * L'handler intercetta i segnali SIGINT, SIGTERM e SIGALRM */
void signals_handler(int signum)
{
    int old_errno;
    static int invocations_counter = 0;

    old_errno = errno;

    SIGNAL_MESSAGE("ricevuto segnale", signum);
    switch(signum) {
        case SIGINT: /* Ricevuto segnale SIGINT o SIGTERM, è necessario pulire le strutture sysV create */
        case SIGTERM:
            if(getpid() == main_pid) {
                EXIT_SEQ(0);
            } else {
                exit(0);
            }
        case SIGALRM:
            if(getpid() == main_pid) {
                /* Ricevuto segale SIGALRM */
                invocations_counter++;
                /* All'ultima invocazione viene terminata la simulazione */
                if(invocations_counter == conf.so_duration) end_simulation = 1;
                else alarm(1);
                /* Ogni secondo viene stampata la mappa */
                pause_taxi_kids();
#ifdef DEBUG
                printf("\nnum active taxi: %d\n", count_active_taxi());
#endif
                /* processes_info_list_print(kids_list); */
                if(shm_map_print_state(shm_p, sems_access_id) < 0)
                    ERROR_MESSAGE("errore nella stampa della mappa");
                printf("\n");
                resume_taxi_kids();
            }
            break;
        default: break;
    }

    errno = old_errno;
}

#ifdef DEBUG
int count_active_taxi() {
    int count = 0;
    struct processes_info_list* proc = kids_list;
    for(; proc != NULL; proc = proc->next) {
        if(proc->type == PROCESS_INFO_TYPE_TAXI && proc->state == PROCESS_INFO_STATE_RUNNING) count++;
    }

    return count;
}
#endif
