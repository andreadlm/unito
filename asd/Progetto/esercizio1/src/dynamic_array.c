/* Author: Andrea Delmastro
 * The library implements a generic dynamic array.
 * A dynamic array is an array whose size can change dynamically
 * during the execution. This particular implementation allows to
 * create a dynamic array for any type (primitive / pointer). */

#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include "dynamic_array.h"

#define INITIAL_CAPACITY 10

void dynamic_array_m_copy(void* a, void* b, size_t n);

struct _DynamicArray {
  void* array;
  size_t size;
  size_t capacity;
  size_t length;
};

DynamicArray* dynamic_array_create(size_t size)
{
  DynamicArray* dynamicArray = (DynamicArray*)malloc(sizeof(DynamicArray));
  if(dynamicArray == NULL) {
    fprintf(stderr, "dynamic_array_create: unable to allocate memory for the dynamic array");
    return NULL;
  }

  dynamicArray->array = malloc(size * INITIAL_CAPACITY);
  if(dynamicArray->array == NULL) {
    fprintf(stderr, "dynamic_array_create: unable to allocate memory for the internal array");
    return NULL;
  }

  dynamicArray->size = size;
  dynamicArray->capacity = INITIAL_CAPACITY;
  dynamicArray->length = 0;

  return dynamicArray;
}

int dynamic_array_is_empty(DynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_is_empty: dynamic_array parameter cannot be NULL");
    return -1;
  }

  return dynamic_array->length == 0;
}

int dynamic_array_add(DynamicArray* dynamic_array, void* element)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_add: dynamic_array parameter cannot be NULL");
    return -1;
  }
  if(element == NULL) {
    fprintf(stderr, "dynamic_array_add: element cannot be NULL");
    return -1;
  }

  if(dynamic_array->length == dynamic_array->capacity) {
    dynamic_array->capacity = dynamic_array->capacity * 2;
    dynamic_array->array = realloc(dynamic_array->array, dynamic_array->capacity * dynamic_array->size);
    if(dynamic_array->array == NULL) {
      fprintf(stderr, "dynamic_array_add: unable to reallocate memory to host the new element");
      return -1;
    }
  }

  dynamic_array_m_copy(element, (char*)dynamic_array->array + dynamic_array->size * dynamic_array->length, dynamic_array->size);
  dynamic_array->length++;

  return 0;
}

void* dynamic_array_get(DynamicArray* dynamic_array, size_t i)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get: dynamic_array parameter cannot be NULL");
    return NULL;
  }
  if(i >= dynamic_array->length) {
    fprintf(stderr, "dynamic_array_get: index %lu is out of the array bounds", i);
    return NULL;
  }

  return (char*)dynamic_array->array + dynamic_array->size * i;
}

void* dynamic_array_get_array(DynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_array: dynamic_array parameter cannot be NULL");
    return NULL;
  }

  return dynamic_array_get(dynamic_array, 0);
}

size_t dynamic_array_get_length(DynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_length: dynamic_array parameter cannot be NULL");
    return (size_t)-1;
  }

  return dynamic_array->length;
}

size_t dynamic_array_get_size(DynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_get_size: dynamic_array parameter cannot be NULL");
    return (size_t)-1;
  }

  return dynamic_array->size;
}

void dynamic_array_free_memory(DynamicArray* dynamic_array)
{
  if(dynamic_array == NULL) {
    fprintf(stderr, "dynamic_array_free_memory: dynamic_array parameter cannot be NULL");
    return;
  }

  free(dynamic_array->array);
  free(dynamic_array);
}

/* Byte-wise copy.
 * The function copies n bytes from a to b. */
void dynamic_array_m_copy(void* a, void* b, size_t n)
{
  char* a_ = (char*)a;
  char* b_ = (char*)b;

  for(size_t i = 0; i < n; i++)
    *b_++ = *a_++;
}

