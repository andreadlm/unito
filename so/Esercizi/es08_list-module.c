#include "list.h"
#include <stdlib.h>

list list_delete_if(list head , int to_delete)
{
    if(head == NULL) return head;

#ifdef RECURSIVE_LIST_METHODS
    if(head->value == to_delete) {
        list ret = head->next;
        free(head);
        return ret;
    }
    else {
        head->next = list_delete_if(head->next, to_delete);
        return head;
    }
#else
    if(head->value == to_delete) {
        list ret = head->next;
        free(head);
        return ret;
    }

    list curr_node;
    for(curr_node = head; curr_node != NULL && curr_node->next != NULL; curr_node = curr_node->next)
        if(curr_node->next->value == to_delete) {
            curr_node->next = curr_node->next->next;
            free(curr_node->next);
        }

    return head;
#endif
}

list list_delete_odd(list head)
{
    if(head == NULL) return head;

#ifdef RECURSIVE_LIST_METHODS
    else {
        list ret = head->next;
        if(head->next != NULL) head->next->next = list_delete_odd(head->next->next);
        free(head);
        return ret;
    }
#else
    list curr_node = head->next;
    free(head);
    head = curr_node;

    for(; curr_node != NULL && curr_node->next != NULL; curr_node = curr_node->next) {
        list tmp = curr_node->next->next;
        free(curr_node->next);
        curr_node->next = tmp;
    }

    return head;
#endif
}

list list_cut_below(list head, int cut_value) {
    if (head == NULL) return head;

#ifdef RECURSIVE_LIST_METHODS
    if(head->value < cut_value) {
        list tmp = head->next;
        free(head);
        return list_cut_below(tmp, cut_value);
    } else {
        head->next = list_cut_below(head->next, cut_value);
        return head;
    }
#else
    list curr_node = head;
    while(curr_node->next != NULL) {
        if(curr_node == head && head->value < cut_value) {
            curr_node = head->next;
            free(head);
            head = curr_node;
        } else {
            if (curr_node->next->value < cut_value) {
                list tmp = curr_node->next;
                curr_node->next = curr_node->next->next;
                free(tmp);
            }
            curr_node = curr_node->next;
        }
    }

    return head;
#endif
}

list list_dup(list head)
{
    if(head == NULL) return head;

#ifdef RECURSIVE_LIST_METHODS
    list cp = (list)malloc(sizeof(*cp));
    cp->value = head->value;
    cp->next = list_dup(head->next);
    return cp;
#else
    list tmp, p, n, ret;

    p = (list)malloc(sizeof(*p));
    p->value = head->value;

    ret = p;

    for(tmp = head->next; tmp != NULL; tmp = tmp->next, p = n) {
        n = (list)malloc(sizeof(*n));
        n->value = tmp->value;

        p->next = n;
    }

    return ret;
#endif
}