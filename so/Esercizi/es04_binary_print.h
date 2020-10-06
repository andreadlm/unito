#ifndef ESERCIZI_ES04_BINARY_PRINT_H
#define ESERCIZI_ES04_BINARY_PRINT_H

void binary_print(unsigned int n)
{
    unsigned int bit_num = sizeof(n) * 8;
    unsigned int mask = 1;
    for(int i = 0; i < bit_num - 1; i++) mask <<= 1u; //0x800000000 se 32bit

    // v1
    for(int i = 0; i < bit_num; mask >>= 1u, i++)
        printf("%d ", (n & mask) >> (bit_num - i - 1));

    // v2
    for(int i = 0; i < bit_num; mask >>= 1u, i++)
        printf("%d ", (n & mask) == 0 ? 0 : 1);
}

#endif //ESERCIZI_ES04_BINARY_PRINT_H
