/* Author: Andrea Delmastro
 * The client allows to order a list of pre-defined records stored inside a csv file.
 * Each record contains three fields:
 * - field_1 (string)
 * - filed_2 (int)
 * - field_3 (float)
 * The client allows to specify the field you want to sort. */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include "merge_binary_insertion_sort.h"
#include "record.h"
#include "dynamic_array.h"

#define EXIT_SEQUENCE(code) free_mem(dynamic_array); \
                            exit(code)
#define BUFFER_SIZE 1024

void merge_binary_insertion_sort_param(void*, size_t, size_t, int (*cmp)(const void*, const void*), size_t);

void read_csv(DynamicArray*, char*);
void sort(DynamicArray*, int, size_t);
void test_param(DynamicArray*);
void print_csv(DynamicArray*, char*);
void free_mem(DynamicArray*);

int main(int argc, char* argv[])
{
  DynamicArray* dynamic_array = NULL;

  if(argc == 2 && strcmp(argv[1], "--help") == 0) {
    printf("Usage: client <INPUT_FILE_PATH> <OUTPUT_FILE_PATH> <FIELD_TO_ORDER> <MAX_THRESH_PARAM>\n"
           "Order all the records on the <FIELD_TO_ORDER>th field in <INPUT_FILE_PATH> and prints the result in <OUTPUT_FILE_PATH>\n\n"
           "The record structure must be in accordance with the project specifications:\n"
           "\t- id (int)\n"
           "\t- field_1 (string)\n"
           "\t- field_2 (int)\n"
           "\t- field_3 (float)\n"
           "each field must be separated by a ','\n");
    EXIT_SEQUENCE(EXIT_SUCCESS);
  } else if(argc != 5) {
    fprintf(stderr, "Usage: client <INPUT_FILE_PATH> <OUTPUT_FILE_PATH> <FIELD_TO_ORDER> <MAX_THRESH_PARAM>\n");
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  char* input_file_path = argv[1];
  char* output_file_path = argv[2];
  int field_to_order = (int)strtol(argv[3], NULL, 10);
  size_t max_thresh = (size_t)strtol(argv[4], NULL, 10);
  if(field_to_order < 1 || field_to_order > 3) {
    fprintf(stderr, "client: not valid column number, column number must be a value between 1 and 3 (included)\n");
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  dynamic_array = dynamic_array_create(sizeof(Record*));

  read_csv(dynamic_array, input_file_path);

  sort(dynamic_array, field_to_order, max_thresh);

  print_csv(dynamic_array, output_file_path);

  EXIT_SEQUENCE(EXIT_SUCCESS);
}

/* Reads the csv file containing all the records and stores it inside a dynamic array */
void read_csv(DynamicArray* dynamic_array, char* input_file_path)
{
  printf("(1) Loading data ...:"); fflush(stdout);

  FILE* input_file = fopen(input_file_path, "r");
  if(input_file == NULL) {
    fprintf(stderr, "client: unable to open %s\n", input_file_path);
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  char buff[BUFFER_SIZE];

  while(fgets(buff, BUFFER_SIZE, input_file) != NULL) {
    Record* record = record_create();
    record_load_data_from_csv_row(record, buff);
    if(record == NULL) exit(EXIT_FAILURE);
    if(dynamic_array_add(dynamic_array, &record)) exit(EXIT_FAILURE);
  }

  fclose(input_file);

  printf("   finished\n");
}

/* Sorts the dynamic array on the specified field */
void sort(DynamicArray* dynamic_array, int field_to_order, size_t max_thresh)
{
  int(*cmp_func)(const void*, const void*);
  switch(field_to_order) {
    case 1:
      cmp_func = record_cmp_field_1;
      break;
    case 2:
      cmp_func = record_cmp_field_2;
      break;
    case 3:
      cmp_func = record_cmp_field_3;
      break;
    default:
      fprintf(stderr, "sort: not valid field number\n");
      return;
  }

  printf("(2) Sorting ...:"); fflush(stdout);

  clock_t sorting_clock_begin = clock();
  merge_binary_insertion_sort_param(dynamic_array_get_array(dynamic_array),
                                    dynamic_array_get_length(dynamic_array),
                                    sizeof(Record*),
                                    cmp_func,
                                    max_thresh);
  clock_t sorting_clock_end = clock();
  double sorting_time = (double)(sorting_clock_end - sorting_clock_begin) / CLOCKS_PER_SEC;

  printf("        finished in %fs\n", sorting_time);
}

/* Prints the dynamic array to the output file. */
void print_csv(DynamicArray* dynamic_array, char* output_file_path)
{
  printf("(3) Printing data ...:"); fflush(stdout);

  FILE* output_file = fopen(output_file_path, "w");
  if(output_file == NULL) {
    fprintf(stderr, "client: unable to open %s\n", output_file_path);
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  size_t array_length = dynamic_array_get_length(dynamic_array);
  for(size_t i = 0; i < array_length; i++) {
    Record* record = *(Record**)dynamic_array_get(dynamic_array, i);
    fprintf(output_file, "%d,%s,%d,%f\n", record_get_id(record),
                                        record_get_field_1(record),
                                        record_get_field_2(record),
                                        record_get_field_3(record));
  }

  fclose(output_file);

  printf("  finished\n");
}

/* Frees the memory allocated earlier. */
void free_mem(DynamicArray* dynamic_array)
{
  if(dynamic_array != NULL) {
    printf("(4) Freeing memory ...:"); fflush(stdout);

    for (size_t i = 0; i < dynamic_array_get_length(dynamic_array); i++)
      record_free_memory(*(Record **) dynamic_array_get(dynamic_array, i));
    dynamic_array_free_memory(dynamic_array);

    printf(" finished\n");
  }
}

/* Test function, it tests all the parameters between 0 and 50 and prints the results in a csv format. */
void test_param(DynamicArray* dynamic_array)
{
  FILE* results = fopen("../results.csv", "w");
  fprintf(results, "param,field,time\n");

  printf("(2) Sorting ...:"); fflush(stdout);

  for(size_t max_thresh = 0; max_thresh <= 50; max_thresh++) {
    for(int field = 1; field < 4; field++) {

      int(*cmp_func)(const void*, const void*);
      switch(field) {
        case 1:
          cmp_func = record_cmp_field_1;
          break;
        case 2:
          cmp_func = record_cmp_field_2;
          break;
        case 3:
          cmp_func = record_cmp_field_3;
          break;
        default:
          fprintf(stderr, "sort: not valid filed number\n");
          return;
      }

      void *cp_array = malloc(dynamic_array_get_length(dynamic_array) * dynamic_array_get_size(dynamic_array));
      memcpy(cp_array, dynamic_array_get_array(dynamic_array),
             dynamic_array_get_length(dynamic_array) * dynamic_array_get_size(dynamic_array));

      clock_t sorting_clock_begin = clock();
      merge_binary_insertion_sort_param(cp_array,
                                        dynamic_array_get_length(dynamic_array),
                                        sizeof(Record *),
                                        cmp_func,
                                        max_thresh);
      clock_t sorting_clock_end = clock();
      double sorting_time = (double) (sorting_clock_end - sorting_clock_begin) / CLOCKS_PER_SEC;

      free(cp_array);

      fprintf(results, "%ld,%d,%lf\n", max_thresh, field, sorting_time); fflush(results);
    }
  }

  fclose(results);

  printf("        finished\n");
}
