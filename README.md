# Oberon Syntax Analyzer

This Syntax Analyzer was built in Java for *CPTR354 Compilers and Programming Languages* at Walla Walla University. 
The goal of the project was to design and create a parser for the programming language `Oberon-2`. 
You can [read more about Oberon-2](http://cseweb.ucsd.edu/classes/fa00/cse131a/oberon2.htm).

Coded in Java using Maven as a project manager, the specifications are as follows:
* Run your Oberon grammar file (supplied by us) through yacc/CUP to produce a parser. 
* Interface your parser with your lexer from the last project, producing a program that will accept syntactically legal Oberon programs and reject illegal programs. 
* Add error recovery actions to your parser to handle specific illegal inputs.

A thorough specification sheet for the project requirements [can be found here](http://cseweb.ucsd.edu/classes/fa00/cse131a/parser.htm)

>Note: If you follow the link, the specs are under the tab **Project 2** on the left sidebar

## Setup
As mentioned, the project uses Apache Maven to compile and run the project. 
Here are a list of useful maven commands:

Compile project to byte code and output to the target directory
```
mvn compile
```
Execute main in OberonParser.java with the possibility of adding arguments such as files to be opened.
There are text files that exist in the project `oberon.txt`. If no file is specified, main will use standard input a.k.a `System.in`

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

The following terminal code built the parser using the JavaCC parser-builder
```
javacc -OUTPUT_DIRECTORY=src/main/java/com/jonathan/project/ oberon.jj 
```
