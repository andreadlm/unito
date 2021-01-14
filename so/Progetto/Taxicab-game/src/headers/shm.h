#ifndef TAXICAB_GAME_SHM_H
#define TAXICAB_GAME_SHM_H

#define SHUFFLE_FACTOR 5 /* Fattore moltiplicativo di spostamento */
#define OP_MODE_INSERT CELL_TYPE_HOLE /* Modalità di inserimento di una buca */
#define OP_MODE_REMOVE CELL_TYPE_ROAD /* Modalità di rimozione di una buca */

#include "cell.h"
#include "conf_file.h"

struct point {
    int row; /* Indice di riga */
    int col; /* Indice di colonna */
};

struct shm {
    struct cell map[SO_HEIGHT][SO_WIDTH]; /* Mappa della simulazione */
};

void point_constructor(struct point* self, int x, int y);
void point_print(struct point* self);
short int shm_constructor(struct shm *s, short int map_model[SO_HEIGHT][SO_WIDTH], int sems_access_id, int sems_counter_id, struct conf conf);
void shm_map_print_type(struct shm* s);
void generate_map_model(short int map_model[SO_HEIGHT][SO_WIDTH], int so_holes, int so_sources);
void get_cell_by_type(short int map_model[SO_HEIGHT][SO_WIDTH], int n, int type, int* row_idx, int* col_idx);
int op_cell(short int map_model[SO_HEIGHT][SO_WIDTH], int row_idx, int col_idx, short int op);
void print_map_model(short int map_model[SO_HEIGHT][SO_WIDTH]);
int shm_map_print_state(struct shm* s, int sems_access_id);
int shm_map_print_top_cells(struct shm* s, int sems_counter_id, int so_top_cells);
int set_top_cells(struct shm* s, int sems_counter_id, int so_top_cells);
#endif /* TAXICAB_GAME_SHM_H */
