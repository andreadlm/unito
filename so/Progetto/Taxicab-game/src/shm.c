#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/sem.h>

#include "headers/global.h"
#include "headers/conf_file.h"
#include "headers/cell.h"
#include "headers/shm.h"

/* Costruttore per la struttura point */
void point_constructor(struct point* self, int x, int y)
{
    self->row = x;
    self->col = y;
}

/* Semplice stampa di point */
void point_print(struct point* self)
{
    printf("{row = %d | col = %d}\t", self->row, self->col);
}

/* La funzione inizializza ciascuna cella della mappa sulla base
 * del modello fornito e di valori casuali nei range forniti dalla
 * configurazione */
short int shm_constructor(struct shm *s, short int map_model[SO_HEIGHT][SO_WIDTH], int sems_access_id, int sems_counter_id, struct conf conf)
{
    int i, j;
    int sem_access_idx_counter = 0;

    for(i = 0; i < SO_HEIGHT; i++) {
        for (j = 0; j < SO_WIDTH; j++) {
            if(cell_constructor(&s->map[i][j],
                                map_model[i][j],
                                rand() % (conf.so_timesec_max - conf.so_timesec_min + 1) + conf.so_timesec_min,
                                sem_access_idx_counter,
                                sems_access_id,
                                sems_counter_id,
                                rand() % (conf.so_cap_max - conf.so_cap_min + 1) + conf.so_cap_min) == -1)
                return -1;
            if(s->map[i][j].type != CELL_TYPE_HOLE)
                sem_access_idx_counter++;
        }
    }

    return 0;
}

/* La funzione stampa la mappa. Solo il tipo delle celle viene mostrato */
void shm_map_print_type(struct shm* s)
{
    int i, j;

#ifdef ENHANCED_PRINT
    printf("🧭 MAPPA 🧭\n");
#else
    printf("== MAPPA ==\n");
#endif
    for(i = 0; i < SO_HEIGHT; i++) {
        for (j = 0; j < SO_WIDTH; j++) {
            cell_print_type(&s->map[i][j]);
        }
        printf("\n");
    }
}

/* La funzione stampa la mappa. Il tipo delle celle e il loro stato
 * di occupazione vengono mostrati */
int shm_map_print_state(struct shm* s, int sem_access_id)
{
    int i, j;
#ifdef DEBUG
    int sum;
    sum = 0;
#endif

#ifdef ENHANCED_PRINT
    printf("🧭 STATO OCCUPAZIONE CELLE 🧭\n");
#else
    printf("== STATO OCCUPAZIONE CELLE ==\n");
#endif
    for(i = 0; i < SO_HEIGHT; i++) {
        for (j = 0; j < SO_WIDTH; j++) {
#ifdef DEBUG
            sum += cell_print_state(&s->map[i][j], sem_access_id);
#else
            cell_print_state(&s->map[i][j], sem_access_id);
#endif
        }
        printf("\n");
    }
#ifdef DEBUG
    printf("tot: %d\n", sum);
#endif
    return 0;
}

/* Stampa la mappa con le SO_TOP_CELLS evidenziate */
int shm_map_print_top_cells(struct shm* s, int sems_counter_id, int so_top_cells)
{
    int i, j;

    if(set_top_cells(s, sems_counter_id, so_top_cells) < 0) /* Errore */ return -1;

    for(i = 0; i < SO_HEIGHT; i++) {
        printf("    ");
        for (j = 0; j < SO_WIDTH; j++) {
            /* Stampa la cella formattata sulla base del fatto che sia stata
             * identificata come una delle SO_TOP_CELLS */
            cell_print_top(&s->map[i][j]);
        }
        printf("\n");
    }

    return 0;
}

/* Imposta a -1 il semaforo contatore degli accessi delle
 * SO_TOP_CELLS celle */
