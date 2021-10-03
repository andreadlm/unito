#include "unity.h"
#include "../../src/merge_binary_insertion_sort.h"
#include "merge_binary_insertion_sort_test.h"
#include "person.h"

void setUp (void) {} /* Is run before every test, put unit init calls here. */
void tearDown (void) {} /* Is run after every test, put unit clean-up calls here. */

int cmp_int(const void* a, const void* b)
{
  int* a_ = (int*)a;
  int* b_ = (int*)b;

  return *a_ - *b_;
}

static void test_copy_person()
{
  Person p1 = {"Andrea", "Delmastro", 20};
  Person p2 = {"Federico", "Bianchi", 19};
  merge_binary_insertion_m_copy(&p1, &p2, sizeof(Person));
  TEST_ASSERT_EQUAL_STRING(p1.name, p2.name);
  TEST_ASSERT_EQUAL_STRING(p1.surname, p2.surname);
  TEST_ASSERT_EQUAL_INT(p1.age, p2.age);
}

static void test_copy_int_array()
{
  int numbers1[] = {1, 3, 4, 5};
  int numbers2[] = {2, 5, 6, 7};
  merge_binary_insertion_m_copy(numbers1, numbers2, sizeof(numbers1));
  TEST_ASSERT_EQUAL_INT_ARRAY(numbers1, numbers2, sizeof(numbers1)/sizeof(int));
}

