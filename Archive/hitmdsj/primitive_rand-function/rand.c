/* The main function, without input argument validation */
#include <stdio.h>
#include <stdlib.h>

#define ABS(X) (((X) > 0) ? (X) : -(X))

static int rand_num = 30;

int myrand(void) {
    rand_num = ABS((rand_num * 16807) % 2147483647);
    return rand_num;
}

double frand(void) {
	//return double: (0 <= d < 1)
	double result;
	// make always smaller than 100
	result = (double) myrand() / (2147483648.0);
	//printf("called frand: %f\n", result);
    return result;
}

int main (int argc, char *argv []) {
	int i;
	double num;
	for(i=0; i < 100000;i++) {
        num = frand();
        printf("%f\n", num);           
    }
}

