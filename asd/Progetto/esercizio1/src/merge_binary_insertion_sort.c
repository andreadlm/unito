/* Author: Andrea Delmastro
 * The library implements a generic merge-binary-insertion-sort function. */

#include <stdlib.h>
#include <stdio.h>
#include "merge_binary_insertion_sort.h"

/* Discontinues merge-sort when partition gets below this size.
 * A binary-insertion-sort logic is used for particularly small arrays.
 * This magic number was chosen to work best on an Intel 8250U CPU on Ubuntu-WSL. */
#define MAX_THRESH 16

void merge_binary_insertion_m_copy(void *a, void *b, size_t n);
size_t binary_search(void*, void*, size_t, size_t, size_t, int (*)(const void*, const void*));
void binary_insertion_sort(void*, size_t, size_t, size_t, int (*)(const void*, const void*));
void merge(void*, size_t, size_t, size_t, size_t, int (*)(const void*, const void*));
void merge_binary_insertion_sort_(void*, size_t, size_t, size_t, int (*)(const void*, const void*), size_t);
void merge_binary_insertion_sort_param(void*, size_t, size_t, int (*)(const void*, const void*), size_t);

void merge_binary_insertion_sort(void* array, size_t length, size_t elem_size, int (*cmp)(const void*, const void*))
{
  merge_binary_insertion_sort_param(array, length, elem_size, cmp, MAX_THRESH);
}

/* Same as merge_binary_insertion_sort, but allows to define the limit where merge-sort is discontinued
 * in favor of binary-insertion-sort. */
void merge_binary_insertion_sort_param(void* array, size_t length, size_t elem_size, int (*cmp)(const void*, const void*),
                                       size_t max_thresh)
{
  if(array == NULL) {
    fprintf(stderr, "merge_binary_insertion_sort: array parameter cannot be NULL");
    return;
  }
  if(length <= 0) {
    fprintf(stderr, "merge_binary_insertion_sort: length parameter cannot be below 1");
    return;
  }
  if(elem_size <= 0) {
    fprintf(stderr, "merge_binary_insertion_sort: elem_size parameter cannot be below 1");
    return;
  }
  if(cmp == NULL) {
    fprintf(stderr, "merge_binary_insertion_sort: cmp parameter cannot be NULL");
    return;
  }

  merge_binary_insertion_sort_(array, 0, length - 1, elem_size, cmp, max_thresh);
}

/* More logic-friendly definition of the merge-binary-insertion-sort function.
 * The function takes the staring and ending indexes of the subarray considered instead of the length. */
void merge_binary_insertion_sort_(void* array, size_t s_idx, size_t e_idx, size_t elem_size, int (*cmp)(const void*, const void*),
                                  size_t max_thresh)
{
  if(s_idx < e_idx) {
    size_t c_idx = (s_idx + e_idx) / 2;

    if(c_idx - s_idx + 1 > max_thresh)
      merge_binary_insertion_sort_(array, s_idx, c_idx, elem_size, cmp, max_thresh);
    else /* left-subarray size under the merge-sort limit */
      binary_insertion_sort(array, s_idx, c_idx, elem_size, cmp);

    if(e_idx - c_idx > max_thresh)
      merge_binary_insertion_sort_(array, c_idx + 1, e_idx, elem_size, cmp, max_thresh);
    else /* right-subarray size under the merge-sort limit */
      binary_insertion_sort(array, c_idx + 1, e_idx, elem_size, cmp);

    merge(array, s_idx, c_idx, e_idx, elem_size, cmp);
  }
}

/* Merges two adjacent ordered subarrays in one. */
void merge(void* array, size_t s_idx, size_t c_idx, size_t e_idx, size_t size, int (*cmp_func)(const void*, const void*))
{
  size_t i = 0, j = 0;
  int cmp;

  size_t l_tmp_array_length = c_idx - s_idx + 1;
  char* l_tmp_array = (char*)malloc(l_tmp_array_length * size);
  merge_binary_insertion_m_copy((char *) array + s_idx * size, l_tmp_array, l_tmp_array_length * size);

  size_t r_tmp_array_length = e_idx - c_idx;
  char* r_tmp_array = (char*)malloc(r_tmp_array_length * size);
  merge_binary_insertion_m_copy((char *) array + (c_idx + 1) * size, r_tmp_array, r_tmp_array_length * size);

  for(size_t k = s_idx; k <= e_idx; k++) {
    if(i >= l_tmp_array_length) {
      merge_binary_insertion_m_copy(r_tmp_array + j * size, (char *) array + k * size, size);
      j++;
    } else if(j >= r_tmp_array_length) {
      merge_binary_insertion_m_copy(l_tmp_array + i * size, (char *) array + k * size, size);
      i++;
    } else {
      cmp = (*cmp_func)(l_tmp_array + i * size, r_tmp_array + j * size);
      if(cmp >= 0) {
        merge_binary_insertion_m_copy(r_tmp_array + j * size, (char *) array + k * size, size);
        j++;
      } else {
        merge_binary_insertion_m_copy(l_tmp_array + i * size, (char *) array + k * size, size);
        i++;
      }
    }
  }

  free(l_tmp_array);
  free(r_tmp_array);
}

/* The function implements a binary-insertion-sort logic. It works the same as an insertion sort, but the position at
 * which the element should be placed is found using a binary search to reduce the amount of comparisons needed. */
void binary_insertion_sort(void* array, size_t s_idx, size_t e_idx, size_t elem_size, int (*cmp)(const void*, const void*))
{
  size_t i;
  void* key = malloc(elem_size);

  for(size_t j = s_idx + 1; j <= e_idx; j++) {
    size_t position = binary_search(array, (char*)array + j * elem_size, s_idx, j - 1, elem_size, cmp);
    if(position != j) {
      merge_binary_insertion_m_copy((char *) array + j * elem_size, key, elem_size);

      for(i = j - 1; i >= position; i--) {
        merge_binary_insertion_m_copy((char *) array + i * elem_size, (char *) array + (i + 1) * elem_size, elem_size);
        if(i == 0) break; /* Inferior limit reached, site_z type cannot go below 0 */
      }

      merge_binary_insertion_m_copy(key, (char *) array + position * elem_size, elem_size);
    }
  }

  free(key);
}

/* Binary search inside a subarray starting from s_idx and ending at e_idx.
 * The function looks for the position at which the element elem should be inserted inside the array. */
size_t binary_search(void* array, void* elem, size_t s_idx, size_t e_idx, size_t elem_size, int (*cmp_func)(const void*,
                     const void*))
{
  if(s_idx > e_idx) return s_idx;

  size_t c_idx = (s_idx + e_idx) / 2;
  int cmp = (*cmp_func)((char*)array + c_idx * elem_size, elem);

  if(cmp == 0) return c_idx;
  if(cmp > 0) {
    if(c_idx == 0) return c_idx; /* Inferior limit reached, size_t type cannot go below 0 */
    return binary_search(array, elem, s_idx, c_idx - 1, elem_size, cmp_func);
  }
  return binary_search(array, elem, c_idx + 1, e_idx, elem_size, cmp_func);
}

/* Byte-wise merge_binary_insertion_m_copy.
 * The function copies a into b for n bytes. */
void merge_binary_insertion_m_copy(void* a, void* b, size_t n)
{
  char* a_ = (char*)a;
  char* b_ = (char*)b;

  for(size_t i = 0; i < n; i++)
    *b_++ = *a_++;
}
