CFLAGS      = -O3 -Wall -Wextra -Wpedantic -Wconversion # O3 has been chosen as the best-performing optimization flag
COMMON_DEPS = src/*.h Makefile

BIN_DIR     = bin
BUILD_DIR   = build
SRC_DIR     = src
TEST_DIR    = test

all: $(BIN_DIR)/client $(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test

$(BUILD_DIR)/%.o: $(SRC_DIR)/%.c $(COMMON_DEPS)
	$(CC) $(CFLAGS) -c $< -o $@

$(BIN_DIR)/client: $(BUILD_DIR)/client.o \
                   $(BUILD_DIR)/merge_binary_insertion_sort.o \
                   $(BUILD_DIR)/dynamic_array.o \
                   $(BUILD_DIR)/record.o \
                   $(COMMON_DEPS)
	$(CC) -o $(BIN_DIR)/client \
             $(BUILD_DIR)/client.o \
             $(BUILD_DIR)/merge_binary_insertion_sort.o \
             $(BUILD_DIR)/dynamic_array.o \
             $(BUILD_DIR)/record.o

$(TEST_DIR)/$(BUILD_DIR)/%.o: $(TEST_DIR)/$(SRC_DIR)/%.c $(TEST_DIR)/$(COMMON_DEPS)
	$(CC) $(CFLAGS) -c $< -o $@

$(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test: $(TEST_DIR)/$(BUILD_DIR)/merge_binary_insertion_sort_test.o \
                                                         $(TEST_DIR)/$(BUILD_DIR)/unity.o \
                                                         $(TEST_DIR)/$(BUILD_DIR)/person.o \
                                                         $(BUILD_DIR)/merge_binary_insertion_sort.o \
                                                         $(TEST_DIR)/$(COMMON_DEPS) \
                                                         # merge_binary_insertion_sort.h
	$(CC) -o $(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test \
             $(TEST_DIR)/$(BUILD_DIR)/merge_binary_insertion_sort_test.o \
             $(TEST_DIR)/$(BUILD_DIR)/unity.o \
             $(TEST_DIR)/$(BUILD_DIR)/person.o \
             $(BUILD_DIR)/merge_binary_insertion_sort.o

test: $(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test

run_test: $(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test
	@echo "\n\033[1;33m== MERGE-BINARY-INSERTION-SORT LIBRARY UNIT TESTS ==\033[0m\n"
	@./$(TEST_DIR)/$(BIN_DIR)/merge_binary_insertion_sort_test 2> /dev/null

clean:
	$(RM) $(BUILD_DIR)/*
	$(RM) $(BIN_DIR)/*
	$(RM) $(TEST_DIR)/$(BUILD_DIR)/*
	$(RM) $(TEST_DIR)/$(BIN_DIR)/*

$(shell mkdir -p $(BIN_DIR) $(BUILD_DIR) $(TEST_DIR)/$(BIN_DIR) $(TEST_DIR)/$(BUILD_DIR))
