/* Author: Andrea Delmastro
 * The library implements two different ways of writing the edit distance function: the fist one uses a simple
 * recursive logic, while the second one uses a dynamic logic in order to minimize the complexity of the algorithm.
 * The edit distance between two strings is defined as the minimum number of operations needed to transform a
 * string into another. In the implemented functions only insertions and deletions are allowed. */

/* The function accepts as input two pointers to two strings and calculates the edit distance between them.
 * The input parameters cannot be NULL. */
long int edit_distance(char*, char*);

/* The function accepts as input two pointers to two strings and calculates the edit distance between them.
 * It returns the edit distance between the two strings, -1 if an error occurs.
 * The input parameters cannot be NULL. */
long int edit_distance_dyn(char*, char*);
