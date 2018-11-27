package com.jonathan.project;


import java.io.FileNotFoundException;

/**
 * Oberon class has a main method that instantiates a Lexer instance then passes
 * the object to the LexerTester instance to run the program
 * The main method accepts files as arguments to be used by the Lexer OR
 * the default oberon.txt in the project folder if it exists OR
 * standard input stream
 */
public class Oberon {
    public static void main(String[] args){
        Lexer lexer;
        if(args.length > 0){
            //Read file(s) from command line
            for(int i = 0; i < args.length; i++){
                try {
                    lexer = new Lexer(args[i]);
                    LexerTester lexerTester = new LexerTester(lexer);
                    lexerTester.Run();
                } catch (FileNotFoundException e) {
                    System.out.println("Error, '"+args[i]+"', line 0: cannot open file");
                }
            }
        }else {
            System.out.println("Program is using System.in\n" +
                    "To execute lexer, type your commands into the console.\n" +
                    "To end program, type 'EOF' without quotes.\n");
            lexer = new Lexer();
            LexerTester lexerTester = new LexerTester(lexer);
            lexerTester.Run();
        }
    }
}
