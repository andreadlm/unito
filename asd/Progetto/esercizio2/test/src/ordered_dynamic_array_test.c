#include "unity.h"
#include "../../src/ordered_dynamic_array.h"

int cmp_int(const void* a, const void* b)
{
  int* a_ = (int*)a;
  int* b_ = (int*)b;

  return *a_ - *b_;
}

void setUp (void) {}
void tearDown (void) {}

static void test_ordered_dynamic_array_is_empty()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  TEST_ASSERT_EQUAL_INT(1, dynamic_array_is_empty(arr));
}

static void test_ordered_dynamic_array_add()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n = 10;
  dynamic_array_add(arr, &n);
  TEST_ASSERT_EQUAL_INT(n, *(int*)dynamic_array_get(arr, 0));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_add_unordered()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n3);
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 0));
  TEST_ASSERT_EQUAL_INT(n2, *(int*)dynamic_array_get(arr, 1));
  TEST_ASSERT_EQUAL_INT(n3, *(int*)dynamic_array_get(arr, 2));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_add_reverse_ordered()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n3);
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n1);
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 0));
  TEST_ASSERT_EQUAL_INT(n2, *(int*)dynamic_array_get(arr, 1));
  TEST_ASSERT_EQUAL_INT(n3, *(int*)dynamic_array_get(arr, 2));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_add_correct_ordered()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n3);
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 0));
  TEST_ASSERT_EQUAL_INT(n2, *(int*)dynamic_array_get(arr, 1));
  TEST_ASSERT_EQUAL_INT(n3, *(int*)dynamic_array_get(arr, 2));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_add_same_element()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n1);
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 0));
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 1));
  TEST_ASSERT_EQUAL_INT(n1, *(int*)dynamic_array_get(arr, 2));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_get_length()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n1);
  TEST_ASSERT_EQUAL_INT(4, dynamic_array_get_length(arr));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_get_size()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(long), cmp_int);
  TEST_ASSERT_EQUAL_INT64(sizeof(long), dynamic_array_get_size(arr));
  dynamic_array_free_memory(arr);
}

static void test_ordered_dynamic_array_contains_true()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n3);
  TEST_ASSERT_EQUAL_INT(1, dynamic_array_contains(arr, &n2));
}

static void test_ordered_dynamic_array_contains_true_first_element()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n3);
  TEST_ASSERT_EQUAL_INT(1, dynamic_array_contains(arr, &n1));
}

static void test_ordered_dynamic_array_contains_true_last_element()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n2);
  dynamic_array_add(arr, &n3);
  TEST_ASSERT_EQUAL_INT(1, dynamic_array_contains(arr, &n3));
}

static void test_ordered_dynamic_array_contains_false()
{
  OrderedDynamicArray* arr = dynamic_array_create(sizeof(int), cmp_int);
  int n1 = 10;
  int n2 = 20;
  int n3 = 30;
  dynamic_array_add(arr, &n1);
  dynamic_array_add(arr, &n2);
  TEST_ASSERT_EQUAL_INT(-1, dynamic_array_contains(arr, &n3));
}


int main()
{
  UNITY_BEGIN();
  RUN_TEST(test_ordered_dynamic_array_is_empty);
  RUN_TEST(test_ordered_dynamic_array_add);
  RUN_TEST(test_ordered_dynamic_array_add_unordered);
  RUN_TEST(test_ordered_dynamic_array_add_reverse_ordered);
  RUN_TEST(test_ordered_dynamic_array_add_correct_ordered);
  RUN_TEST(test_ordered_dynamic_array_add_same_element);
  RUN_TEST(test_ordered_dynamic_array_get_length);
  RUN_TEST(test_ordered_dynamic_array_get_size);
  RUN_TEST(test_ordered_dynamic_array_contains_true);
  RUN_TEST(test_ordered_dynamic_array_contains_false);
  RUN_TEST(test_ordered_dynamic_array_contains_true_first_element);
  RUN_TEST(test_ordered_dynamic_array_contains_true_last_element);
  return UNITY_END();
}

