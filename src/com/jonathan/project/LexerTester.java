package com.jonathan.project;

//---------------------------------------------------------------------
//
//  LexerTester.java
//
//---------------------------------------------------------------------

import java.io.*;

public class LexerTester
{
    //-----------------------------------------------------------------
    //
    //-----------------------------------------------------------------

    public LexerTester (Lexer lexer)
    {
        m_lexer = lexer;
    }

    //-----------------------------------------------------------------
    //
    //-----------------------------------------------------------------

    public void Run ()
    {
        boolean     bDone = false;
        Token       token = null;
        String      strLexeme;


        while (true)
        {
            token = m_lexer.GetToken();

            if (token.GetType () == Sym.EOF)
                break;

            strLexeme = token.GetLexeme ();
            while (strLexeme.length () < 20)
                strLexeme += " ";

            System.out.print ("lexeme: <" + strLexeme + "> ");

            switch (token.GetType ())
            {
                case Sym.T_ARRAY:
                    System.out.println ("keyword: ARRAY");
                    break;
                case Sym.T_BEGIN:
                    System.out.println ("keyword: BEGIN");
                    break;
                case Sym.T_BY:
                    System.out.println ("keyword: BY");
                    break;
                case Sym.T_CASE:
                    System.out.println ("keyword: CASE");
                    break;
                case Sym.T_CONST:
                    System.out.println ("keyword: CONST");
                    break;
                case Sym.T_DIV:
                    System.out.println ("keyword: DIV");
                    break;
                case Sym.T_DO:
                    System.out.println ("keyword: DO");
                    break;
                case Sym.T_ELSE:
                    System.out.println ("keyword: ELSE");
                    break;
                case Sym.T_ELSIF:
                    System.out.println ("keyword: ELSIF");
                    break;
                case Sym.T_END:
                    System.out.println ("keyword: END");
                    break;
                case Sym.T_EXIT:
                    System.out.println ("keyword: EXIT");
                    break;
                case Sym.T_FOR:
                    System.out.println ("keyword: FOR");
                    break;
                case Sym.T_IF:
                    System.out.println ("keyword: IF");
                    break;
                case Sym.T_IMPORT:
                    System.out.println ("keyword: IMPORT");
                    break;
                case Sym.T_IN:
                    System.out.println ("keyword: IN");
                    break;
                case Sym.T_IS:
                    System.out.println ("keyword: IS");
                    break;
                case Sym.T_LOOP:
                    System.out.println ("keyword: LOOP");
                    break;
                case Sym.T_MOD:
                    System.out.println ("keyword: MOD");
                    break;
                case Sym.T_MODULE:
                    System.out.println ("keyword: MODULE");
                    break;
                case Sym.T_NIL:
                    System.out.println ("keyword: NIL");
                    break;
                case Sym.T_OF:
                    System.out.println ("keyword: OF");
                    break;
                case Sym.T_OR:
                    System.out.println ("keyword: OR");
                    break;
                case Sym.T_POINTER:
                    System.out.println ("keyword: POINTER");
                    break;
                case Sym.T_PROCEDURE:
                    System.out.println ("keyword: PROCEDURE");
                    break;
                case Sym.T_RECORD:
                    System.out.println ("keyword: RECORD");
                    break;
                case Sym.T_REPEAT:
                    System.out.println ("keyword: REPEAT");
                    break;
                case Sym.T_RETURN:
                    System.out.println ("keyword: RETURN");
                    break;
                case Sym.T_THEN:
                    System.out.println ("keyword: THEN");
                    break;
                case Sym.T_TO:
                    System.out.println ("keyword: TO");
                    break;
                case Sym.T_TYPE:
                    System.out.println ("keyword: TYPE");
                    break;
                case Sym.T_UNTIL:
                    System.out.println ("keyword: UNTIL");
                    break;
                case Sym.T_VAR:
                    System.out.println ("keyword: VAR");
                    break;
                case Sym.T_WHILE:
                    System.out.println ("keyword: WHILE");
                    break;
                case Sym.T_WITH:
                    System.out.println ("keyword: WITH");
                    break;
                case Sym.T_BOOLEAN:
                    System.out.println ("predef id: BOOLEAN");
                    break;
                case Sym.T_CHAR:
                    System.out.println ("predef id: CHAR");
                    break;
                case Sym.T_FALSE:
                    System.out.println ("predef id: FALSE");
                    break;
                case Sym.T_INTEGER:
                    System.out.println ("predef id: INTEGER");
                    break;
                case Sym.T_NEW:
                    System.out.println ("predef id: NEW");
                    break;
                case Sym.T_REAL:
                    System.out.println ("predef id: REAL");
                    break;
                case Sym.T_TRUE:
                    System.out.println ("predef id: TRUE");
                    break;
                case Sym.T_AMPERSAND:
                    System.out.println ("punctuation: AMPERSAND");
                    break;
                case Sym.T_ARROW:
                    System.out.println ("punctuation: ARROW");
                    break;
                case Sym.T_ASSIGN:
                    System.out.println ("punctuation: ASSIGN");
                    break;
                case Sym.T_BAR:
                    System.out.println ("punctuation: BAR");
                    break;
                case Sym.T_COLON:
                    System.out.println ("punctuation: COLON");
                    break;
                case Sym.T_COMMA:
                    System.out.println ("punctuation: COMMA");
                    break;
                case Sym.T_DOTDOT:
                    System.out.println ("punctuation: DOTDOT");
                    break;
                case Sym.T_DOT:
                    System.out.println ("punctuation: DOT");
                    break;
                case Sym.T_EQU:
                    System.out.println ("punctuation: EQU");
                    break;
                case Sym.T_GT:
                    System.out.println ("punctuation: GT");
                    break;
                case Sym.T_GTE:
                    System.out.println ("punctuation: GTE");
                    break;
                case Sym.T_LBRACE:
                    System.out.println ("punctuation: LBRACE");
                    break;
                case Sym.T_LBRACKET:
                    System.out.println ("punctuation: LBRACKET");
                    break;
                case Sym.T_LPAREN:
                    System.out.println ("punctuation: LPAREN");
                    break;
                case Sym.T_LT:
                    System.out.println ("punctuation: LT");
                    break;
                case Sym.T_LTE:
                    System.out.println ("punctuation: LTE");
                    break;
                case Sym.T_MINUS:
                    System.out.println ("punctuation: MINUS");
                    break;
                case Sym.T_NEQ:
                    System.out.println ("punctuation: NEQ");
                    break;
                case Sym.T_PLUS:
                    System.out.println ("punctuation: PLUS");
                    break;
                case Sym.T_RBRACE:
                    System.out.println ("punctuation: RBRACE");
                    break;
                case Sym.T_RBRACKET:
                    System.out.println ("punctuation: RBRACKET");
                    break;
                case Sym.T_RPAREN:
                    System.out.println ("punctuation: RPAREN");
                    break;
                case Sym.T_SEMI:
                    System.out.println ("punctuation: SEMI");
                    break;
                case Sym.T_SLASH:
                    System.out.println ("punctuation: SLASH");
                    break;
                case Sym.T_STAR:
                    System.out.println ("punctuation: STAR");
                    break;
                case Sym.T_TILDE:
                    System.out.println ("punctuation: TILDE");
                    break;
                case Sym.T_ID:
                    System.out.println ("identifier");
                    break;
                case Sym.T_INT_LITERAL:
                    System.out.println ("integer literal");
                    break;
                case Sym.T_STR_LITERAL:
                    System.out.println ("string literal");
                    break;
                case Sym.T_REAL_LITERAL:
                    System.out.println ("real literal");
                    break;
                case Sym.T_CHAR_LITERAL:
                    System.out.println ("character literal");
                    break;
            }
        }
    }

    //-----------------------------------------------------------------
    //
    //-----------------------------------------------------------------

    private Lexer   m_lexer;
}