static void test_binary_search_int_array()
{
  int numbers[] = {2, 4, 6, 8, 9, 10, 14, 25, 11};
  size_t pos = binary_search(numbers, numbers + 8, 0, sizeof(numbers)/sizeof(int) - 1, sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT64(6, pos);
}

static void test_binary_search_already_in_int_array()
{
  int numbers[] = {2, 4, 6, 8, 9, 10, 14, 25, 4};
  size_t pos = binary_search(numbers, numbers + 8, 0, sizeof(numbers)/sizeof(int) - 1, sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT64(1, pos);
}

static void test_binary_search_first_position_already_in_int_array()
{
  int numbers[] = {2, 4, 6, 8, 9, 10, 14, 25, 2};
  size_t pos = binary_search(numbers, numbers + 8, 0, sizeof(numbers)/sizeof(int) - 1, sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT64(0, pos);
}

static void test_binary_search_first_position_int_array()
{
  int numbers[] = {2, 4, 6, 8, 9, 10, 14, 25, 1};
  size_t pos = binary_search(numbers, numbers + 8, 0, sizeof(numbers)/sizeof(int) - 1, sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT64(0, pos);
}

static void test_binary_search_last_position_int_array()
{
  int numbers[] = {2, 4, 6, 8, 9, 10, 14, 22, 29};
  size_t pos = binary_search(numbers, numbers + 8, 0, sizeof(numbers)/sizeof(int) - 1, sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT64(8, pos);
}

static void test_binary_search_people_array()
{
  Person people[] = {{"Fabio",      "Bertaina",  21},
                     {"Alessandro", "Delmastro", 24},
                     {"Andrea",     "Delmastro", 20},
                     {"Andrea",     "Delmastro", 21},
                     {"Fabrizio",   "Delmastro", 56},
                     {"Giacomo",    "Femorali",  19},
                     {"Mattia",     "Giordano",  19},
                     {"Luca",       "Kroj",      20},
                     {"Paul",       "Ndiaye",    19},
                     {"Nadia",      "Carletto",  51}};
  size_t pos = binary_search(people, people + 9, 0, sizeof(people)/sizeof(Person) - 1, sizeof(Person), cmp_people);
  TEST_ASSERT_EQUAL_INT64(1, pos);
}

static void test_merge_binary_insertion_sort_empty_array()
{
  void* array = NULL;
  merge_binary_insertion_sort(array, 0, 0, NULL);
  TEST_ASSERT_NULL(array);
}

static void test_merge_binary_insertion_sort_one_element_int_array()
{
  int number[] = {42};
  merge_binary_insertion_sort(number, sizeof(number)/sizeof(int), sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT_ARRAY(number, number, sizeof(number)/sizeof(int));
}

static void test_merge_binary_insertion_sort_two_elements_int_array()
{
  int numbers[] = {42, 24};
  int sorted_numbers[] = {24, 42};
  merge_binary_insertion_sort(numbers, sizeof(numbers)/sizeof(int), sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT_ARRAY(sorted_numbers, numbers, sizeof(sorted_numbers)/sizeof(int));
}

static void test_merge_binary_insertion_sort_unordered_int_array()
{
  int numbers_1[] = {4, 5, 3, 7, 4, 3, 6, 8, 7, 2, 1, 2, 3, 3, 2, 7, 6, 5, 8, 9, 4, 2, 1, 1};
  int sorted_numbers_1[] = {1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 8, 8, 9};
  int numbers_2[] = {6, 7, -1, 10, 4, 23, 22, 23, 87, 2, 3, -19, 23, 4, 2, 67, 32, 22};
  int sorted_numbers_2[] = {-19, -1, 2, 2, 3, 4, 4, 6, 7, 10, 22, 22, 23, 23, 23, 32, 67, 87};
  merge_binary_insertion_sort(numbers_1, sizeof(numbers_1)/sizeof(int), sizeof(int), cmp_int);
  merge_binary_insertion_sort(numbers_2, sizeof(numbers_2)/sizeof(int), sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT_ARRAY(sorted_numbers_1, numbers_1, sizeof(numbers_1)/sizeof(int));
  TEST_ASSERT_EQUAL_INT_ARRAY(sorted_numbers_2, numbers_2, sizeof(numbers_2)/sizeof(int));
}

static void test_merge_binary_insertion_sort_ordered_int_array()
{
  int numbers[] = {2, 4, 6, 7, 10, 14, 15, 19, 22, 23, 25, 28, 30, 32};
  merge_binary_insertion_sort(numbers, sizeof(numbers)/sizeof(int), sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT_ARRAY(numbers, numbers, sizeof(numbers)/sizeof(int));
}

static void test_merge_binary_insertion_sort_reverse_order_array()
{
  int numbers[] = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4};
  int sorted_numbers[] = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  merge_binary_insertion_sort(numbers, sizeof(numbers)/sizeof(int), sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT_ARRAY(sorted_numbers, numbers, sizeof(numbers)/sizeof(int));
}

static void test_merge_binary_insertion_sort_unorderd_people_array()
{
  Person people[] = {{"Andrea",     "Delmastro", 20},
                     {"Alessandro", "Delmastro", 24},
                     {"Fabrizio",   "Delmastro", 56},
                     {"Mattia",     "Giordano",  19},
                     {"Fabio",      "Bertaina",  21},
                     {"Luca",       "Kroj",      20},
                     {"Paul",       "Ndiaye",    19},
                     {"Giacomo",    "Femorali",  19},
                     {"Andrea",     "Delmastro", 21}};
  Person sorted_people[] = {{"Fabio",      "Bertaina",  21},
                            {"Alessandro", "Delmastro", 24},
                            {"Andrea",     "Delmastro", 20},
                            {"Andrea",     "Delmastro", 21},
                            {"Fabrizio",   "Delmastro", 56},
                            {"Giacomo",    "Femorali",  19},
                            {"Mattia",     "Giordano",  19},
                            {"Luca",       "Kroj",      20},
                            {"Paul",       "Ndiaye",    19}};
  merge_binary_insertion_sort(people, sizeof(people)/sizeof(Person), sizeof(Person), cmp_people);
  for(size_t i = 0; i < sizeof(sorted_people)/sizeof(Person); i++) {
    TEST_ASSERT_EQUAL_STRING(sorted_people[i].surname, people[i].surname);
    TEST_ASSERT_EQUAL_STRING(sorted_people[i].name, people[i].name);
    TEST_ASSERT_EQUAL_INT(sorted_people[i].age, people[i].age);
  }
}

int main()
{
  UNITY_BEGIN();
  RUN_TEST(test_copy_person);
  RUN_TEST(test_copy_int_array);
  RUN_TEST(test_binary_search_int_array);
  RUN_TEST(test_binary_search_already_in_int_array);
  RUN_TEST(test_binary_search_first_position_int_array);
  RUN_TEST(test_binary_search_last_position_int_array);
  RUN_TEST(test_binary_search_first_position_already_in_int_array);
  RUN_TEST(test_binary_search_people_array);
  RUN_TEST(test_merge_binary_insertion_sort_empty_array);
  RUN_TEST(test_merge_binary_insertion_sort_one_element_int_array);
  RUN_TEST(test_merge_binary_insertion_sort_two_elements_int_array);
  RUN_TEST(test_merge_binary_insertion_sort_unordered_int_array);
  RUN_TEST(test_merge_binary_insertion_sort_ordered_int_array);
  RUN_TEST(test_merge_binary_insertion_sort_reverse_order_array);
  RUN_TEST(test_merge_binary_insertion_sort_unorderd_people_array);
  return UNITY_END();
}
