#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define INIT_V_SIZE 1

bool is_prime(unsigned int num);
unsigned int next_prime(unsigned int prime);

/*
 * La funzione alloca autonomamente due vettori di dimensione
 * variabile in base al numero di primi che fattorizzano num.
 * Ritorna un vettore di numeri primi e un vettore che rappresenta
 * l'esponente di ciascun primo.
 * Entrambi i vettori sono terminati da 0.
 *
 * Attenzione: possibili aree di memoria già allocate passate alla
 * funzione andranno perse.
 */
void factor(unsigned int num, unsigned int** primes, unsigned int** reps)
{
    int idx = 0;
    *primes = (unsigned int*)malloc(sizeof(**primes) * INIT_V_SIZE);
    *reps = (unsigned int*)malloc(sizeof(**reps) * INIT_V_SIZE);

    unsigned int prime;
    for(prime = 2; prime <= num; prime = next_prime(prime)) {
        if(num % prime == 0) {
            (*primes)[idx] = prime;
            (*reps)[idx] = 0;

            while(num % prime == 0) {
                num /= prime;
                (*reps)[idx]++;
            }

            idx++;

            *primes = (unsigned int*)realloc(*primes, sizeof(**primes) * (idx + 1));
            *reps = (unsigned int*)realloc(*reps, sizeof(**reps) * (idx + 1));
        }
    }

}

void print_factor(int num, int* primes, int* reps)
{
    int i;
    for(i = 0; primes[i] != 0; i++) {
#ifndef CSV
        if (i == 0) printf("%d = ", num);
        printf("%d^%d%c", primes[i], reps[i], primes[i + 1] != 0 ? '*' : ' ');
#else
        printf("%d,%d\n", primes[i], reps[i]);
#endif
    }
}

unsigned int next_prime(unsigned int prime)
{
    unsigned int next = (prime % 2 == 0) ? prime + 1 : prime + 2;

    for(; ; next += 2)
        if(is_prime(next)) return next;

}

bool is_prime(unsigned int num)
{
    int div;
    for(div = 2; div < num / 2 && num % div != 0; div++) {}

    return div >= num / 2;
}
