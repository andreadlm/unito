#include "unity.h"
#include "../../src/edit_distance.h"

void setUp (void) {}
void tearDown (void) {}

static void test_edit_distance_different_strings()
{
  TEST_ASSERT_EQUAL_INT64(3, edit_distance("Onusto", "Angusto"));
}

static void test_edit_distance_dyn_different_strings()
{
  TEST_ASSERT_EQUAL_INT64(4, edit_distance_dyn("Pernicioso", "Pericoloso"));
}

static void test_edit_distance_same_string()
{
  TEST_ASSERT_EQUAL_INT64(0, edit_distance("Sicofante", "Sicofante"));
}

static void test_edit_distance_dyn_same_string()
{
  TEST_ASSERT_EQUAL_INT64(0, edit_distance_dyn("Granciporro", "Granciporro"));
}

static void test_edit_distance_zero_len_string()
{
  TEST_ASSERT_EQUAL_INT64(12, edit_distance("Lapalissiano", ""));
}

static void test_edit_distance_dyn_zero_len_string()
{
  TEST_ASSERT_EQUAL_INT64(11, edit_distance_dyn("Erubiscente", ""));
}

static void test_edit_distance_zero_len_both_strings()
{
  TEST_ASSERT_EQUAL_INT64(0, edit_distance("", ""));
}

static void test_edit_distance_dyn_zero_len_both_strings()
{
  TEST_ASSERT_EQUAL_INT64(0, edit_distance_dyn("", ""));
}

static void test_edit_distance_null_string()
{
  TEST_ASSERT_EQUAL_INT64(-1, edit_distance(NULL, "Solipsista"));
}

static void test_edit_distance_dyn_null_string()
{
  TEST_ASSERT_EQUAL_INT64(-1, edit_distance_dyn(NULL, "Zuzzurellone"));
}

int main()
{
  UNITY_BEGIN();
  RUN_TEST(test_edit_distance_different_strings);
  RUN_TEST(test_edit_distance_dyn_different_strings);
  RUN_TEST(test_edit_distance_same_string);
  RUN_TEST(test_edit_distance_dyn_same_string);
  RUN_TEST(test_edit_distance_zero_len_string);
  RUN_TEST(test_edit_distance_dyn_zero_len_string);
  RUN_TEST(test_edit_distance_zero_len_both_strings);
  RUN_TEST(test_edit_distance_dyn_zero_len_both_strings);
  RUN_TEST(test_edit_distance_null_string);
  RUN_TEST(test_edit_distance_dyn_null_string);
  return UNITY_END();
}



