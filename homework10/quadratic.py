#!/usr/bin/env python
# Jonathan De Leon
# CPTR 454 Design & Analysis of Algorithms
# HW 10: 11.4.8
# March, 18 2018
#
# Assignment:
# Write a computer program to solve the equation ax^2 + bx + c = 0 using basic math operations.


def newtons_method(discriminant):
    """ Solves square roots 1/2 * (x_n + D / x_n) """
    approx = 0.5 + 0.5 * discriminant   # base case; x_0
    while True:
        sqrt = 0.5 * approx + 0.5 * (discriminant / approx)
        if sqrt == approx or sqrt == 0:
            break
        approx = sqrt
    return sqrt

def quadratic_equation(a, b, c):
    """
    Solves quadratic equations both real and complex
    complex roots represented as a string
    """
    discriminant = (b * b) - 4 * a * c
    if discriminant < 0:
        discriminant = discriminant * -1
        root = newtons_method(discriminant)
        real = (-b) / (2 * a)
        complex = root / (2 * a)
        x1 = str(real) + ' + ' + str(complex) + 'j'
        x2 = str(real) + ' - ' + str(complex) + 'j'
    else:
        root = newtons_method(discriminant)
        x1 = (-b) / (2 * a) + root / (2 * a)
        x2 = (-b) / (2 * a) - root / (2 * a)
    return [x1, x2]

if __name__ == "__main__":
    print("Welcome!\n")
    print("To solve the quadratic equation `ax^2 + bx + c = 0`; please input coefficients a, b, c...\n")
    try:
        a = float(input("Enter a: "))
        b = float(input("Enter b: "))
        c = float(input("Enter c: "))
        result = quadratic_equation(a, b, c)
        print('\nSolution:')
        print 'x = ', (result[0], result[1])
    except Exception as e:
        print('Unexpected error:', str(e))
