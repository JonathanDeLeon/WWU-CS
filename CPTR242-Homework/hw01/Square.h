/*************************************************************************
 *
 * HW01: Building a basic shape and square class to highlight C++ concepts.
 *      - Polymorphism
 *      - Inheritance
 *      - Exception handling
 * 
 * File Name:  Square.h
 * Name:       Jonathan De Leon
 * Course:     CPTR 242
 * Date:       April 16, 2018
 * Time Taken: 1 hour
 * 
 */

#ifndef square_h
#define square_h

#include <iostream>
#include "Shape.h"

using namespace std;

class Square : public Shape {
private:
    int sideLength;
public:
    Square();

    Square(int sideLength);

    int getSideLength() const;

    friend ostream &operator<<(ostream &output, const Square &shape);
};

#endif // SQUARE_H