/* Author: Andrea Delmastro
 * The library implements the project-defined structure for
 * a record. */

#include "record.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct _Record {
  int id;
  char* field_1;
  int field_2;
  float field_3;
};

Record* record_create()
{
  return (Record*)malloc(sizeof(Record));
}

Record* record_load_data_from_csv_row(Record* record, char* csv_record)
{
  if(csv_record == NULL) {
    fprintf(stderr, "record_create_from_csv: csv_record parameter cannot be NULL");
    return NULL;
  }

  char* pointer_to_id = strtok(csv_record, ",");
  char* pointer_to_field_1 = strtok(NULL, ",");
  char* pointer_to_field_2 = strtok(NULL, ",");
  char* pointer_to_field_3 = strtok(NULL, ",");

  record->id = (int)strtol(pointer_to_id, NULL, 10);
  record->field_1 = (char*)malloc((strlen(pointer_to_field_1) + 1) * sizeof(char));
  strcpy(record->field_1, pointer_to_field_1);
  record->field_2 = (int)strtol(pointer_to_field_2, NULL, 10);
  record->field_3 = strtof(pointer_to_field_3, NULL);

  return record;
}

int record_get_id(Record* record)
{
  if(record == NULL) {
    fprintf(stderr, "record_get_id: record parameter cannot be NULL");
  }
  return record->id;
}

char* record_get_field_1(Record* record)
{
  if(record == NULL) {
    fprintf(stderr, "record_get_field_1: record parameter cannot be NULL");
  }
  return record->field_1;
}

int record_get_field_2(Record* record)
{
  if(record == NULL) {
    fprintf(stderr, "record_get_field_2: record parameter cannot be NULL");
  }
  return record->field_2;
}

float record_get_field_3(Record* record)
{
  if(record == NULL) {
    fprintf(stderr, "record_get_field_3: record parameter cannot be NULL");
  }
  return record->field_3;
}

void record_free_memory(Record* record)
{
  free(record->field_1);
  free(record);
}

int record_cmp_field_1(const void* p_record_a, const void* p_record_b)
{
  Record* record_a = *(Record**)p_record_a;
  Record* record_b = *(Record**)p_record_b;

  return strcmp(record_a->field_1, record_b->field_1);
}

int record_cmp_field_2(const void* p_record_a, const void* p_record_b)
{
  Record* record_a = *(Record**)p_record_a;
  Record* record_b = *(Record**)p_record_b;

  return record_a->field_2 - record_b->field_2;
}

int record_cmp_field_3(const void* p_record_a, const void* p_record_b)
{
  Record* record_a = *(Record**)p_record_a;
  Record* record_b = *(Record**)p_record_b;

  if(record_a->field_3 - record_b->field_3 < 0.0)
    return -1;
  else
    return 1;
}
