//
// Created by Jonathan De Leon on 5/23/18.
//

#include <iostream>

class Applicant {
public:
    Applicant();

    void Initialize(long id, std::string name, std::string emailAddress, int age1);

    int GetAge() const;

    void SetAge(int age1);

    bool HasChildren() const;

    int GetChildren() const;

    void SetChildren(int children);

    bool IsMarried() const;

    void SetMarried(bool isMarried);

    bool operator==(Applicant other) const;

    friend bool operator!=(const Applicant &app1, const Applicant &app2);

    friend std::ostream &operator<<(std::ostream &output, const Applicant &applicant);

private:
    long studentId;
    std::string fullName;
    std::string email;
    int age;
    int numChildren;
    bool married;
};
