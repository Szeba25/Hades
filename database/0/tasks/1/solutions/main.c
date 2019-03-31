#include <stdio.h>

int main() {
	int n, i, v;
	scanf("%d", &n);
	for (i = 0; i < n; i++) {
		scanf("%d", &v);
		printf("%d\n", v*2);
	}
	return 0;
}
