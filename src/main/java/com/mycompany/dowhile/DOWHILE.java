package com.mycompany.dowhile;

import java.util.List;
import java.util.Scanner;

public class DOWHILE {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while(true){
        System.out.println("entrez votre code ");
        StringBuilder sb=new StringBuilder();
        String line;
        while(!(line=sc.nextLine()).isEmpty()){
            if(line.equalsIgnoreCase("exit")){
            System.out.println("Fermeture du compilateur...");
            return;
            }
        sb.append(line).append("\n");
        }
        String code=sb.toString();
        AnalyseurLexDOWHILE lex=new AnalyseurLexDOWHILE(code);
        AnalyseurSyntaxiqueDOWHILE syn=new AnalyseurSyntaxiqueDOWHILE(lex);
        syn.analserProgramme();
        List<String> erreursLex=lex.getErreur();
        List<String> erreursSyn =syn.getErreurSyn();
        if(erreursLex.isEmpty()&&erreursSyn.isEmpty()){
        System.out.println("code ecrit avec succes");
        }else{
        if(!erreursLex.isEmpty()){
            System.out.println("\nErreurs lexicals :");
            for(String e:erreursLex){
                System.out.println(" - "+e);
            }
        }
        if(!erreursSyn.isEmpty()){
            System.out.println("\nErreurs syntaxique :");
        for(String e:erreursSyn){
        System.out.println(" - "+e);
        }
        }
        }
        System.out.println("Analyse finis");
       
        }

       //sc.close(); 
    }
    
}