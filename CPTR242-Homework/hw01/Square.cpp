//
// Created by Jonathan De Leon on 4/16/18.
//

#include <iostream>
#include "Square.h"
#include "NegativeLengthException.h"

using namespace std;

Square::Square() : Shape("Square") {
    this->sideLength = 0;
}

Square::Square(int sideLength) : Shape("Square") {
    if (sideLength < 0) {
        throw NegativeLengthException();
    }
    this->sideLength = sideLength;
}

int Square::getSideLength() const {
    return sideLength;
}

ostream &operator<<(ostream &output, const Square &shape) {
    output << shape.getName() << "(" << shape.getSideLength() << ")";
    return output;
}
