package com.jonathan.project;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private Reader reader;
    private String readerType;
    private Token token;

    private int lineNumber = 1;
    private boolean isLineBreak = false;
    private String errorMessage = "";

    private Pattern keywordReg =
            Pattern.compile("^(ARRAY|BEGIN|BY|CASE|CONST|DIV|DO|ELSE|ELSIF|"+
                    "END|EXIT|FOR|IF|IMPORT|IN|IS|LOOP|MOD|MODULE|NIL|OF|OR|"+
                    "POINTER|PROCEDURE|RECORD|REPEAT|RETURN|THEN|TO|TYPE|UNTIL|"+
                    "VAR|WHILE|WITH|BOOLEAN|CHAR|FALSE|INTEGER|NEW|REAL|TRUE)$");

    private Pattern symbolReg =
            Pattern.compile("^(&|\\^|:=|:|,|\\.\\.|\\.|\\||=|>|>=|\\{|\\[|\\(|<|<=|-|#|\\+|}|]|\\)|;|~|/|\\*)");

    private Pattern commentReg =
            Pattern.compile("^.*?(\\(\\*|\\*\\)|\\(\\*.*\\*\\)).*");
    private Pattern startCommentReg =
            Pattern.compile("^.*?(\\(\\*).*");
    private Pattern endCommentReg =
            Pattern.compile("^.*?(\\*\\)).*");

    public HashMap<String, Integer> symbolMap = new HashMap<>();


    public Lexer(Reader input, String inputType){
        reader = new BufferedReader(input);
        readerType = inputType;
        createSymbolMap();
    }

    public Lexer(String fileName) throws FileNotFoundException {
        this(new FileReader(fileName), "'"+fileName+"'");
    }

    public Lexer(){
        this(new InputStreamReader(System.in), "(stdin)");
    }

    public Token GetToken(){
        try{
            int r = 0;
            String str = "", comment = "";
            boolean inComment = false;
            while((r = reader.read()) != -1){
                char c = (char)r;
                if(Character.isWhitespace(c)) {
                    // If character is whitespace
                    if(str.matches("^(\"[a-zA-Z0-9\\s\']*(?!\")|\'[a-zA-Z0-9\\s\"]*(?!\'))")){
                        //TODO: Symbols in string literal
                        if (c == ' '){
                            str += c;
                        }
                        int next = peek();
                        if (next == -1){
                            outputErrorMessage("unterminated string literal");
                            outputErrorMessage("EOF in string literal");
                            str = "";
                        }else if(c =='\r' || c =='\n'){
                            outputErrorMessage("unterminated string literal");
                            outputErrorMessage("newline in string literal");
                            lineNumber++;
                            str = "";
                        }
                        continue;
                    }
                    if(c == '\r' || c == '\n'){
                        this.isLineBreak = true;
                    }
                    if(str.length() <= 0) {
                        if (this.isLineBreak){
                            this.isLineBreak = false;
                            this.lineNumber++;
                        }
                        continue;
                    }else{
                        // Handles all cases where there is whitespace after the symbol, expression, etc
                        if(str.equals("INCLUDE")){
                            str = "";
                            continue;
                        }
                        return readToken(str);
                    }
                }
                if(!inComment){
                    str += c;
                    if (c == '*' && commentReg.matcher(str).matches()){
                        // Handle comments
                        str = str.substring(0, str.indexOf("(*"));
                        inComment = true;
                        comment = "(*";
                        continue;
                    }
                    Matcher m = symbolReg.matcher(Character.toString(c));
                    Matcher m2 = keywordReg.matcher(str);
                    if (m.matches()){
                        // Handle Punctuations
                        if(!peekForSymbol(c)){
                            return readToken(str);
                        }
                    }
                    if(str.matches("^[a-zA-Z0-9]+$")){
                        // Handle any alpha-numeric name
                        if(peekForSymbol()){
                            return readToken(str);
                        }
                    }
                    if(str.matches("^(\"[a-zA-Z0-9\\s\']*\"|\'[a-zA-Z0-9\\s\"]*\')")){
                        // Handle string literals
                        if(peekForSymbol()){
                            return readToken(str);
                        }
                    }
                }else{
                    comment += c;
                    if(c == ')' && endCommentReg.matcher(comment).matches()){
                        inComment = false;
                        comment = "";
                    }
                }
            }
        }catch(Exception e){
            errorMessage = e.getMessage();
        }finally {
            outputErrorMessage();
        }
        return (new Token(Sym.EOF, "EOF"));
    }

    private Token readToken(String str) throws ParseException {
        token = null;
        Matcher m = keywordReg.matcher(str);
        Matcher m2 = symbolReg.matcher(str);
//        System.out.println("String: "+str);
        if (m.find() || m2.find()) {
            if (symbolMap.containsKey(str) && symbolMap.get(str) >= 0) {
                token = new Token(symbolMap.get(str), str);
            }
        }else if (str.matches("^([0-9]+[a-fA-F0-9]*[hH]*)$")){
            //INT_LITERAL CASE
            // remove leading 0's; check for illegal hex;
            // int_literal must be no more than 10 digits
            if(str.startsWith("0")){
                str = str.replaceFirst("^0+(?!$)", "");
            }
            if (str.matches("^.*?[a-fA-F].*$") && !str.endsWith("H")){
                outputErrorMessage("illegal hex integer literal");
                if (str.length() > 10){
                    outputErrorMessage("integer literal too long");
                    str = str.substring(0,10)+"H";
                }else{
                    str += "H";
                }
            }
            if ((str.length() > 10 && !str.endsWith("H")) || (str.length() > 11 && str.endsWith("H"))){
                outputErrorMessage("integer literal too long");
                if(str.endsWith("H")) {
                    str = str.substring(0, 10)+"H";
                }else{
                    str += str.substring(0, 10);
                }
            }
            token = new Token(Sym.T_INT_LITERAL, str);
        }else if (str.matches("^([0-9]+[a-fA-F0-9]*[xX]*)$")){
            //CHAR_LITERAL CASE
            // sequence of up to 3 hex digits followed by 'X'
            if(str.startsWith("0")){
                str = str.replaceFirst("^0+(?!$)", "");
            }
            if ((str.length() > 3 && !str.endsWith("X")) || (str.length() > 4 && str.endsWith("X"))) {
                outputErrorMessage("character literal too long");
                if (str.endsWith("X")) {
                    str = str.substring(0, 3) + "X";
                } else {
                    str += str.substring(0, 3);
                }
            }
            token = new Token(Sym.T_CHAR_LITERAL, str);
        }else if (str.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            //IDENTIFIER CASE
            // sequence of up to 40 characters
            if (str.equals("EOF")) {
                // Process EOF from System.in
                System.out.println("Program terminated");
                return (new Token(Sym.EOF, "EOF"));
            }
            if (str.length() > 40) {
                outputErrorMessage("identifier too long");
                str += str.substring(0, 40);
            }
            token = new Token(Sym.T_ID, str);
        }else if(str.matches("^(\"[a-zA-Z0-9\\s\']*\"|\'[a-zA-Z0-9\\s\"]*\')")){
            //STR_LITERAL CASE
            // sequence of up to 80 characters not including surrounding quotes
            if (str.length() > 82){
                outputErrorMessage("string literal too long");
                str = str.substring(0,81)+ str.charAt(str.length()-1);
            }
            String temp = str.substring(1, str.length()-1);
            token = new Token(Sym.T_STR_LITERAL, temp);
        }else{
            // Instructions ambiguous how to handle error messages
            // i.e. program termination or continuation with a message?
            outputErrorMessage("Unknown character '"+str+"'");
        }
        if (this.isLineBreak){
            this.isLineBreak = false;
            this.lineNumber++;
        }
        return token;

    }

    private boolean peekForSymbol(){
        return peekForSymbol(Character.MIN_VALUE);
    }
    private boolean peekForSymbol(char currentSymbol){
        char t = (char)peek();
        String str = Character.toString(currentSymbol)+Character.toString(t);
        Matcher symbol = symbolReg.matcher(Character.toString(t));
        if (symbol.find() && (symbolMap.containsKey(str.trim()) || str.equals("(*"))){
            return true;
        }
        return false;
    }
    private int peek(){
        int p = -1;
        try {
            reader.mark(1);
            p = reader.read();
            reader.reset();
        } catch (IOException e) {
            outputErrorMessage(e.getMessage());
        }
        return p;
    }

    private void createSymbolMap() {
        // Adding Oberon keywords
        symbolMap.put("ARRAY", Sym.T_ARRAY);
        symbolMap.put("BEGIN", Sym.T_BEGIN);
        symbolMap.put("BY", Sym.T_BY);
        symbolMap.put("CASE", Sym.T_CASE);
        symbolMap.put("CONST", Sym.T_CONST);
        symbolMap.put("DIV", Sym.T_DIV);
        symbolMap.put("DO", Sym.T_DO);
        symbolMap.put("ELSE", Sym.T_ELSE);
        symbolMap.put("ELSIF", Sym.T_ELSIF);
        symbolMap.put("END", Sym.T_END);
        symbolMap.put("EXIT", Sym.T_EXIT);
        symbolMap.put("FOR", Sym.T_FOR);
        symbolMap.put("IF", Sym.T_IF);
        symbolMap.put("IMPORT", Sym.T_IMPORT);
        symbolMap.put("IN", Sym.T_IN);
        symbolMap.put("IS", Sym.T_IS);
        symbolMap.put("LOOP", Sym.T_LOOP);
        symbolMap.put("MOD", Sym.T_MOD);
        symbolMap.put("MODULE", Sym.T_MODULE);
        symbolMap.put("NIL", Sym.T_NIL);
        symbolMap.put("OF", Sym.T_OF);
        symbolMap.put("OR", Sym.T_OR);
        symbolMap.put("POINTER", Sym.T_POINTER);
        symbolMap.put("PROCEDURE", Sym.T_PROCEDURE);
        symbolMap.put("RECORD", Sym.T_RECORD);
        symbolMap.put("REPEAT", Sym.T_REPEAT);
        symbolMap.put("RETURN", Sym.T_RETURN);
        symbolMap.put("THEN", Sym.T_THEN);
        symbolMap.put("TO", Sym.T_TO);
        symbolMap.put("TYPE", Sym.T_TYPE);
        symbolMap.put("UNTIL", Sym.T_UNTIL);
        symbolMap.put("VAR", Sym.T_VAR);
        symbolMap.put("WHILE", Sym.T_WHILE);
        symbolMap.put("WITH", Sym.T_WITH);
        // Adding Oberon predeclared identifiers
        symbolMap.put("BOOLEAN", Sym.T_BOOLEAN);
        symbolMap.put("CHAR", Sym.T_CHAR);
        symbolMap.put("FALSE", Sym.T_FALSE);
        symbolMap.put("INTEGER", Sym.T_INTEGER);
        symbolMap.put("NEW", Sym.T_NEW);
        symbolMap.put("REAL", Sym.T_REAL);
        symbolMap.put("TRUE", Sym.T_TRUE);
        // Adding Oberon punctuation symbols
        symbolMap.put("&", Sym.T_AMPERSAND);
        symbolMap.put("^", Sym.T_ARROW);
        symbolMap.put(":=", Sym.T_ASSIGN);
        symbolMap.put("|", Sym.T_BAR);
        symbolMap.put(":", Sym.T_COLON);
        symbolMap.put(",", Sym.T_COMMA);
        symbolMap.put("..", Sym.T_DOTDOT);
        symbolMap.put(".", Sym.T_DOT);
        symbolMap.put("=", Sym.T_EQU);
        symbolMap.put(">", Sym.T_GT);
        symbolMap.put(">=", Sym.T_GTE);
        symbolMap.put("{", Sym.T_LBRACE);
        symbolMap.put("[", Sym.T_LBRACKET);
        symbolMap.put("(", Sym.T_LPAREN);
        symbolMap.put("<", Sym.T_LT);
        symbolMap.put("<=", Sym.T_LTE);
        symbolMap.put("-", Sym.T_MINUS);
        symbolMap.put("#", Sym.T_NEQ);
        symbolMap.put("+", Sym.T_PLUS);
        symbolMap.put("}", Sym.T_RBRACE);
        symbolMap.put("]", Sym.T_RBRACKET);
        symbolMap.put(")", Sym.T_RPAREN);
        symbolMap.put(";", Sym.T_SEMI);
        symbolMap.put("~", Sym.T_TILDE);
        symbolMap.put("/", Sym.T_SLASH);
        symbolMap.put("*", Sym.T_STAR);
    }

    private void outputErrorMessage(){
        outputErrorMessage(this.errorMessage);
        this.errorMessage = "";
    }
    private void outputErrorMessage(String message){
        if (message.length() > 0)
            System.out.println("Error, "+this.readerType+", line "+(this.lineNumber)+": "+message);
    }
    /*
    Using hash map in place of switch statement
    public int getTokenTypeByString(String keyword) {
        int tokenType = -1;
        switch (keyword){
            case "ARRAY":
                tokenType = Sym.T_ARRAY;
                break;
            case "BEGIN":
                tokenType = Sym.T_BEGIN;
                break;
            case "BY":
                tokenType = Sym.T_BY;
                break;
            case "CASE":
                tokenType = Sym.T_CASE;
                break;
            case "CONST":
                tokenType = Sym.T_CONST;
                break;
            case "DIV":
                tokenType = Sym.T_DIV;
                break;
            case "DO":
                tokenType = Sym.T_DO;
                break;
            case "ELSE":
                tokenType = Sym.T_ELSE;
                break;
            case "ELSIF":
                tokenType = Sym.T_ELSIF;
                break;
            case "END":
                tokenType = Sym.T_END;
                break;
            case "EXIT": tokenType = Sym.T_EXIT;
                break;
            case "FOR":
                tokenType = Sym.T_FOR;
                break;
            case "IF":
                tokenType = Sym.T_IF;
                break;
            case "IMPORT":
                tokenType = Sym.T_IMPORT;
                break;
            case "IN":
                tokenType = Sym.T_IN;
                break;
            case "IS":
                tokenType = Sym.T_IS;
                break;
            case "LOOP":
                tokenType = Sym.T_LOOP;
                break;
            case "MOD":
                tokenType = Sym.T_MOD;
                break;
            case "MODULE":
                tokenType = Sym.T_MODULE;
                break;
            case "NIL":
                tokenType = Sym.T_NIL;
                break;
            case "OF":
                tokenType = Sym.T_OF;
                break;
            case "OR":
                tokenType = Sym.T_OR;
                break;
            case "POINTER":
                tokenType = Sym.T_POINTER;
                break;
            case "PROCEDURE":
                tokenType = Sym.T_PROCEDURE;
                break;
            case "RECORD":
                tokenType = Sym.T_RECORD;
                break;
            case "REPEAT":
                tokenType = Sym.T_REPEAT;
                break;
            case "RETURN":
                tokenType = Sym.T_RETURN;
                break;
            case "THEN":
                tokenType = Sym.T_THEN;
                break;
            case "TO":
                tokenType = Sym.T_TO;
                break;
            case "TYPE":
                tokenType = Sym.T_TYPE;
                break;
            case "UNTIL":
                tokenType = Sym.T_UNTIL;
                break;
            case "VAR":
                tokenType = Sym.T_VAR;
                break;
            case "WHILE":
                tokenType = Sym.T_WHILE;
                break;
            case "WITH":
                tokenType = Sym.T_WITH;
                break;
            case "BOOLEAN":
                tokenType = Sym.T_BOOLEAN;
                break;
            case "CHAR":
                tokenType = Sym.T_CHAR;
                break;
            case "FALSE":
                tokenType = Sym.T_FALSE;
                break;
            case "INTEGER":
                tokenType = Sym.T_INTEGER;
                break;
            case "NEW":
                tokenType = Sym.T_NEW;
                break;
            case "REAL":
                tokenType = Sym.T_REAL;
                break;
            case "TRUE":
                tokenType = Sym.T_TRUE;
                break;
            case "&":
                tokenType = Sym.T_AMPERSAND;
                break;
            case "^":
                tokenType = Sym.T_ARROW;
                break;
            case ":=":
                tokenType = Sym.T_ASSIGN;
                break;
            case "|":
                tokenType = Sym.T_BAR;
                break;
            case ":":
                tokenType = Sym.T_COLON;
                break;
            case ",":
                tokenType = Sym.T_COMMA;
                break;
            case "..":
                tokenType = Sym.T_DOTDOT;
                break;
            case ".":
                tokenType = Sym.T_DOT;
                break;
            case "=":
                tokenType = Sym.T_EQU;
                break;
            case ">":
                tokenType = Sym.T_GT;
                break;
            case ">=":
                tokenType = Sym.T_GTE;
                break;
            case "{":
                tokenType = Sym.T_LBRACE;
                break;
            case "[":
                tokenType = Sym.T_LBRACKET;
                break;
            case "(":
                tokenType = Sym.T_LPAREN;
                break;
            case "<":
                tokenType = Sym.T_LT;
                break;
            case "<=":
                tokenType = Sym.T_LTE;
                break;
            case "-":
                tokenType = Sym.T_MINUS;
                break;
            case "#":
                tokenType = Sym.T_NEQ;
                break;
            case "+":
                tokenType = Sym.T_PLUS;
                break;
            case "}":
                tokenType = Sym.T_RBRACE;
                break;
            case "]":
                tokenType = Sym.T_RBRACKET;
                break;
            case ")":
                tokenType = Sym.T_RPAREN;
                break;
            case ";":
                tokenType = Sym.T_SEMI;
                break;
            case "~":
                tokenType = Sym.T_TILDE;
                break;
            case "/":
                tokenType = Sym.T_SLASH;
                break;
            case "*":
                tokenType = Sym.T_STAR;
                break;
            default:
                tokenType = Sym.T_ID;
                break;
        }
        return tokenType;
    }
    */
}
