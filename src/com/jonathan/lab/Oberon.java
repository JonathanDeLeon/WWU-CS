package com.jonathan.lab;


import java.io.File;

public class Oberon {
    public static void main(String[] args){
        try {
            Lexer lexer;
            File f = new File("oberon.txt");
            if(args.length > 0){
                lexer = new Lexer(args[0]);
            }else if(f.exists()) {
                lexer = new Lexer(f.getName());
            }else{
                System.out.println("Program is using System.in\n"+
                        "To execute lexer, type your commands into the console.\n"+
                        "To end program, type 'EOF' without quotes.\n");
                lexer = new Lexer();
            }
            LexerTester lexerTester = new LexerTester(lexer);
            lexerTester.Run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
