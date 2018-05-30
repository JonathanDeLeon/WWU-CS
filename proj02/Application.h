//
// Created by Jonathan De Leon on 5/28/18.
//


#include <iostream>
#include "Applicant.h"

class Application {
public:
    Application();

    void Initialize(const Applicant &);

    bool operator<(const Application &other) const;

    bool operator>(const Application &other) const;

    bool operator==(const Application &other) const;

    friend bool operator!=(const Application& x, const Application& y) { return !(x==y); }

    friend bool operator<=(const Application& x, const Application& y) { return !(x>y); }

    friend bool operator>=(const Application& x, const Application& y) { return !(x<y); }

    friend std::ostream &operator<<(std::ostream &output, const Application &application);

private:
    Applicant applicant;
    bool priority;
    std::time_t timestamp;
};

void DoesApplicantHavePriority(const Applicant &, bool &priority);
