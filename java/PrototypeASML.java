/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.io.File;
import java.io.IOException;
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
        String dirPath = "C:\\Users\\Pepefab\\Documents\\NetBeansProjects\\PROJET_M1_SelEtPC\\asml\\";
        File dir = new File(dirPath);
        String[] files = dir.list();
        for (String aFile : files) {
            if(aFile.endsWith(".asml")){
                String data = new String(Files.readAllBytes(Paths.get(dirPath + aFile)));
                ASMLArbre a = new ASMLArbre(data);
                a.registerAllocation_Spill();
                System.out.println(a);
            }
        }    
    }
    
}
