#include <string.h>
#include "person.h"

int cmp_people(const void* p1, const void* p2)
{
  int surname_cmp = strcmp(((Person*)p1)->surname, ((Person*)p2)->surname);
  if(surname_cmp != 0) return surname_cmp;

  int name_cmp = strcmp(((Person*)p1)->name, ((Person*)p2)->name);
  if(name_cmp != 0) return name_cmp;

  return ((Person*)p1)->age - ((Person*)p2)->age;
}
