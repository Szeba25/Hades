#include <stdio.h>
#include "user.h"

int main() {
	int a, b;
	read_a(&a);
	read_b(&b);
	swap(&a, &b);
	printf("%d\n", a);
	printf("%d\n", b);
	return 0;
}