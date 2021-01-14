#include <stdio.h>
#include <math.h>

#include "headers/conf_file.h"
#include "headers/global.h"

/* Funzione di caricamento delle configurazioni dal file conf */
int load_conf(struct conf* self)
{
    FILE* conf_ptr;
    int holes_limit;

    conf_ptr = fopen(CONF_FILE_PATH, "r");
    if(conf_ptr == NULL) return -1;

    if(fscanf(conf_ptr, "so_holes=%d ", &self->so_holes) == EOF) return -1;
    if(fscanf(conf_ptr, "so_sources=%d ", &self->so_sources) == EOF) return -1;
    if(fscanf(conf_ptr, "so_cap_min=%d ", &self->so_cap_min) == EOF) return -1;
    if(fscanf(conf_ptr, "so_cap_max=%d ", &self->so_cap_max) == EOF) return -1;
    if(fscanf(conf_ptr, "so_taxi=%d ", &self->so_taxi) == EOF) return -1;
    if(fscanf(conf_ptr, "so_timesec_min=%ld ", &self->so_timesec_min) == EOF) return -1;
    if(fscanf(conf_ptr, "so_timesec_max=%ld ", &self->so_timesec_max) == EOF) return -1;
    if(fscanf(conf_ptr, "so_timeout=%d ", &self->so_timeout) == EOF) return -1;
    if(fscanf(conf_ptr, "so_duration=%d ", &self->so_duration) == EOF) return -1;
    if(fscanf(conf_ptr, "so_top_cells=%d ", &self->so_top_cells) == EOF) return -1;

    if(self->so_holes < 0 ||
       self->so_sources < 0 ||
       self->so_cap_min < 0 ||
       self->so_cap_max < 0 ||
       self->so_taxi < 0 ||
       self->so_timesec_min < 0 ||
       self->so_timesec_max < 0 ||
       self->so_timeout < 0 ||
       self->so_duration < 0 ||
       self->so_top_cells < 0)
        return -2;

    /* Controllo dei vincoli sul superamento dei limiti mappa */
    /* width/2 (arrotondata per eccesso) * height/2 (arrotondata per eccesso) */
    holes_limit = (int)(ceil(((double)SO_WIDTH / 2)) * ceil(((double)SO_HEIGHT / 2)));
    if(self->so_holes > holes_limit ||
       self->so_top_cells > (SO_WIDTH * SO_HEIGHT) ||
       self->so_sources > (SO_WIDTH * SO_HEIGHT - self->so_holes))
        return -3;

    /* Controllo min < max */
    if(self->so_cap_min > self->so_cap_max ||
       self->so_timesec_min > self->so_timesec_max)
        return -4;

    /* Controllo che ci sia almeno un taxi ed almeno una source */
    if(self->so_sources < 1 ||
       self->so_taxi < 1)
        return -5;

    /* Controllo che la simulazione duri almeno un secondo */
    if(self->so_duration < 1)
        return -6;

    fclose(conf_ptr);
    return 0;
}
