/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author lefebfab
 */
public class PrototypeASML {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //String dirPath = "/home/l/lefebfab/Cours/Master/Projet/PROJET_M1_SelEtPC/asml/";
            String nomFichier = null;
            String[] pathTab;
            String[] nameTab;
            String nomFichierSansMl = null;
            String nomFichierAvecS = nomFichierSansMl + ".s";
            String dirPath = args[0];
            File dir = new File(dirPath);
            String[] files = dir.list();
            String data = new String(Files.readAllBytes(Paths.get(dirPath)));
            ASMLArbre a = new ASMLArbre(data);
            a.registerAllocation_Spill();
            //System.out.println(a);
            System.out.println(a.genererAssembleur());
            PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(nomFichierAvecS)));
            w.print(a.genererAssembleur());
            w.close();
    }
}    
    
    
