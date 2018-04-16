/*************************************************************************
 *
 * HW01: Building a basic shape and square class to highlight C++ concepts.
 *      - Polymorphism
 *      - Inheritance
 *      - Exception handling
 * 
 * File Name:  Circle.h
 * Name:       Jonathan De Leon
 * Course:     CPTR 242
 * Date:       April 16, 2018
 * Time Taken: 1 hour
 * 
 */

#ifndef circle_h
#define circle_h

#include <iostream>
#include "Shape.h"

using namespace std;

class Circle : public Shape {
private:
    int radius;
public:
    Circle();

    Circle(int radius);

    int getRadius() const;

    friend ostream &operator<<(ostream &output, const Circle &shape);
};

Circle getCircleFromUser();

#endif // CIRCLE_H