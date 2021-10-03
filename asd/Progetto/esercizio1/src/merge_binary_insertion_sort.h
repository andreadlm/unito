/* Author: Andrea Delmastro
 * The library implements a generic merge-binary-insertion-sort function. */

#ifndef MERGE_BINARY_INSERTION_SORT_H
#define MERGE_BINARY_INSERTION_SORT_H

#include <stddef.h>

/* The function sorts a generic array using a merge-binary-insertion-sort logic.
 * It accepts as parameters:
 * - a generic array
 * - the length (in number of elements) of the array
 * - the size (in bytes) of each element of the array
 * - a function that takes two pointers to two elements of the array an returns a value greater than
 *   0 if the element pointed by the first pointer is greater than the element pointed by the second
 *   pointer, a value less than 0 if the the element pointed by the first pointer is less than the element
 *   pointed by the second pointer, 0 if the two elements pointed are the same.
 * The parameters cannot be NULL.
 * The length and the size must be valid values (> 0). */
void merge_binary_insertion_sort(void*, size_t, size_t, int (*)(const void*, const void*));

#endif /* MERGE_BINARY_INSERTION_SORT_H */