int set_top_cells(struct shm* s, int sems_counter_id, int so_top_cells)
{
    int k, i, j;
    struct point best;
    int n_crossings, best_n_crossings;

    /* La mappa viene fatta scorrere SO_TOP_CELLS volte
     * e ad ogni iterazione viene identificata la cella
     * con il numero di accessi maggiore */
    for(k = 0; k < so_top_cells; k++) {
        best_n_crossings = -1;
        for(i = 0; i < SO_HEIGHT; i++) {
            for(j = 0; j < SO_WIDTH; j++) {
                if(s->map[i][j].type != CELL_TYPE_HOLE) {
                    n_crossings = semctl(sems_counter_id, s->map[i][j].sem_access_idx, GETVAL);
                    if(n_crossings < 0) /* Errore */ return -1;
                    else if ((s->map[i][j].type & CELL_TOP_FLAG) == 0 && n_crossings > best_n_crossings) {
                        best.row = i;
                        best.col = j;
                        best_n_crossings = n_crossings;
                    }
                }
            }
        }
        /* La migliore cella dell'iterazione corrente viene marcata come una delle SO_TOP_CELLS */
        s->map[best.row][best.col].type |= CELL_TOP_FLAG;
    }

    return 0;
}

/* La funzione genera un modello casuale per l'inizializzazione
 * della mappa contenuta nella shared memory */
void generate_map_model(short int map_model[SO_HEIGHT][SO_WIDTH], int so_holes, int so_sources)
{
    int av_cells = SO_HEIGHT * SO_WIDTH; /* Numero di celle disponibili al posizionamento di una buca */
    int i, j; /* Indici utilizzati per scorrere la matrice */
    int count; /* Conta il numero di celle di un dato tipo da posizionare */
    int n_free; /* Numero di celle libere sulla mappa (celle in cui poter posizionare una buca */

    /* Posizionamento sequenziale delle buche */
    for(i = 0, count = so_holes; i < SO_HEIGHT && count > 0; i += 2) {
        for (j = 0; j < SO_WIDTH && count > 0; j += 2, count--) {
            av_cells -= op_cell(map_model, i, j, OP_MODE_INSERT);
        }
    }

    /* Ciclo per ogni buca presente moltiplicato per un fattore.
     * Termina nel caso in cui non ci siano più celle libere */
    srand(getpid());
    for(i = 0; i < so_holes * SHUFFLE_FACTOR && av_cells > 0; i++) {
        int row_idx_old_hole, /* Indice di riga corrispondente alla buca da spostare */
        col_idx_old_hole, /* Indice di colonna corrispondente alla buca da spostare */
        row_idx_new_hole, /* Indice di riga corrispondente alla nuova posizione della buca */
        col_idx_new_hole; /* Indice di colonna corrispondente alla nuova posizione della buca */
        /* Scelta di una buca casuale da spostare.
         * Viene selzionato un indice compreso tra 0 e SO_HOLES - 1 corrispondente alla
         * n_hole-esima buca presente sulla mappa */
        int n_hole = rand() % so_holes;
        /* Ottenimento delle coordinate del n-hole-esima buca sulla mappa */
        get_cell_by_type(map_model, n_hole, CELL_TYPE_HOLE, &row_idx_old_hole, &col_idx_old_hole);
        /* Rimozione della buca dalla sua vecchia posione e aggiunta delle nuove celle libere */
        av_cells += op_cell(map_model, row_idx_old_hole, col_idx_old_hole, OP_MODE_REMOVE);
        /* Scelta di una cella libera casuale dove spostare la buca.
         * Viene selezionato un indice compreso tra 0 e av_cells -1 corrispondente alla
         * n_free-esima cella libera presente sulla mappa */
        n_free = rand() % av_cells;
        /* Ottenimento delle coordinate della n_free-esima cella libera sulla mappa */
        get_cell_by_type(map_model, n_free, CELL_TYPE_ROAD, &row_idx_new_hole, &col_idx_new_hole);
        /* Inserimento della buca nella nuova posizione e rimozione delle celle che non sono più libere */
        av_cells -= op_cell(map_model, row_idx_new_hole, col_idx_new_hole, OP_MODE_INSERT);
    }

    /* Azzeramento delle celle libere */
    for(i = 0; i < SO_HEIGHT; i++)
        for(j = 0; j < SO_WIDTH; j++)
            if(map_model[i][j] > CELL_TYPE_ROAD) map_model[i][j] = 0;

    /* Posizionamento casuale delle sorgenti */
    count = so_sources;
    while(count > 0) {
        i = rand() % SO_HEIGHT;
        j = rand() % SO_WIDTH;
        if(map_model[i][j] == CELL_TYPE_ROAD) {
            map_model[i][j] = CELL_TYPE_SOURCE;
            count--;
        }
    }
}

