//
// Created by Jonathan De Leon on 4/16/18.
//

#include <iostream>
#include "Circle.h"
#include "NegativeLengthException.h"

using namespace std;

Circle::Circle() : Shape("Circle") {
    this->radius = 0;
}

Circle::Circle(int radius) : Shape("Circle") {
    if (radius < 0) {
        throw NegativeLengthException();
    }
    this->radius = radius;
}

int Circle::getRadius() const {
    return radius;
}

ostream &operator<<(ostream &output, const Circle &shape) {
    output << shape.getName() << "(" << shape.getRadius() << ")";
    return output;
}

Circle getCircleFromUser() {
    int radius;
    cout << "Enter the size of the circle: ";
    cin >> radius;
    Circle circle;
    try {
        circle = Circle(radius);
    } catch (NegativeLengthException &e) {
        cerr << "Input Error: " << e.what() << endl;
        circle = getCircleFromUser();
    }
    return circle;
}