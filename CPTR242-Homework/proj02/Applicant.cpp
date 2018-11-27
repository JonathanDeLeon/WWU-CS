//
// Created by Jonathan De Leon on 5/23/18.
//

#include "Applicant.h"

Applicant::Applicant() {
    studentId = -1;
    fullName = "";
    email = "";
    age = 21;
    numChildren = 0;
    married = false;
}

void Applicant::Initialize(long id, std::string name, std::string emailAddress, int age1) {
    studentId = id;
    fullName = name;
    email = emailAddress;
    age = age1;
    married = false;
    numChildren = 0;
}

int Applicant::GetAge() const {
    return age;
}

void Applicant::SetAge(int age1) {
    age = age1;
}

bool Applicant::HasChildren() const {
    return numChildren > 0;
}

int Applicant::GetChildren() const {
    return numChildren;
}

void Applicant::SetChildren(int children) {
    numChildren = children;
}

bool Applicant::IsMarried() const {
    return married;
}

void Applicant::SetMarried(bool isMarried) {
    married = isMarried;
}

bool Applicant::operator==(Applicant other) const {
    if (studentId != other.studentId) {
        return false;
    } else if (age != other.age) {
        return false;
    } else if (fullName != other.fullName) {
        return false;
    } else if (married != other.married) {
        return false;
    } else if (numChildren != other.numChildren) {
        return false;
    }
    return true;
}

bool operator!=(const Applicant &app1, const Applicant &app2) {
    return !(app1 == app2);
}

std::ostream &operator<<(std::ostream &output, const Applicant &applicant) {
    output << "Student ID " << applicant.studentId << "; " << applicant.fullName << "; Email " << applicant.email << "; "
           << applicant.age << " years old;";
    output << " Married? " << (applicant.IsMarried() ? "Yes;" : "No;");
    output << " Children? " << (applicant.HasChildren() ? "Yes;" : "No;");
    if (applicant.HasChildren()) {
        output << " " << applicant.numChildren << " children";
    }

    return output;
}


