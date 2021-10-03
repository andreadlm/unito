/* Author: Andrea Delmastro
 * The library implements a generic ordered dynamic array.
 * A dynamic array is an array whose size can change dynamically
 * during the execution. The array is maintained ordered.
 * This particular implementation allows to
 * create a dynamic array for any type (primitive / pointer). */

#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include "ordered_dynamic_array.h"

#define INITIAL_CAPACITY 10

void ordered_dynamic_array_m_copy(void* a, void* b, size_t n);
size_t get_index_to_insert(OrderedDynamicArray*, void*);
void insert_element(OrderedDynamicArray*, void*, size_t);
int binary_search(OrderedDynamicArray*, void*, size_t, size_t);

struct _OrderedDynamicArray {
  void* array;
  size_t size;
  size_t capacity;
  size_t length;
  int(*cmp)(const void*, const void*);
};

OrderedDynamicArray* dynamic_array_create(size_t size, int(*cmp)(const void*, const void*))
{
  if(cmp == NULL) {
    fprintf(stderr, "dynamic_array_create: cmp parameter cannot be NULL");
    return NULL;
  }

  OrderedDynamicArray* dynamic_array = (OrderedDynamicArray*)malloc(sizeof(OrderedDynamicArray));
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_create: unable to allocate memory for the dynamic array");
    return NULL;
  }

  dynamic_array->array = malloc(size * INITIAL_CAPACITY);
  if(dynamic_array->array == NULL) {
    fprintf(stderr, "dynamic_array_create: unable to allocate memory for the internal array");
    return NULL;
  }

  dynamic_array->size = size;
  dynamic_array->capacity = INITIAL_CAPACITY;
  dynamic_array->length = 0;
  dynamic_array->cmp = cmp;

  return dynamic_array;
}

int dynamic_array_is_empty(OrderedDynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_is_empty: dynamic_array parameter cannot be NULL");
    return -1;
  }

  return dynamic_array->length == 0;
}

int dynamic_array_add(OrderedDynamicArray* ordered_dynamic_array, void* element)
{
  if(ordered_dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_add: ordered_dynamic_array parameter cannot be NULL");
    return -1;
  }
  if(element == NULL) {
    fprintf(stderr, "dynamic_array_add: element cannot be NULL");
    return -1;
  }

  if(ordered_dynamic_array->length == ordered_dynamic_array->capacity) {
    ordered_dynamic_array->capacity = ordered_dynamic_array->capacity * 2;
    ordered_dynamic_array->array = realloc(ordered_dynamic_array->array, ordered_dynamic_array->capacity * ordered_dynamic_array->size);
    if(ordered_dynamic_array->array == NULL) {
      fprintf(stderr, "dynamic_array_add: unable to reallocate memory to host the new element");
      return -1;
    }
  }

  size_t index = get_index_to_insert(ordered_dynamic_array, element);
  insert_element(ordered_dynamic_array, element, index);
  ordered_dynamic_array->length++;

  return 0;
}

size_t get_index_to_insert(OrderedDynamicArray* ordered_dynamic_array, void* element)
{
  if(ordered_dynamic_array->length == 0) return 0;

  size_t i = ordered_dynamic_array->length - 1;

  while(i > 0) {
    if(ordered_dynamic_array->cmp(element, dynamic_array_get(ordered_dynamic_array, i)) >= 0)
      break;
    i--;
  }

  if(i == 0 && ordered_dynamic_array->cmp(element, dynamic_array_get(ordered_dynamic_array, i)) < 0)
    return 0;
  return i + 1;
}

void insert_element(OrderedDynamicArray* ordered_dynamic_array, void* element, size_t index)
{
  for(size_t i = ordered_dynamic_array->length; i > index; i--)
    ordered_dynamic_array_m_copy((char *) ordered_dynamic_array->array + ordered_dynamic_array->size * (i - 1),
                                 (char *) ordered_dynamic_array->array + ordered_dynamic_array->size * i,
                                 ordered_dynamic_array->size);
  ordered_dynamic_array_m_copy(element,
                               (char *) ordered_dynamic_array->array + ordered_dynamic_array->size * index,
                               ordered_dynamic_array->size);
}

void* dynamic_array_get(OrderedDynamicArray* ordered_dynamic_array, size_t i)
{
  if(ordered_dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get: ordered_dynamic_array parameter cannot be NULL");
    return NULL;
  }
  if(i >= ordered_dynamic_array->length) {
    fprintf(stderr, "dynamic_array_get: index %lu is out of the array bounds", i);
    return NULL;
  }

  return (char*)ordered_dynamic_array->array + ordered_dynamic_array->size * i;
}

void* dynamic_array_get_array(OrderedDynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_array: dynamic_array parameter cannot be NULL");
    return NULL;
  }

  return dynamic_array_get(dynamic_array, 0);
}

size_t dynamic_array_get_length(OrderedDynamicArray* ordered_dynamic_array)
{
  if(ordered_dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_length: ordered_dynamic_array parameter cannot be NULL");
    return (size_t)-1;
  }

  return ordered_dynamic_array->length;
}

size_t dynamic_array_get_size(OrderedDynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_size: dynamic_array parameter cannot be NULL");
    return (size_t)-1;
  }

  return dynamic_array->size;
}

int dynamic_array_contains(OrderedDynamicArray* dynamic_array, void* element)
{
  return binary_search(dynamic_array, element, 0, dynamic_array_get_length(dynamic_array) - 1);
}

int binary_search(OrderedDynamicArray* dynamic_array, void* element, size_t s_idx, size_t e_idx)
{
  if(s_idx > e_idx) return -1;

  size_t c_idx = (s_idx + e_idx) / 2;
  int cmp = dynamic_array->cmp(dynamic_array_get(dynamic_array, c_idx), element);

  if(cmp == 0) return 1;
  if(cmp > 0) {
    if(c_idx == 0) return -1; /* Inferior limit reached, size_t type cannot go below 0 */
    return binary_search(dynamic_array, element, s_idx, c_idx - 1);
  }
  return binary_search(dynamic_array, element, c_idx + 1, e_idx);
}

void dynamic_array_free_memory(OrderedDynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_free_memory: dynamic_array parameter cannot be NULL");
    return;
  }

  free(dynamic_array->array);
  free(dynamic_array);
}

/* Byte-wise copy.
 * The function copies n bytes from a into b. */
void ordered_dynamic_array_m_copy(void* a, void* b, size_t n)
{
  char* a_ = (char*)a;
  char* b_ = (char*)b;

  for(size_t i = 0; i < n; i++)
    *b_++ = *a_++;
}

