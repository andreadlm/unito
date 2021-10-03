/* Written by Andrea Delmastro
 * Part of the algorithm and data structures course project
 * Exercise 2: dynamic edit distance.
 *
 * The library implements two different ways of writing the edit distance function: the fist one uses a simple
 * recursive logic, while the second one uses a dynamic logic in order to minimize the complexity of the algorithm.
 * The edit distance between two strings is defined as the minimum number of operations needed to transform a
 * string into another. In the implemented functions only insertions and deletions are allowed. */

#include <string.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include "edit_distance.h"

#define NK (-1)

#define min(x, y, z) (x) < (y) ? ((x) < (z) ? (x) : (z)) : ((y) < (z) ? (y) : (z))

long int edit_distance_dyn_mem(char*, char*, long int**);

long int edit_distance(char* s1, char* s2)
{
  if(s1 == NULL) {
    fprintf(stderr, "edit_distance: s1 parameter cannot be NULL\n");
    return -1;
  }
  if(s2 == NULL) {
    fprintf(stderr, "edit_distance: s2 parameter cannot be NULL\n");
    return -1;
  }

  if(strlen(s1) == 0) return (long int)strlen(s2);
  if(strlen(s2) == 0) return (long int)strlen(s1);

  long int do_no_op = *s1 == *s2 ? edit_distance(s1 + 1, s2 + 1) : LONG_MAX;
  long int d_canc = 1 + edit_distance(s1, s2 + 1);
  long int d_ins = 1 + edit_distance(s1 + 1, s2);

  return min(do_no_op, d_canc, d_ins);
}

long int edit_distance_dyn(char* s1, char* s2)
{
  if(s1 == NULL) {
    fprintf(stderr, "edit_distance_dyn: s1 parameter cannot be NULL\n");
    return -1;
  }
  if(s2 == NULL) {
    fprintf(stderr, "edit_distance_dyn: s2 parameter cannot be NULL\n");
    return -1;
  }

  long int** m = (long int**)malloc((strlen(s2) + 1) * sizeof(long int*));
  for(size_t i = 0; i <= strlen(s2); i++)
    m[i] = (long int*)malloc((strlen(s1) + 1) * sizeof(long int));

  for(size_t i = 0; i <= strlen(s2); i++)
    m[i][0] = (long int)i;
  for(size_t j = 0; j <= strlen(s1); j++)
    m[0][j] = (long int)j;

  for(size_t i = 1; i <= strlen(s2); i++)
    for(size_t j = 1; j <= strlen(s1); j++)
      m[i][j] = NK;

  long int edit_distance = edit_distance_dyn_mem(s1, s2, m);

  for(size_t i = 0; i <= strlen(s2); i++)
    free(m[i]);
  free(m);

  return edit_distance;
}

/* Recursive dynamic implementation of the edit distance function that uses a matrix to memoize the intermediate
 * results.
 * The function accepts two strings and a pre-initialized matrix. Il returns the edit distance between the two
 * strings.
 * The input parameters cannot be NULL. */
long int edit_distance_dyn_mem(char* s1, char* s2, long int** m)
{
  if(s1 == NULL) {
    fprintf(stderr, "edit_distance_dyn_mem: s1 parameter cannot be NULL\n");
    return -1;
  }
  if(s2 == NULL) {
    fprintf(stderr, "edit_distance_dyn_mem: s2 parameter cannot be NULL\n");
    return -1;
  }
  if(m == NULL) {
    fprintf(stderr, "edit_distance_dyn_mem: m parameter cannot be NULL\n");
    return -1;
  }

  size_t i = strlen(s2);
  size_t j = strlen(s1);

  if(m[i][j] == NK) {
    long int do_no_op = *s1 == *s2 ? edit_distance_dyn_mem(s1 + 1, s2 + 1, m) : LONG_MAX;
    long int d_canc = 1 + edit_distance_dyn_mem(s1, s2 + 1, m);
    long int d_ins = 1 + edit_distance_dyn_mem(s1 + 1, s2, m);

    m[i][j] =  min(do_no_op, d_canc, d_ins);
  }

  return m[i][j];
}
