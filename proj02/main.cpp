//
// Created by Jonathan De Leon on 5/30/18.
//


#include <iostream>
#include "PQType.h"
#include "Application.h"

using namespace std;

PQType<Application> apps(99);
long nextApplicationId = 0;

/*
 * Helper function to copy the passed in queue value and print the applications in the queue
 */
void PrintQueue(PQType<Application> queue) {
    if (queue.IsEmpty()) {
        cout << "There are currently no applications in the list." << endl;
        return;
    }

    Application current;
    while (!queue.IsEmpty()) {
        queue.Dequeue(current);
        cout << current << endl;
    }
}

/*
 * Helper function to turn an input character (y/n) to boolean
 */
bool inputBooleanHelper() {
    char boolean;
    while (true) {
        cin >> boolean;
        switch (boolean) {
            case 'y':
                return true;
            case 'n':
                return false;
            default:
                cout << "Invalid input. Enter y or n" << endl;
        }
    }
}

/*
 * Helper function to correctly read a string from input
 * Blank lines are omitted
 */
string inputStringHelper() {
    string str;
    getline(cin, str);
    while (str.length() == 0 ) {
        getline(cin, str);
    }
    return str;
}

/*
 * Function to walk client through to add an application to the queue
 */
void addApplication() {
    long studentId;
    string name;
    string email;
    int age;
    bool boolean = false;

    // Get Applicant's Student ID
    cout << "Enter Applicant's WWU Student Id (i.e. 123456): " << endl;
    cin >> studentId;
    // Get Applicant's name
    cout << "Enter Applicant's full name: " << endl;
    name = inputStringHelper();
    // Get Applicant's email
    cout << "Enter Applicant's email address: " << endl;
    email = inputStringHelper();
    // Get Applicant's age
    cout << "Enter Applicant's age: " << endl;
    cin >> age;

    Applicant applicant;
    applicant.Initialize(studentId, name, email, age);

    // Check if Applicant is married
    cout << "Is the Applicant married? [y/n]: " << endl;
    boolean = inputBooleanHelper();
    applicant.SetMarried(boolean);

    // Check if Applicant has any children, if so, how many?
    cout << "Does the Applicant have any children? [y/n]: " << endl;
    boolean = inputBooleanHelper();
    if (boolean) {
        int children;
        cout << "How many children does the Applicant have?: " << endl;
        cin >> children;
        applicant.SetChildren(children);
    }

    // Print Applicant
    cout << "Applicant created...Showing Applicant..." << endl << endl;
    cout << applicant << endl << endl;

    // Verify Applicant information and add to the queue, if correct
    cout << "Is the above info about the Applicant correct? [y/n]: " << endl;
    boolean = inputBooleanHelper();
    if (boolean) {
        Application application;
        long applicationId = ++nextApplicationId;
        application.Initialize(applicationId, applicant);
        apps.Enqueue(application);
    } else {
        cout << "Please enter the applicant's info again..." << endl;
    }
    return;
}

/*
 * Function to grant housing to the application with the highest priority
 */
void grantApplication() {
    try {
        Application application;
        apps.Dequeue(application);
        cout << "Housing was granted to the following application: " << endl;
        cout << application << endl;
    } catch (EmptyPQ ex) {
        cout << "There are currently no applications in the list." << endl;
    }
}

/*
 * Main method
 */
int main() {
    cout << "Welcome to the Village Housing Application!" << endl;
    int in;
    while (true) {
        cout << endl << "[1] Apply \n[2] Grant Housing to the Next Applicant \n[3] Show Waiting List \n[4] Exit"
             << endl;
        cin >> in;
        switch (in) {
            case 1:
                addApplication();
                break;
            case 2:
                grantApplication();
                break;
            case 3:
                cout << endl;
                PrintQueue(apps);
                break;
            case 4:
                return 0;
            default:
                cout << "Input not recognized. Try one of the numeric menu options [1-4]." << endl;
                break;
        }
    }
}
