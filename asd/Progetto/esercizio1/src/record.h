/* Author: Andrea Delmastro
 * The library implements the project-defined structure for
 * a record. */

#ifndef ESERCIZIO1_RECORD_H
#define ESERCIZIO1_RECORD_H

#include <stddef.h>

typedef struct _Record Record;

/* The function allocates a new record.
 * Ite returns a pointer to the new record. */
Record* record_create();

/* The function loads a record with datas from a csv row.
 * The csv separator must be ',' and the csv fields must respect
 * the defined record structure.
 * It accepts as parameters a pointer to the record and a string
 * containing the csv row that has to be loaded inside the record.
 * The input paramaters cannot be NULL. */
Record* record_load_data_from_csv_row(Record*, char*);

/* The function returns the id field of the record.
 * It accepts a pointer to the record.
 * The input parameter cannot be NULL. */
int record_get_id(Record*);

/* The function returns the first field of the record.
 * If the record hasn't been initialized yet, NULL is returned.
 * It accepts a pointer to the record.
 * The input parameter cannot be NULL. */
char* record_get_field_1(Record *record);

/* The function returns the second field of the record.
 * It accepts a pointer to the record.
 * The input parameter cannot be NULL. */
int record_get_field_2(Record*);

/* The function returns the thirs filed of the record.
 * It accepts a pointer to the record.
 * The input parameter cannot be NULL. */
float record_get_field_3(Record*);

/* The function frees the memory needed to allocate the record.
 * It accepts a pointer to the record.
 * The input parameter cannot be NULL. */
void record_free_memory(Record*);

/* The functions compares the first field of two records.
 * It accepts as parameters two pointers to the two records.
 * It returns 1 if the first record is greater than the second, -1 if the second
 * record is greater than the first, 0 if they are euqal. */
int record_cmp_field_1(const void*, const void*);

/* The functions compares the second field of two records.
 * It accepts as parameters two pointers to the two records.
 * It returns 1 if the first record is greater than the second, -1 if the second
 * record is greater than the first, 0 if they are euqal. */
int record_cmp_field_2(const void*, const void*);

/* The functions compares the thirs field of two records.
 * It accepts as parameters two pointers to the two records.
 * It returns 1 if the first record is greater than the second, -1 if the second
 * record is greater than the first, 0 if they are euqal. */
int record_cmp_field_3(const void*, const void*);

#endif /* SERCIZIO1_RECORD_H */
