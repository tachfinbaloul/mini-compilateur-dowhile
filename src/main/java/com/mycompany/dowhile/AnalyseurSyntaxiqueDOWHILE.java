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
    public class AnalyseurSyntaxiqueDOWHILE {
    private final AnalyseurLexDOWHILE lex;
    private Token courant;
    private final List<String> erreursSyn=new ArrayList<>();
    private boolean enRecuperation=false;
    
    public AnalyseurSyntaxiqueDOWHILE(AnalyseurLexDOWHILE lex){
    this.lex=lex;
    this.courant=lex.prochainToken();
    }
    public List<String> getErreurSyn(){
    return erreursSyn;
    }
    private void avancer(){
    courant=lex.prochainToken();
    }
    private void erreursSyntaxique(String message){
    if (enRecuperation) return;
    enRecuperation=true;
    String msgComplet = "Erreur syntaxique ligne " + courant.ligne +
            " : " + message + " (trouvé " + courant.type + " ' " + courant.lex + " ')";
    erreursSyn.add(msgComplet);
    }
    private void consommer(TokenType attendu,String MsgErreur){
    if(courant.type==attendu){
        avancer();
        if (attendu == TokenType.POINTV) {
            enRecuperation = false;
        }
        return;
    }
    erreursSyntaxique(MsgErreur + " attendu : " + attendu);

    if (attendu == TokenType.POINTV) {
        while (courant.type != TokenType.POINTV &&
               courant.type != TokenType.RACCO &&
               courant.type != TokenType.EOF) {
            avancer();
        }
        if (courant.type == TokenType.POINTV) {
            avancer();
        }
        enRecuperation = false; 
    } else {
        if(courant.type!=TokenType.EOF){
            avancer();
        }
    }
    }
    private boolean estIdent() {
    return courant.type == TokenType.ID
        || courant.type == TokenType.TACHFIN
        || courant.type == TokenType.BALOUL;
}

    private void consommerIdent(String msgErreur) {
    if (estIdent()) {
        avancer();
    } else {
        erreursSyntaxique(msgErreur + " (identificateur attendu)");
        if (courant.type != TokenType.EOF) {
            avancer();
        }
    }
}

    public void analserProgramme(){
    analyserDOWHILE();
    if(courant.type!=TokenType.EOF){
    erreursSyntaxique("EOF attendu");
    }
    if(erreursSyn.isEmpty()){
    System.out.println("Analyse syntaxique reussi");
    }else{
    System.out.println("analyse syntaxique terminé avec erreurs");
    }
    }
    private void analyserDOWHILE(){
        consommer(TokenType.DO,"DO attendu");
        analyserBloc();
        consommer(TokenType.WHILE,"while attendu");
        consommer(TokenType.LPAR,"LPAR attendu");
        analyserCondition();
        consommer(TokenType.RPAR,"RPAR attendu");
        consommer(TokenType.POINTV,"point virgule attendu");
    }
    private void analyserBloc(){
        consommer(TokenType.LACCO,"LACCO attendu");
        analyserListeInst();
        consommer(TokenType.RACCO,"RACCO attendu");
    }
    private void analyserListeInst(){
    while(estDebutInstruction(courant.type)){
        analyserInstruction();
    }
    }
    private boolean estDebutInstruction(TokenType t){
        return t==TokenType.LET 
              || t==TokenType.VAR
              || t==TokenType.CONST
              || t==TokenType.ID
              || t==TokenType.DO
              || t==TokenType.BALOUL
              || t==TokenType.TACHFIN;
    }
    private void analyserInstruction(){
    if(courant.type==TokenType.DO){
        analyserDOWHILE();
    }else if(courant.type==TokenType.LET || courant.type==TokenType.VAR||courant.type==TokenType.CONST){
        analyserDeclaration();
    }else if(courant.type==TokenType.ID ||courant.type==TokenType.BALOUL||courant.type==TokenType.TACHFIN){
        analyserAffectation();
    }else{
        erreursSyntaxique("Debut d'instruction inattendu");
        while(courant.type!=TokenType.POINTV &&
              courant.type!=TokenType.RACCO &&
              courant.type!=TokenType.EOF){
            avancer();
        }
        while(courant.type==TokenType.POINTV){
            avancer();
        }
        enRecuperation = false; 
    }
}

    
    private void analyserDeclaration(){
        if(courant.type==TokenType.LET ||courant.type==TokenType.VAR||courant.type==TokenType.CONST){
        avancer();
        }else{
            erreursSyntaxique("mot clé attendu let var const");
            if(courant.type!=TokenType.EOF){
            avancer();
            }
        
        }
        consommerIdent("id attendu");
        if(courant.type==TokenType.AFFECT){
        consommer(TokenType.AFFECT,"affect attendu");
        analyserExpression();
        }
        consommer(TokenType.POINTV,"point virgule attendu");
    }
    private void analyserAffectation(){
    if (courant.type == TokenType.ID 
        || courant.type == TokenType.TACHFIN 
        || courant.type == TokenType.BALOUL) {
        avancer();
    } else {
        erreursSyntaxique("id attendu");
        if (courant.type != TokenType.EOF) {
            avancer();
        }
        return;
    }


    if (courant.type == TokenType.AFFECT) {
        consommer(TokenType.AFFECT,"affect attendu");
        analyserExpression();
        consommer(TokenType.POINTV,"point virgule attendu");

    } else if (courant.type == TokenType.INCR || courant.type == TokenType.DECR) {
        // Cas incrémentation / décrémentation : id++ ; ou id-- ;
        avancer(); // consomme ++ ou --
        consommer(TokenType.POINTV,"point virgule attendu");

    } else {
        erreursSyntaxique("Après un identificateur, '=' , '++' ou '--' attendu");
        // petite récupération : on saute jusqu'à ';' ou '}' ou EOF
        while(courant.type!=TokenType.POINTV &&
              courant.type!=TokenType.RACCO &&
              courant.type!=TokenType.EOF){
            avancer();
        }
        if (courant.type == TokenType.POINTV) {
            avancer();
        }
        enRecuperation = false;
    }
}

    private void analyserCondition(){
        analyserExpression();
        if (courant.type == TokenType.INF ||
        courant.type == TokenType.SUP ||
        courant.type == TokenType.INFEG ||
        courant.type == TokenType.SUPEG ||
        courant.type == TokenType.EGAL) {
        analyserOP();
        analyserExpression();
    }
   
    }
    private void analyserOP(){
        switch(courant.type){
        case INF:
        case SUP:
        case INFEG:
        case SUPEG:
        case EGAL:
            avancer();
            break;
        default : erreursSyntaxique("operateur de comparaison (<,<=,>,>=,==) attendu");
        if(courant.type!=TokenType.EOF){
        avancer();
        }
        break;
        }
    }
    private void analyserExpression(){
        switch(courant.type){
            case ID:
            case NOMBRE:
            case True:
            case False:
            case BALOUL:
            case TACHFIN:
            avancer();
        break;
            default: erreursSyntaxique("expression atendu identificateur , nombre ou booléen");
            if(courant.type!=TokenType.EOF){
            avancer();
            }
            break;
        }
    }
}
