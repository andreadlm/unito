#ifndef TAXICAB_GAME_GLOBAL_H
#define TAXICAB_GAME_GLOBAL_H

#define SHM_KEY 0xad0
#define SEM_START_KEY 0xad1
#define SEMS_ACCESS_KEY 0xad2
#define SEMS_COUNTER_ID 0xad3
#define MSG_REQUESTS_KEY 0xad4
#define MSG_REPORTS_KEY 0xad5

#define ANSI_COLOR_RED     "\x1b[31m"
#define ANSI_COLOR_GREEN   "\x1b[32m"
#define ANSI_COLOR_YELLOW  "\x1b[33m"
#define ANSI_COLOR_BLUE    "\x1b[34m"
#define ANSI_COLOR_MAGENTA "\x1b[35m"
#define ANSI_COLOR_CYAN    "\x1b[36m"
#define ANSI_COLOR_RESET   "\x1b[0m"

#ifndef SO_HEIGHT
#define SO_HEIGHT 10
#endif
#ifndef SO_WIDTH
#define SO_WIDTH 10
#endif
#endif /* TAXICAB_GAME_GLOBAL_H */
