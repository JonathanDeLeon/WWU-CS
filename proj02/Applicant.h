//
// Created by Jonathan De Leon on 5/23/18.
//

#ifndef CPTR242_HOMEWORK_APPLICANT_H
#define CPTR242_HOMEWORK_APPLICANT_H


#include <ostream>

enum RelationType { LESS, GREATER, EQUAL };

class Applicant {
public:
    Applicant();

    RelationType ComparedTo(Applicant) const;

    void Print(std::ostream &) const;

    void Initialize(int number);

private:
    int value;
};

Applicant::Applicant() {
    value = 0;
}



#endif //CPTR242_HOMEWORK_APPLICANT_H
