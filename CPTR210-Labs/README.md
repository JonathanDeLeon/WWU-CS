# CPTR 210: Labs Repository

Welcome to the Walla Walla University Data Communication and Networks git repository for lab assignments.
You may used this repository with any development tool your familiar.
The repository works well with the [Cloud 9](http://c9.io/) online development environment.
Below you will find instructions on setting up your environment and keeping it up-to-date.

## Set-Up
Setting up your repository is a two-step process.
You must first fork your own version of the repository on the WWU Computer Science GitLab server and then set up your environment on the Cloud 9 web application.


### Forking the Repository
To set up a fork of this repository, click on the *fork* button on the original repository at <https://gitlab.cs.wallawalla.edu/cptr210/labs>.
Copy the URL of your forked repository by clicking the clipboard icon next to it.
The URL should look something like: 

```
git@gitlab.cs.wallawalla.edu:YourUsername/labs.git
```


### Setting Up Your Git Workspace
Pick your favorite Git tool to create clone the lab assignment project and set the remote upstream project.

__In Linux (command line)__

In the directory you would like to keep your lab assignments, run the following command.
```
git clone git@gitlab.cs.wallawalla.edu:YourUsername/labs.git
```

Finally, to make sure that you can receive updates easily (see below), type the following commands.

```
git remote add upstream git@gitlab.cs.wallawalla.edu:cptr210/labs.git
```


## Refreshing Your Lab Assignments
Each week you should refresh your workspace to reflect the latest version of the lab assignments.

__In Linux (command line)__

The following command will update your workspace with the lastest from the CS Lab Gitlab servers.
```
./refresh.sh
```


## Saving Your Lab Assignments
In order to submit your work for grading, you will need to upload it to the WWU repository.
Please save your work to your git repository and push the changes to CS Lab Gitlab servers.

__In Linux (command line)__

The following command will save your work to CS Lab Gitlab servers.
```
./commit_and_push.sh
```
