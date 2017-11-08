package com.jonathan.project;


import java.io.File;
import java.io.FileNotFoundException;

public class Oberon {
    public static void main(String[] args){
        Lexer lexer = null;
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
            //Use standard in stream to run program or "oberon.txt" if it exists
            File f = new File("oberon.txt");
            if (f.exists()) {
                try {
                    lexer = new Lexer(f.getName());
                } catch (FileNotFoundException e) {
                    System.out.println("Error, '"+f.getName()+"', line 0: cannot open file");
                }
            } else {
                System.out.println("Program is using System.in\n" +
                        "To execute lexer, type your commands into the console.\n" +
                        "To end program, type 'EOF' without quotes.\n");
                lexer = new Lexer();
            }
            if (lexer != null) {
                LexerTester lexerTester = new LexerTester(lexer);
                lexerTester.Run();
            }
        }
    }
}
