/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dowhile;

/**
 *
 * @author tachf
 */
 class Token {
    TokenType type;
    String lex;
    int ligne;
    Token(TokenType type,String lex,int ligne){
        this.type=type;
        this.lex=lex;
        this.ligne=ligne;
    }
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lex + '\'' +
                ", ligne=" + ligne +
                '}';
    }
}
