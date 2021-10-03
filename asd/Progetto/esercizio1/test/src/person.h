#ifndef ESERCIZIO1_PERSON_H
#define ESERCIZIO1_PERSON_H

typedef struct {
  char* name;
  char* surname;
  int age;
} Person;

int cmp_people(const void*, const void*);
void print_person(Person);

#endif //ESERCIZIO1_PERSON_H
