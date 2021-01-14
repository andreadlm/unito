#include <sys/sem.h>
#include <string.h>
#include <errno.h>

#include "headers/sem.h"

/* La funzione esegue una lock sull'sem_idx-esimo
 * semaforo dell'array sem_id.
 * Ritorna 0 in caso di successo, -1 in caso di errore */
int sem_lock(int sem_id, int sem_idx)
{
    struct sembuf op_sem;
    op_sem.sem_op = -1;
    op_sem.sem_num = sem_idx;
    op_sem.sem_flg = 0;
    if(semop(sem_id, &op_sem, 1) < 0) return -1;
    return 0;
}

/* La funzione esegue una unlock sull'sem_idx-esimo
 * semaforo dell'array sem_id.
 * Ritorna 0 in caso di successo, -1 in caso di errore */
int sem_unlock(int sem_id, int sem_idx)
{
    struct sembuf op_sem;
    op_sem.sem_op = 1;
    op_sem.sem_num = sem_idx;
    op_sem.sem_flg = 0;
    if(semop(sem_id, &op_sem, 1) < 0) return -1;
    return 0;
}

/* La funzione esegue una wait-for-zero sull'sem_idx-esimo
 * semaforo dell'array sem_id.
 * Ritorna 0 in caso di successo, -1 in caso di errore */
int sem_wait_for_0(int sem_id, int sem_idx)
{
    struct sembuf op_sem;
    op_sem.sem_op = 0;
    op_sem.sem_num = sem_idx;
    op_sem.sem_flg = 0;
    if(semop(sem_id, &op_sem, 1) < 0) return -1;
    return 0;
}