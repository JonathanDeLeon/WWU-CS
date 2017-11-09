package com.jonathan.project;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexer class analyzes a given stream (file or sys.in) performs the basic functions:
 * 1) recognizes patterns returning them as Oberon Tokens
 * 2) Discards inputs not recognizable
 * 3) Reports lexical errors
 */
public class Lexer {
    private Reader reader;
    private String readerType;
    private Token token;

    /* Flags to use */
    private int lineNumber = 1;
    private boolean isLineBreak = false;
    private boolean inInclude = false;
    private boolean inComment = false;
    private String errorMessage = "";

    /* RESERVED Regular Expressions */
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

    private HashMap<String, Integer> symbolMap = new HashMap<>();


    /**
     * Constructors
     */
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

    /**
     * Method called from LexerTester.java
     * Parses the reader and extracts tokens on the lexemes
     * Uses Token.java and Sym.java
     * @return Token
     */
    public Token GetToken(){
        try{
            int r;
            String str = "", comment = "";
            //Loop through every character in the BufferReader
            while((r = reader.read()) != -1){
                char c = (char)r;
                if(Character.isWhitespace(c)) {
                    // If character is whitespace
                    if(str.matches("(\"[^\"]*(?!\")|\'[^\']*(?!\'))")){
                        // Case to handle white space when string literal has no ending quotes
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
                    //Handle line breaks (new lines)
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
                            this.inInclude = true;
                            continue;
                        }
                        if (readToken(str) != null){
                            return this.token;
                        }else{
                            str = "";
                            continue;
                        }
                    }
                }
                if(!this.inComment){
                    //When not in a comment scope, build lexeme
                    str += c;
                    if (c == '*' && commentReg.matcher(str).matches() && !str.matches("^.*?([\'\"])+.*$")){
                        // Checks if lexer is entering a comment scope
                        str = str.substring(0, str.indexOf("(*"));
                        this.inComment = true;
                        comment = "(*";
                        continue;
                    }
                    Matcher m = symbolReg.matcher(str);
                    if (m.matches()){
                        // Handle reserved punctuations
                        if(!peekForSymbol(c)){
                            //If next character is not a recognized symbol, get the token of current symbol
                            //i.e. '*,' str='*' next=',' next is symbol but '*,' is not recognizable
                            // thus return token punctuation '*'
                            return readToken(str);
                        }
                    }
                    if(str.matches("^[a-zA-Z0-9]+$") || str.matches("(\"[^\"]*\"|\'[^\']*\')")){
                        // Handle any alpha-numeric name
                        // or string literal
                        if(peekForSymbol()){
                            //If next character is a symbol, get the token of current string
                            //i.e. 'n=100' str='n' next='=' next is symbol thus, return token identifier 'n'
                            if (readToken(str) != null){
                                return this.token;
                            }else{
                                str = "";
                            }
                        }
                    }
                }else{
                    //When in a comment scope
                    comment += c;
                    if(c == ')' && endCommentReg.matcher(comment).matches()){
                        // Handles nested comments
                        int start = 0;
                        int end = 0;
                        int lastIndex = 0;
                        while(lastIndex != -1){
                            lastIndex = comment.indexOf("(*", lastIndex);
                            if (lastIndex != -1){
                                start++;
                                lastIndex += 2;
                            }
                        }
                        lastIndex = 0;
                        while(lastIndex != -1){
                            lastIndex = comment.indexOf("*)", lastIndex);
                            if (lastIndex != -1){
                                end++;
                                lastIndex += 2;
                            }
                        }
                        if (start == end) {
                            this.inComment = false;
                            comment = "";
                        }
                    }
                    //If next character is EOF, output error
                    int next = peek();
                    if (next == -1 && this.inComment){
                        outputErrorMessage("unterminated comment");
                    }
                }
            }
        }catch(Exception e){
            errorMessage = e.getMessage();
        }finally {
            //Output any error messages in the queue
            outputErrorMessage();
        }
        //Terminate program
        return (new Token(Sym.EOF, "EOF"));
    }

    /**
     * Read given lexeme and match it to a token condition (case)
     * @param str lexeme to match
     * @return Token
     */
    private Token readToken(String str){
        token = null;
        //Open include files if they exist and scan them
        if (this.inInclude){
            this.inInclude = false;
            if(str.matches("(\"[^\"]*\"|\'[^\']*\')")){
                //Remove outer quotes in the string literal
                String temp = str.substring(1, str.length()-1);
                try {
                    Lexer lexer = new Lexer(temp);
                    LexerTester lexerTester = new LexerTester(lexer);
                    lexerTester.Run();
                } catch (FileNotFoundException e) {
                    outputErrorMessage("bad include file "+str);
                }
            }else{
                outputErrorMessage("illegal include directive '"+str+"'");
            }
            str = "";
        }
        Matcher m = keywordReg.matcher(str);
        Matcher m2 = symbolReg.matcher(str);
        if (m.find() || m2.find()) {
            //Lexeme matches RESERVED tokens
            if (symbolMap.containsKey(str) && symbolMap.get(str) >= 0) {
                token = new Token(symbolMap.get(str), str);
            }
        }else if (str.matches("^([0-9]+[a-fA-F0-9]*[hH]*)$")){
            //INT_LITERAL CASE
            // remove leading 0's
            if(str.startsWith("0")){
                str = str.replaceFirst("^0+(?!$)", "");
            }
            // check for illegal hex
            if (str.matches("^.*?[a-fA-F].*$") && !str.endsWith("H")){
                outputErrorMessage("illegal hex integer literal");
                if (str.length() > 10){
                    outputErrorMessage("integer literal too long");
                    str = str.substring(0,10)+"H";
                }else{
                    str += "H";
                }
            }
            // int_literal must be no more than 10 digits
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
            // remove leading 0's
            if(str.startsWith("0")){
                str = str.replaceFirst("^0+(?!$)", "");
            }
            // sequence of up to 3 hex digits followed by 'X'
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
            // Process EOF from System.in
            if (str.equals("EOF")) {
                System.out.println("Program terminated");
                return (new Token(Sym.EOF, "EOF"));
            }
            // sequence of up to 40 characters
            if (str.length() > 40) {
                outputErrorMessage("identifier too long");
                str += str.substring(0, 40);
            }
            token = new Token(Sym.T_ID, str);
        }else if(str.matches("(\"[^\"]*\"|\'[^\']*\')")){
            //STR_LITERAL CASE
            // sequence of up to 80 characters not including surrounding quotes
            if (str.length() > 82){
                outputErrorMessage("string literal too long");
                str = str.substring(0,81)+ str.charAt(str.length()-1);
            }
            //Do not return surrounding quotes
            String temp = str.substring(1, str.length()-1);
            token = new Token(Sym.T_STR_LITERAL, temp);
        }else{
            //Unknown character in a lexeme
            if(str.length() > 0)
                outputErrorMessage("Unknown character in lexeme '"+str+"'");
        }
        //Add lineNumber on a linebreak
        if (this.isLineBreak){
            this.isLineBreak = false;
            this.lineNumber++;
        }
        return token;

    }

    /**
     * Determine if next character in reader is a reserved punctuation
     * Use overloaded method
     * @return boolean
     */
    private boolean peekForSymbol(){
        return peekForSymbol(Character.MIN_VALUE);
    }
    private boolean peekForSymbol(char currentSymbol){
        char t = (char)peek();
        String str = Character.toString(currentSymbol)+Character.toString(t);
        Matcher symbol = symbolReg.matcher(Character.toString(t));
        return symbol.find() && (symbolMap.containsKey(str.trim()) || str.equals("(*"));
    }

    /**
     * Bread and butter of the program
     * Peeks at the next character in the reader
     * @return int ASCII code of next character
     */
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

    /**
     * Initialize HashMap of RESERVED tokens
     */
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

    /**
     * Output error messages either in the queue or a specific message using standard output
     * Uses overloaded method
     */
    private void outputErrorMessage(){
        outputErrorMessage(this.errorMessage);
        this.errorMessage = "";
    }
    private void outputErrorMessage(String message){
        if (message.length() > 0)
            System.out.println("Error, "+this.readerType+", line "+(this.lineNumber)+": "+message);
    }
}
