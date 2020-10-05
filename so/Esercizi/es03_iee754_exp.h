#ifndef ESERCIZI_ES03_IEE754_EXP_H
#define ESERCIZI_ES03_IEE754_EXP_H

int iee754_exp(double* d)
{
    unsigned short s = ((unsigned short*)d)[3];

    s &= 0x7FF0;
    s >>= 0x4;
    s -= 0x3FF;

    return s;
}

#endif //ESERCIZI_ES03_IEE754_EXP_H
