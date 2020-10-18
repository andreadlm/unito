#include <stdio.h>
#include <limits.h>

int range_of_even (int * nums , int length , int * min , int * max)
{
    if(nums == NULL || length <= 0) return 0;

    int i, tmp_max = INT_MIN, tmp_min = INT_MAX;
    for(i = 0; i < length; i++)
        if(nums[i] % 2 == 0) {
            if (nums[i] > tmp_max) tmp_max = nums[i];
            if (nums[i] < tmp_min) tmp_min = nums[i];
        }

    if(tmp_max == INT_MIN || tmp_min == INT_MAX) return 0;

    *min = tmp_min;
    *max = tmp_max;
    return 1;
}


