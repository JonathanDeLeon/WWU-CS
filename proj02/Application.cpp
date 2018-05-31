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
    applicationId = 999;
    priority = false;
}

void Application::Initialize(long id, const Applicant &applicant1) {
    applicationId = id;
    applicant = applicant1;
    DoesApplicantHavePriority(applicant, priority);
}

bool Application::operator<(const Application &other) const {
    if (priority == true && other.priority == false) {
        return false;
    } else if (priority == false && other.priority == true) {
        return true;
    } else if (applicationId > other.applicationId) {
        // Smaller ID = application was submitted earlier
        return true;
    }
    return false;
}

bool Application::operator==(const Application &other) const {
    if (priority != other.priority) {
        return false;
    } else if (applicationId != other.applicationId) {
        return false;
    } else if (applicant != other.applicant) {
        return false;
    }
    return true;
}

std::ostream &operator<<(std::ostream &output, const Application &application) {
    output << "Application " << application.applicationId << " : " << application.applicant;
    return output;
}
