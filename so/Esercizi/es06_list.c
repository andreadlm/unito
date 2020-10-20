#include <stdio.h>
#include <stdlib.h>

#include "list.h"

list list_insert_head(list p, int val)
{
	list new_el;

	/* Allocate the new node */
	new_el = malloc(sizeof(*new_el));
	/* Setting the data */
	new_el->value = val;
	/* head insertion: old head becomes .next field of new head */
	new_el->next = p;
	/* new head is the pointer to the new node */
	return new_el;
}

void list_print(list p)
{
	/* Looping all nodes until p == NULL */
	if (p == NULL) {
		printf("Empty list\n");
		return;
	}
	printf("[%i]", p->value);
	for(; p->next!=NULL; p = p->next) {
		printf(" -> [%i]", p->next->value);
	}
	printf("\n");
}

void list_free(list p)
{
	/* If p ==  NULL, nothing to deallocate */
	if (p == NULL) {
		return;
	}
	/* First deallocate (recursively) the next nodes... */
	list_free(p->next);
	/* ... then deallocate the node itself */
	free(p);
}

list list_insert_ordered(list p, int val)
{
    list new_elem = (list)malloc(sizeof(*new_elem));
    new_elem->value = val;
    new_elem->next = NULL;

    if(p == NULL) return new_elem;

    list pNode = NULL;
    list cNode = p;
    for(; cNode != NULL && new_elem->value > cNode->value; pNode = cNode, cNode = cNode->next) {}

    new_elem->next = cNode;
    if(pNode) {
        pNode->next = new_elem;
        return p;
    }
    /* caso primo elemento */
    else return new_elem;
}

list list_insert_ordered_rec(list p, int val)
{
    if(p != NULL && val > p->value) {
        p->next = list_insert_ordered(p->next, val);
        return p;
    } else {
        list new_elem = (list)malloc(sizeof(*new_elem));
        new_elem->value = val;
        new_elem->next = p;
        return new_elem;
    }
}

list list_cat(list before, list after)
{
    if(before == NULL) return after;

    list tmp = before;
    for(; tmp->next != NULL; tmp = tmp->next) {}

    tmp->next = after;

    return before;
}

list list_cat_rec(list before, list after)
{
    if(before == NULL) return after;
    else {
        before->next = list_cat_rec(before->next, after);
        return before;
    }
}

list list_insert_tail(list p, int val)
{
    list new_elem = (list)malloc(sizeof(*new_elem));
    new_elem->value = val;
    new_elem->next = NULL;

    if(p == NULL) return new_elem;

    list tmp = p;
    for(; tmp->next != NULL; tmp = tmp->next) {}
    tmp->next = new_elem;

    return p;
}

list list_insert_tail_rec(list p, int val) {
    if(p == NULL) {
        list new_elem = (list)malloc(sizeof(*new_elem));
        new_elem->value = val;
        new_elem->next = NULL;

        return new_elem;
    } else {
        p->next = list_insert_tail_rec(p->next, val);
        return p;
    }
}