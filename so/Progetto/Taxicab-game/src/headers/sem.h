#ifndef TAXICAB_GAME_SEM_H
#define TAXICAB_GAME_SEM_H

int sem_lock(int sem_id, int sem_idx);
int sem_unlock(int sem_id, int sem_idx);
int sem_wait_for_0(int sem_id, int sem_idx);

#endif /* TAXICAB_GAME_SEM_H */