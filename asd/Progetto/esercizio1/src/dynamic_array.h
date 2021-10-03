/* Author: Andrea Delmastro
 * The library implements a generic dynamic array.
 * A dynamic array is an array whose size can change dynamically
 * during the execution. This particular implementation allows to
 * create a dynamic array for any type (primitive / pointer). */

#ifndef ESERCIZIO1_DYNAMIC_ARRAY_H
#define ESERCIZIO1_DYNAMIC_ARRAY_H

typedef struct _DynamicArray DynamicArray;

/* The function creates an empty dynamic array and returns the created dynamic array.
 * It accepts as input the size in bytes of each element stored in the array, this
 * value cannot be changed in a second time. */
DynamicArray* dynamic_array_create(size_t);

/* The function accepts as input a pointer to a dynamic array and
 * it returns 1 iff the ordered array is empty (0 otherwise).
 * The input parameter cannot be NULL. */
int dynamic_array_is_empty(DynamicArray*);

/* The function adds an element to the dynamic array in the first free position.
 * It accepts as input a pointer to a dynamic array and a pointer to the element
 * to be added. The element is copied inside the array.
 * The input parameters cannot be NULL. */
int dynamic_array_add(DynamicArray*, void*);

/* The function returns a pointer to the n-th element stored in the dynamic array.
 * It accepts as input a pointer to a dynamic array and the index of the element
 * to be retrieved.
 * The input parameters cannot be NULL or out of the array bounds. */
void* dynamic_array_get(DynamicArray*, size_t);

/* The function returns a pointer to the inside array.
 * It accepts as input a pointer to a dynamic array.
 * The input parameter cannot be NULL. */
void* dynamic_array_get_array(DynamicArray*);

/* The function returns a the number of elements stored in the dynamic array.
 * It accepts as input a pointer to the dynamic array.
 * The input parameter cannot be NULL */
size_t dynamic_array_get_length(DynamicArray*);

/* The function returns a the size in bytes of each element stored in the dynamic array.
 * It accepts as input a pointer to the dynamic array.
 * The input parameter cannot be NULL */
size_t dynamic_array_get_size(DynamicArray*);

/* It accepts as input a pointer to a dynamic array and
 * it frees the memory allocated to store the dynamic array.
 * The input parameters cannot be NULL. */
void dynamic_array_free_memory(DynamicArray*);

#endif /* ESERCIZIO1_DYNAMIC_ARRAY_H */
