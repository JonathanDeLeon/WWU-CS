# CPTR 141: In-Class Checkpoint Repository

Welcome to the Walla Walla University Fundamentals of Programming I git repository for in-class checkpoint assignments.
This repository is designed to be used with the [Cloud 9](http://c9.io/) online development environment.
Below you will find instructions on setting up that environment and keeping it up-to-date.

## Set-Up
Setting up your repository is a two-step process.
You must first fork your own version of the repository on the WWU Computer Science GitLab server and then set up your environment on the Cloud 9 web application.


### Forking the Repository
To set up a fork of this repository, click on the *fork* button on the original repository at <https://gitlab.cs.wallawalla.edu/cptr141/in-class>.
Copy the URL of your forked repository by clicking the clipboard icon next to it.
The URL should look something like: 

```
git@gitlab.cs.wallawalla.edu:YourUsername/in-class.git
```


### Setting Up a Cloud 9 Workspace
Now log into [Cloud 9](https://c9.io/login) and click on *Create a new workspace*.
Name your workspace something you will remember and make sure that it is set to *public*.
Then, in the box labeled *Clone from Git or Mercurial URL*, paste the URL you copied in the previous step.
Click the *Create workspace* button to create your workspace.

Finally, to make sure that you can receive updates easily (see below), type the following commands in the command window at the bottom of the Cloud 9 screen.

```
git remote add upstream git@gitlab.cs.wallawalla.edu:cptr141/in-class.git
```


## Refreshing the Workspace
Each day you should refresh your workspace to reflect the latest version of the in-class checkpoint assignments.
To do this, make sure you are in your ~~/workspace~ directory and run the ~refresh.sh~ script.
Use the following commands in your Cloud 9 terminal window to accomplish this:

```
cd ~/workspace
./refresh.sh
```


## Saving your Workspace
Your workspace will be saved automatically on the Cloud 9 servers in between sessions.
However, in order to submit your work for grading, you will need to upload it to the WWU repository.
To do this, type the following commands:

```
cd ~/workspace
./save.sh
```
