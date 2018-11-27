# Oberon Lexical Analyzer

This Lexical Analyzer was built in Java for *CPTR354 Compilers and Programming Languages* at Walla Walla University. 
The goal of the project was to design and create a lexer for the programming language `Oberon-2`. 
You can [read more about Oberon-2](http://cseweb.ucsd.edu/classes/fa00/cse131a/oberon2.htm).

Coded in Java using Maven as a project manager, the specifications are as follows:
* Recognize lexemes(patterns) from an input stream and process them as a set of Oberon tokens
* Ignore characters we don't care about
* Report lexical errors 

There are five classe in the project folder: 
#### Sym.java
Contains valid Oberon token codes
#### Token.java
Token class to be returned by `GetToken()`
#### Lexer.java
Bread and butter of the project
#### LexerTester.java
Test program for lexer
#### Oberon.java
Class with a `main()` method that instantiates a Lexer passed on to the LexerTester


A thorough specification sheet for the project requirements [can be found here](http://cseweb.ucsd.edu/classes/fa00/cse131a/)

>Note: If you follow the link, the specs are under the tab **Project 1** on the left sidebar

## Setup
As mentioned, the project uses Apache Maven to compile and run the project. This is all done through the command line. 
Here are a list of useful maven commands:

Compile project to byte code and output to the target directory
```
mvn compile
```
Compile and package project into a jar and output to the target directory
```
mvn package
```
Execute main in Oberon.java with the possibility of adding arguments such as files to be opened.
There are text files that exist in the project `oberon.txt` and `include.txt`. The first file has an `INCLUDE` statement
for the second file.  If no file is specified, main will use standard input a.k.a `System.in`

**Note**: Project must be compiled first
```
mvn exec:java [-Dexec.args="arguments"]
EXAMPLE:
mvn exec:java -Dexec.args="oberon.txt"
```

Remove target directory
```
mvn clean
```
