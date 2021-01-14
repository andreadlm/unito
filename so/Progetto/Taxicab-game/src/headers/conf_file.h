#ifndef TAXICAB_GAME_CONF_FILE_H
#define TAXICAB_GAME_CONF_FILE_H

#define CONF_FILE_PATH "conf"

struct conf {
    int so_holes;
    int so_sources;
    int so_cap_min;
    int so_cap_max;
    int so_taxi;
    long so_timesec_min;
    long so_timesec_max;
    int so_timeout;
    int so_duration;
    int so_top_cells;
};

int load_conf(struct conf* self);
#endif /* TATAXICAB_GAME_CONF_FILE_H */
