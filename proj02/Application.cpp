//
// Created by Jonathan De Leon on 5/28/18.
//

#include "Application.h"

void DoesApplicantHavePriority(const Applicant &applicant, bool &priority) {
    if (applicant.GetAge() >= 25) {
        priority = true;
    } else if (applicant.IsMarried()) {
        priority = true;
    } else if (applicant.HasChildren()) {
        priority = true;
    } else {
        priority = false;
    }
}

Application::Application() {
    priority = false;
}

void Application::Initialize(const Applicant &applicant1) {
    applicant = applicant1;
    DoesApplicantHavePriority(applicant, priority);
    timestamp = std::time(nullptr);
}

bool Application::operator<(const Application &other) const {
    if (priority == false && other.priority == true) {
        return true;
    } else if (timestamp > other.timestamp) {
        // Larger timestamp, lower priority
        return true;
    }
    return false;
}
bool Application::operator>(const Application &other) const {
    if (priority == true && other.priority == false) {
        return true;
    } else if (timestamp < other.timestamp) {
        // Smaller timestamp, higher priority
        return true;
    }
    return false;
}
bool Application::operator==(const Application &other) const {
    if (priority != other.priority) {
        return false;
    } else if (timestamp != other.timestamp) {
        return false;
    } else if (applicant != other.applicant) {
        return false;
    }
    return true;
}

std::ostream &operator<<(std::ostream &output, const Application &application) {
    output << application.applicant << " Created on " << application.timestamp;
    return output;
}
