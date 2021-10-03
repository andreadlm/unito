/* Author: Andrea Delmastro
 * The client tries to correct an input text by showing a list of minimum edit-distance words for each
 * word not stored inside the dictionary file. */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "ordered_dynamic_array.h"
#include "edit_distance.h"

#define WORD_MAX_SIZE 50
#define LINE_MAX_SIZE 1024

#define EXIT_SEQUENCE(code) free_mem(dictionary); \
                            exit(code)

int read_dictionary(OrderedDynamicArray*, char*);
int correct_text(OrderedDynamicArray*, char*);
void standardize_word(char*);
OrderedDynamicArray* find_min_edit_distance_words_list(OrderedDynamicArray*, char*);
void free_mem(OrderedDynamicArray*);

int cmp(const void* s1, const void* s2)
{
  char* s1_ = *(char**)s1;
  char* s2_ = *(char**)s2;

  return strcmp(s1_, s2_);
}

/* The main function should be invoke using three arguments:
 * - the dictionary file path
 * - the input file path (text to be corrected)
 * It tries to correct wrong words (words that are not listed in the dictionary) with an alternative list of minimum
 * edit-distance word. The results are then printed on stdout. */
int main(int argc, char* argv[])
{
  OrderedDynamicArray* dictionary = NULL;

  if(argc == 2 && strcmp(argv[1], "--help") == 0) {
    printf("Usage: client <DICTIONARY_FILE_PATH> <INPUT_FILE_PATH>\n"
           "Corrects a text by printing all the minimum edit-distance words from the dictionary."
           "The dictionary must contain a list of words, one word per line.\n");
    EXIT_SEQUENCE(EXIT_SUCCESS);
  } else if(argc != 3) {
    fprintf(stderr, "Usage: client <DICTIONARY_FILE_PATH> <INPUT_FILE_PATH> \n");
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  char* dictionary_file_path = argv[1];
  char* input_file_path = argv[2];

  dictionary = dynamic_array_create(sizeof(char*), cmp);

  if(read_dictionary(dictionary, dictionary_file_path) == -1) {
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  if(correct_text(dictionary, input_file_path) == -1) {
    EXIT_SEQUENCE(EXIT_FAILURE);
  }

  EXIT_SEQUENCE(EXIT_SUCCESS);
}

/* The function takes two arguments:
 * - A pointer to the dynamic array that will be filled with the dictionary words
 * - The path to the file containing the dictionary
 * Il loads all the words stored in the dictionary file into the dynamic array.
 * It returns 0 if the procedure terminates correctly, -1 if an error occurs. */
int read_dictionary(OrderedDynamicArray* dictionary, char* dictionary_file_path)
{
  if(dictionary == NULL) {
    fprintf(stderr, "read_dictionary: dictionary parameter cannot be NULL");
    return -1;
  }
  if(dictionary_file_path == NULL) {
    fprintf(stderr, "read_dictionary: dictionary_file_path parameter cannot be NULL");
    return -1;
  }

  printf("(1) Loading dictionary ...:"); fflush(stdout);

  FILE* dictionary_file = fopen(dictionary_file_path, "r");
  if(dictionary_file == NULL) {
    fprintf(stderr, "client: unable to open %s\n", dictionary_file_path);
    return -1;
  }

  char buff[WORD_MAX_SIZE];
  while(fgets(buff, WORD_MAX_SIZE, dictionary_file) != NULL) {
    if(buff[strlen(buff) - 1] == '\n')
      buff[strlen(buff) - 1] = '\0';
    char* word = (char*)malloc(sizeof(char) * (strlen(buff) + 1));
    strcpy(word, buff);
    if(dynamic_array_add(dictionary, &word)) {
      fprintf(stderr, "client: unable to add a word to the dictionary\n");
      return -1;
    }
  }

  fclose(dictionary_file);

  printf(" finished\n");

  return 0;
}

/* The function takes three arguments:
 * - A pointer to the dynamic array containing the dictionary
 * - The path to the file to be corrected
 * For each word in the input file, it searches the minimum edit-distance list of words in the dictionary
 * and prints the list.
 * Each word is standardized before being searched inside the dictionary. This prevents from marking as wrong
 * words that contain capital letters on punctuation. */
int correct_text(OrderedDynamicArray* dictionary, char* input_file_path)
{
  if(dictionary == NULL) {
    fprintf(stderr, "correct_text: dictionary argument cannot be NULL");
    return -1;
  }
  if(input_file_path == NULL) {
    fprintf(stderr, "correct_text: input_file_path argument cannot be NULL");
    return -1;
  }

  FILE* input_file = fopen(input_file_path, "r");
  if(input_file == NULL) {
    fprintf(stderr, "correct_text: unable to open %s\n", input_file_path);
    return -1;
  }

  printf("(2) List of minimum edit-distance words:\n");

  char buff[LINE_MAX_SIZE];
  while(fgets(buff, LINE_MAX_SIZE, input_file) != NULL) {
    char* word = NULL;
    while((word = strtok(word == NULL ? buff : NULL, " ")) != NULL) {
      standardize_word(word);

      OrderedDynamicArray* min_edit_distance_words_list = find_min_edit_distance_words_list(dictionary, word);

      if(min_edit_distance_words_list == NULL) {
        printf("--------------------\n\x1B[32m%s\x1B[0m:\n", word);
        printf("  . %s\n", word);
      } else {
        printf("--------------------\n\x1B[0;31m%s\x1B[0m:\n", word);
        for(size_t i = 0; i < dynamic_array_get_length(min_edit_distance_words_list); i++)
          printf("  . %s\n", *(char**)dynamic_array_get(min_edit_distance_words_list, i));
        dynamic_array_free_memory(min_edit_distance_words_list);
      }
    }
  }

  fclose(input_file);

  return 0;
}

/* The function takes a word and removes all non-necessary characters from it.
 * Ending characters such as commas, exclamation points and newlines are removed.
 * The first capital letter is transformed in a lowercase one. */
void standardize_word(char* word)
{
  if(word == NULL) {
    fprintf(stderr, "standardize_word: word parameter cannot be NULL\n");
    return;
  }

  char first_char = word[0];
  if(first_char >= 'A' && first_char <= 'Z')
    word[0] = (char)(word[0] + 32);

  int end = 0;
  while(!end)
    switch(word[strlen(word) - 1]) {
      case '.':
      case ',':
      case ';':
      case ':':
      case '?':
      case '!':
      case '\n':
      case '\r':
        word[strlen(word) - 1] = '\0';
        break;
      default:
        end = 1;
    }
}

/* The function takes a pointer to the dictionary and a word and searches for the list of minimum edit-distance
 * words in the dictionary.
 * It return a dynamic array containing the list of words, NULL if the word is stored inside the array;
 * The input parameters cannot be NULL. */
OrderedDynamicArray* find_min_edit_distance_words_list(OrderedDynamicArray* dictionary, char* word)
{
  if(dictionary == NULL) {
    fprintf(stderr, "find_min_edit_distance_words_list: dictionary parameter cannot be NULL\n");
    return NULL;
  }
  if(word == NULL) {
    fprintf(stderr, "find_min_edit_distance_words_list: word parameter cannot be NULL\n");
    return NULL;
  }

  long int min_edit_distance = LONG_MAX;
  OrderedDynamicArray* min_edit_distance_words_list = NULL;

  if(dynamic_array_contains(dictionary, &word) == -1) {
    for (size_t i = 0; i < dynamic_array_get_length(dictionary); i++) {
      if (abs((int)strlen(*(char **) dynamic_array_get(dictionary, i)) - (int)strlen(word)) <= min_edit_distance) {
        long int edit_distance = edit_distance_dyn(word, *(char **) dynamic_array_get(dictionary, i));
        if (edit_distance == -1) {
          fprintf(stderr, "error calculating edit distance");
          dynamic_array_free_memory(min_edit_distance_words_list);
          EXIT_SEQUENCE(EXIT_FAILURE);
        }
        if (edit_distance <= min_edit_distance) {
          if (edit_distance < min_edit_distance) {
            min_edit_distance = edit_distance;
            if (min_edit_distance_words_list != NULL)
              dynamic_array_free_memory(min_edit_distance_words_list);
            min_edit_distance_words_list = dynamic_array_create(sizeof(char *), cmp);
          }
          dynamic_array_add(min_edit_distance_words_list, dynamic_array_get(dictionary, i));
        }
      }
    }
  }

  return min_edit_distance_words_list;
}

/* The function takes a pointer to a dynamic_array and frees the memory allocated. */
void free_mem(OrderedDynamicArray* dynamic_array)
{
  if(dynamic_array != NULL) {
    printf("\n(3) Freeing memory ...:"); fflush(stdout);

    for(size_t i = 0; i < dynamic_array_get_length(dynamic_array); i++)
      free(*(char**)dynamic_array_get(dynamic_array, i));
    dynamic_array_free_memory(dynamic_array);

    printf("     finished\n");
  }
}
