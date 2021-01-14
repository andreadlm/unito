#ifndef TAXICAB_GAME_PROCESSES_INFO_LIST_H
#define TAXICAB_GAME_PROCESSES_INFO_LIST_H

#define PROCESS_INFO_STATE_RUNNING 1
#define PROCESS_INFO_STATE_TERMINATED 0
#define PROCESS_INFO_TYPE_SOURCE 1
#define PROCESS_INFO_TYPE_TAXI 0

#include <unistd.h>

struct processes_info_list {
    pid_t pid;
    short int type;
    short int state;
    int n_crossed_cells;
    long longest_ride;
    int n_requests;
    struct processes_info_list* next;
};

struct processes_info_list* processes_info_list_insert(struct processes_info_list* self, pid_t pid, short int type);
void processes_info_list_print(struct processes_info_list* self);
void process_info_print(struct processes_info_list* self);
void processes_info_list_free(struct processes_info_list* self);
void processes_info_list_set_state(struct processes_info_list* self, pid_t pid, short int state);
struct processes_info_list* processes_info_list_get_from_pid(struct processes_info_list* self, pid_t pid);
void process_info_list_get_best_n_crossed_cells(struct processes_info_list* self, pid_t* pid, int* n_crossed_cells);
void process_info_list_get_best_longest_ride(struct processes_info_list* self, pid_t* pid, long* longest_ride);
void process_info_list_get_best_n_requests(struct processes_info_list* self, pid_t* pid, int* n_requests);
#endif /* TAXICAB_GAME_PROCESSES_INFO_LIST_H */
