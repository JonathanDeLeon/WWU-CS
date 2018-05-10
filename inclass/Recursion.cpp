//
// Created by Jonathan De Leon on 5/7/18.
//
#include <iostream>
#include <cassert>

using namespace std;

void WriteUp(int n) {
    if (n > 0) {
        WriteUp(n - 1);
    }
    cout << n << " ";
}

void WriteDown(int n) {
    cout << n << " ";
    if (n > 0) {
        WriteDown(n - 1);
    }
}

int Factorial(int n) {
    if (n == 0) {
        return 1;
    }
    return n * Factorial(n - 1);
}

int Fibonacci(int n) {
    if (n == 0) {
        return 0;
    } else if (n == 1) {
        return 1;
    }
    return Fibonacci(n - 1) + Fibonacci(n - 2);
}

int main() {
    // Test Base cases
    WriteUp(1);
    cout << endl;
    WriteDown(1);
    cout << endl;

    assert(Factorial(0) == 1);
    cout << endl;

    assert(Fibonacci(0) == 0);
    assert(Fibonacci(1) == 1);

    // Test General Case
    WriteUp(10);
    cout << endl;
    WriteDown(10);
    cout << endl;
}