/* La funzione ritorna la n-esima cella di tipo type presente nella mappa.
 * map_model - mappa
 * n - numero della cella
 * type - tipo della cella
 * *row_idx - indice della riga da restituire al chiamante
 * *col_idx - indice della colonna da restituire al chiamante */
void get_cell_by_type(short int map_model[SO_HEIGHT][SO_WIDTH], int n, int type, int* row_idx, int* col_idx)
{
    int i, j;
    for(i = 0; i < SO_HEIGHT && n >= 0; i++) {
        for (j = 0; j < SO_WIDTH && n >= 0; j++) {
            if (map_model[i][j] == type) n--;
            if (n < 0) {
                *row_idx = i;
                *col_idx = j;
            }
        }
    }
}

/* La funzione effettua un'operazione su una cella.
 * map_model - mappa
 * row_idx - indice della riga su cui effettuare l'operazione
 * col_idx - indice della colonna su cui effettuare l'operazione
 * op - operazione da eseguire (inserimento di una buca/rimozione di una buca)
 * La funzione ritorna il numero di celle che hanno alterato il loro stato
 * durante l'operazione: il numero di celle che sono diventate libere in caso
 * di rimozione di una buca, il numero di celle che sono diventate "occupate" in
 * caso di inserimento di una buca */
int op_cell(short int map_model[SO_HEIGHT][SO_WIDTH], int row_idx, int col_idx, short int op)
{
    int changed_state_counter = 0;
    int i, j;

    /* Scorrimento della sottomatrice corrispondente alle celle attorno
     * alla cella su cui si deve eseguire l'operazione */
    for(i = row_idx - 1; i < row_idx + 2; i++) {
        for(j = col_idx - 1; j < col_idx + 2; j++) {
            /* Verifica che l'indice non sia esterno alla matrice */
            if(i >= 0 && j >= 0 && i < SO_HEIGHT && j < SO_WIDTH) {
                if(i == row_idx && j == col_idx) map_model[i][j] = op;
                else {
                    if(op == OP_MODE_INSERT) {
                        if(map_model[i][j] == 0) changed_state_counter++;
                        /* Il numero contenuto in ogni cella rappresenta il numero di buche
                         * attorno che la "bloccano" */
                        map_model[i][j]++;
                    } else if(op == OP_MODE_REMOVE) {
                        if(map_model[i][j] == 1) changed_state_counter++;
                        /* Nessuna cella può contenere valori negativi, -1 identifica una buca */
                        if(map_model[i][j] > 0) map_model[i][j]--;
                    }
                }
            }
            /* Il ciclo continua verso la cella successiva */
        }
    }

    return changed_state_counter + 1;
}

/* Funzione di stampa della matrice */
void print_map_model(short int map_model[SO_HEIGHT][SO_WIDTH])
{
    int i, j;
    for (i = 0; i < SO_HEIGHT; i++) {
        for (j = 0; j < SO_WIDTH; j++) {
#ifdef ENHANCED_PRINT
            if(map_model[i][j] == CELL_TYPE_HOLE) printf("[🚧]");
            else if(map_model[i][j] == CELL_TYPE_ROAD) printf("[⚫]");
            else if(map_model[i][j] == CELL_TYPE_SOURCE) printf("[🏰]");
#else
            if(map_model[i][j] == CELL_TYPE_HOLE) printf("[%d]", CELL_TYPE_HOLE);
            else if(map_model[i][j] == CELL_TYPE_ROAD) printf("[ %d]", CELL_TYPE_ROAD);
            else if(map_model[i][j] == CELL_TYPE_SOURCE) printf("[ %d]", CELL_TYPE_SOURCE);
#endif
        }
        printf("\n");
    }
}