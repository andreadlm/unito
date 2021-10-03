/* Author: Andrea Delmastro
 * The library implements a generic ordered dynamic array.
 * A dynamic array is an array whose size can change dynamically
 * during the execution. The array is maintained ordered.
 * This particular implementation allows to
 * create a dynamic array for any type (primitive / pointer). */

#ifndef ESERCIZIO1_DYNAMIC_ARRAY_H
#define ESERCIZIO1_DYNAMIC_ARRAY_H

typedef struct _OrderedDynamicArray OrderedDynamicArray;

/* The function creates an empty dynamic array and returns the created dynamic array.
 * It accepts as input the size in bytes of each element stored in the array, this
 * value cannot be changed in a second time. */
OrderedDynamicArray* dynamic_array_create(size_t, int(*)(const void*, const void*));

/* The function accepts as input a pointer to a dynamic array and
 * it returns 1 iff the ordered array is empty (0 otherwise).
 * The input parameter cannot be NULL. */
int dynamic_array_is_empty(OrderedDynamicArray*);

/* The function adds an element to the dynamic array in the first free position.
 * It accepts as input a pointer to a dynamic array and a pointer to the element
 * to be added. The element is copied inside the array.
 * The input parameters cannot be NULL. */
int dynamic_array_add(OrderedDynamicArray*, void*);

/* The function returns a pointer to the n-th element stored in the dynamic array.
 * It accepts as input a pointer to a dynamic array and the index of the element
 * to be retrived.
 * The input parameters cannot be NULL or out of the array bounds. */
void* dynamic_array_get(OrderedDynamicArray*, size_t);

/* The function returns a pointer to the inside array.
 * It accepts as input a pointer to a dynamic array.
 * The input parameter cannot be NULL. */
void* dynamic_array_get_array(OrderedDynamicArray*);

/* The function returns a the number of elements stored in the dynamic array.
 * It accepts as input a pointer to the dynamic array.
 * The input parameter cannot be NULL */
size_t dynamic_array_get_length(OrderedDynamicArray *ordered_dynamic_array);

/* The function returns a the size in bytes of each element stored in the dynamic array.
 * It accepts as input a pointer to the dynamic array.
 * The input parameter cannot be NULL */
size_t dynamic_array_get_size(OrderedDynamicArray*);

/* It accepts as input a pointer to a dynamic array and
 * it frees the memory allocated to store the dynamic array.
 * The input parameters cannot be NULL. */
void dynamic_array_free_memory(OrderedDynamicArray*);

/* It accepts as input a pointer to a dynamic array and a pointer to an element and checks
 * if the element is stored inside the array.
 * The input parameters cannot be NULL. */
int dynamic_array_contains(OrderedDynamicArray*, void*);

#endif /* ESERCIZIO1_DYNAMIC_ARRAY_H */
