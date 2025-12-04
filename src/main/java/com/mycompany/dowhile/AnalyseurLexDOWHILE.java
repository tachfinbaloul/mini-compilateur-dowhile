/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dowhile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tachf
 */
public class AnalyseurLexDOWHILE {
    private  final String source ;
    private int position;//indice du caracere
    private int ligne;//indice de la ligne d'erreur
    private final List<String> erreursLex=new ArrayList<>();
    
    

static final int cl_lettre=0;
static final int cl_chiffre=1;
static final int cl_autre=2;
private static final int [][]Auto_ident={
    {1,-1,-1},
    {1,1,-1}
};
 public AnalyseurLexDOWHILE(String code){
    this.source=code+'#';
    this.position=0;
    this.ligne=1;
    }
private char caractereCourant(){
return source.charAt(position);
}
public List<String> getErreur(){
return erreursLex;
}

private void avancer(){
char c=caractereCourant();
if(c=='\n'){
ligne++;
}
if(c !='#'){
    position++;
}}
private void sauterBlanc(){
        OUTER:
        while (true) {
            char c=caractereCourant();
            switch (c) {
                case ' ':
                case '\t':
                case '\r':
                    avancer();
                    break;
                case '\n':
                    avancer();
                    break;
                default:
                    break OUTER;
            }
        }
}
int classeCaractere(char c){
if (Character.isLetter(c)|| c=='_') return cl_lettre;
if(Character.isDigit(c))return cl_chiffre;
return cl_autre;
}
private Token lireIdentOuMotCle(){
    StringBuilder sb=new StringBuilder();
    int etat=0;
    while(true){
        char c=caractereCourant();
        int cl=classeCaractere(c);
        int nouvelEtat=Auto_ident[etat][cl];
        if(nouvelEtat==-1){
        break;
        }
        etat=nouvelEtat;
        sb.append(c);
        avancer();
    }
    String lexeme=sb.toString();
        return switch (lexeme) {
            case "do" -> new Token(TokenType.DO,lexeme,ligne);
            case "while" -> new Token(TokenType.WHILE,lexeme,ligne);
            case"super"->new Token(TokenType.SUPER,lexeme,ligne);
            case"null"->new Token(TokenType.NULL,lexeme,ligne);
            case"this"->new Token(TokenType.THIS,lexeme,ligne);
            case"typeof"->new Token(TokenType.TYPEOF,lexeme,ligne);
            case "let" -> new Token(TokenType.LET,lexeme,ligne);
            case "var" -> new Token(TokenType.VAR,lexeme,ligne);
            case "const" -> new Token(TokenType.CONST,lexeme,ligne);
            case "true" -> new Token(TokenType.True,lexeme,ligne);
            case "false" -> new Token(TokenType.False,lexeme,ligne);
            case "tachfin" -> new Token(TokenType.TACHFIN,lexeme,ligne);
            case "baloul"  -> new Token(TokenType.BALOUL,lexeme,ligne); 
            case"if"-> new Token(TokenType.IF,lexeme,ligne);
            case"else"->new Token(TokenType.ELSE,lexeme,ligne);
            case"for"->new Token(TokenType.FOR,lexeme,ligne);
            case"return"-> new Token(TokenType.RETURN,lexeme,ligne);
            case"function"->new Token(TokenType.FUNCTION,lexeme,ligne);
            case"break"->new Token(TokenType.BREAK,lexeme,ligne);
            case"switch"->new Token(TokenType.SWITCH,lexeme,ligne);
            case"case"->new Token(TokenType.CASE,lexeme,ligne);
            case"default"->new Token(TokenType.DEFAULT,lexeme,ligne);
            default -> new Token(TokenType.ID,lexeme,ligne);
        };
}
private Token lireNBR(){
StringBuilder sb=new StringBuilder();
while(Character.isDigit(caractereCourant())){
sb.append(caractereCourant());
avancer();
}
return new Token(TokenType.NOMBRE,sb.toString(),ligne);
}
public Token prochainToken(){
    sauterBlanc();
    char c=caractereCourant();
    if(c=='/' && source.charAt(position+1)=='/'){
        avancer();
        avancer();
        while(caractereCourant()!='\n'&& caractereCourant()!='#'){
        avancer();
        }
        return prochainToken();
    }
    if(c=='/' && source.charAt(position+1)=='*'){
        avancer();
        avancer();
        while(true){
        if(caractereCourant()=='#'){
            break;
        }
        if(caractereCourant()=='*'&& source.charAt(position+1)=='/'){
        avancer();
        avancer();
        break;
        }
        avancer();
        }
        return prochainToken();
    }
    if(c=='#'){
    return new Token(TokenType.EOF,"#",ligne);
    }
    if(Character.isLetter(c) || c=='_'){
    return lireIdentOuMotCle();
    }
    if(Character.isDigit(c)){
    return lireNBR();
    }
    switch (c){
    case'(' -> {
        avancer();
        return new Token(TokenType.LPAR,"(",ligne);
            }
    case')' -> {
        avancer();
        return new Token(TokenType.RPAR,")",ligne);
            }
    case'{' -> {
        avancer();
        return new Token(TokenType.LACCO,"{",ligne);
            }
    case'}' -> {
        avancer();
        return new Token(TokenType.RACCO,"}",ligne);
            }
    case';' -> {
        avancer();
        return new Token(TokenType.POINTV,";",ligne);
            }
    case'=' -> {
        avancer();
        if(caractereCourant()=='='){
            avancer();
            return new Token(TokenType.EGAL,"==",ligne);
        }
        return new Token(TokenType.AFFECT,"=",ligne);
            }
    
    case'<' -> {
        avancer();
        if(caractereCourant()=='='){
            avancer();
            return new Token(TokenType.INFEG,"<=",ligne);
        }else
            return new Token(TokenType.INF,"<",ligne);
            }
    case '+' -> {
    avancer();
    if (caractereCourant() == '+') {
        avancer();
        return new Token(TokenType.INCR, "++", ligne);
    } else {
        String msg = "operateur '+' non supporté (utiliser seulement '++')";
        erreursLex.add(msg);
        return new Token(TokenType.ERREUR, "+", ligne);
    }
    }
    case '-' -> {
    avancer();
    if (caractereCourant() == '-') {
        avancer();
        return new Token(TokenType.DECR, "--", ligne);
    } else {
        String msg = "operateur '-' non supporté (utiliser seulement '--')";
        erreursLex.add(msg);
        return new Token(TokenType.ERREUR, "-", ligne);
    }
    }
    case'>' -> {
        avancer();
        if(caractereCourant()=='='){
            avancer();
            return new Token(TokenType.SUPEG,">=",ligne);
        }
        return new Token(TokenType.SUP,">",ligne);
            }
        
        
    }
    String msg="caractere inattendu '"+c+"'a la ligne "+ligne;
    erreursLex.add(msg);
    avancer();
    return new Token(TokenType.ERREUR,String.valueOf(c),ligne);
}
}


    

