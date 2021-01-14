# Flags di compilazione
CFLAGS = -std=c89 -pedantic -Wall -Wextra

# Percorsi dei file
BUDIR = build
BIDIR = $(BUDIR)/bin
ODIR = $(BUDIR)/obj
SDIR = src
IDIR = $(SDIR)/headers

# Librerie
LIBS = -lm

# Costanti da definire a tempo di compilazione
_DEF = SO_WIDTH=20 SO_HEIGHT=30
# _DEF += ENHANCED_PRINT # Scommentare se si vuole rimuovere la stampa formattata
# _DEF += DEBUG # Scommentare per abilitare le stampe di debug
DEF = $(patsubst %, -D%, $(_DEF))

_DEPS_MAIN = cell.h global.h msg_report.h cell.h msg_request.h process_info_list.h sem.h shm.h conf_file.h
DEPS_MAIN = $(patsubst %, $(IDIR)/%, $(_DEPS_MAIN))

_DEPS_SOURCE = msg_request.h global.h sem.h
DEPS_SOURCE = $(patsubst %, $(IDIR)/%, $(_DEPS_SOURCE))

_DEPS_TAXI = cell.h global.h msg_report.h msg_request.h sem.h shm.h conf_file.h
DEPS_TAXI = $(patsubst %, $(IDIR)/%, $(_DEPS_TAXI))

_OBJ_MAIN = main.o cell.o sem.o shm.o msg_report.o msg_request.o processes_info_list.o conf_file.o
OBJ_MAIN = $(patsubst %, $(ODIR)/%, $(_OBJ_MAIN))

_OBJ_SOURCE = source.o msg_request.o sem.o
OBJ_SOURCE = $(patsubst %, $(ODIR)/%, $(_OBJ_SOURCE))

_OBJ_TAXI = taxi.o cell.o sem.o shm.o msg_report.o msg_request.o conf_file.o
OBJ_TAXI = $(patsubst %, $(ODIR)/%, $(_OBJ_TAXI))

$(ODIR)/%.o: $(SDIR)/%.c Makefile
	$(CC) -c -o $@ $< $(CFLAGS) $(DEF)

$(BIDIR)/main: $(OBJ_MAIN)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

$(BIDIR)/source: $(OBJ_SOURCE)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

$(BIDIR)/taxi: $(OBJ_TAXI)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

all: $(BIDIR)/main $(BIDIR)/source $(BIDIR)/taxi

run: all
	./$(BIDIR)/main

clean:
	rm -f $(ODIR)/* $(BIDIR)/*
