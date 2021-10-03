#ifndef ESERCIZIO1_MERGE_BINARY_INSERTION_SORT_TEST_H
#define ESERCIZIO1_MERGE_BINARY_INSERTION_SORT_TEST_H

#include <stddef.h>

void merge_binary_insertion_m_copy(void *a, void *b, size_t n);
size_t binary_search(void*, void*, size_t, size_t, size_t, int (*cmp_func)(const void*, const void*));
void binary_insertion_sort(void* array, size_t, size_t, size_t, int (*cmp)(const void*, const void*));
void merge(void* array, size_t, size_t, size_t, size_t, int (*cmp_func)(const void*, const void*));

#endif //ESERCIZIO1_MERGE_BINARY_INSERTION_SORT_TEST_H
