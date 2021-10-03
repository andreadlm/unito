CFLAGS      = -O2 -Wall -Wextra -Wpedantic -Wconversion
COMMON_DEPS = src/*.h Makefile

BIN_DIR     = bin
BUILD_DIR   = build
SRC_DIR     = src
TEST_DIR    = test

all: $(BIN_DIR)/client \
     $(TEST_DIR)/$(BIN_DIR)/edit_distance_test \
     $(TEST_DIR)/$(BIN_DIR)/ordered_dynamic_array_test

$(BUILD_DIR)/%.o: $(SRC_DIR)/%.c $(COMMON_DEPS)
	$(CC) $(CFLAGS) -c $< -o $@

$(BIN_DIR)/client: $(BUILD_DIR)/client.o \
                   $(BUILD_DIR)/edit_distance.o \
                   $(BUILD_DIR)/ordered_dynamic_array.o
	$(CC) -o $(BIN_DIR)/client \
             $(BUILD_DIR)/client.o \
             $(BUILD_DIR)/ordered_dynamic_array.o \
			 $(BUILD_DIR)/edit_distance.o

$(TEST_DIR)/$(BUILD_DIR)/%.o: $(TEST_DIR)/$(SRC_DIR)/%.c $(TEST_DIR)/$(COMMON_DEPS)
	$(CC) $(CFLAGS) -c $< -o $@

$(TEST_DIR)/$(BIN_DIR)/edit_distance_test: $(TEST_DIR)/$(BUILD_DIR)/edit_distance_test.o \
                                           $(TEST_DIR)/$(BUILD_DIR)/unity.o \
                                           $(BUILD_DIR)/edit_distance.o \
                                           $(TEST_DIR)/$(COMMON_DEPS)
	$(CC) -o $(TEST_DIR)/$(BIN_DIR)/edit_distance_test \
			 $(TEST_DIR)/$(BUILD_DIR)/edit_distance_test.o \
             $(TEST_DIR)/$(BUILD_DIR)/unity.o \
             $(BUILD_DIR)/edit_distance.o

$(TEST_DIR)/$(BIN_DIR)/ordered_dynamic_array_test: $(TEST_DIR)/$(BUILD_DIR)/ordered_dynamic_array_test.o \
                                           $(TEST_DIR)/$(BUILD_DIR)/unity.o \
                                           $(BUILD_DIR)/ordered_dynamic_array.o \
                                           $(TEST_DIR)/$(COMMON_DEPS)
	$(CC) -o $(TEST_DIR)/$(BIN_DIR)/ordered_dynamic_array_test \
			 $(TEST_DIR)/$(BUILD_DIR)/ordered_dynamic_array_test.o \
             $(TEST_DIR)/$(BUILD_DIR)/unity.o \
             $(BUILD_DIR)/ordered_dynamic_array.o


run_test: $(TEST_DIR)/$(BIN_DIR)/edit_distance_test $(TEST_DIR)/$(BIN_DIR)/ordered_dynamic_array_test
	@echo "\n\033[1;33m== EDIT-DISTANCE LIBRARY UNIT TESTS ==\033[0m\n"
	@./$(TEST_DIR)/$(BIN_DIR)/edit_distance_test 2> /dev/null
	@echo "\n\033[1;33m== ORDERED-DYNAMIC-ARRAY LIBRARY UNIT TESTS ==\033[0m\n"
	@./$(TEST_DIR)/$(BIN_DIR)/ordered_dynamic_array_test 2> /dev/null


clean:
	$(RM) $(BUILD_DIR)/*
	$(RM) $(BIN_DIR)/*
	$(RM) $(TEST_DIR)/$(BUILD_DIR)/*
	$(RM) $(TEST_DIR)/$(BIN_DIR)/*

$(shell mkdir -p $(BIN_DIR) $(BUILD_DIR) $(TEST_DIR)/$(BIN_DIR) $(TEST_DIR)/$(BUILD_DIR))
