package Hebb;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adrian
 */
public class GenerateFile {
    PrintWriter save;
    static Random los = new Random(); 
    int size;
    
    GenerateFile(int s){
        size = s;
        save();
    }
    
    void save(){
        try {
            save = new PrintWriter("file.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int j = 0; j < size; j++) {           
            save.print((los.nextInt(29)+1)+"  "+(los.nextInt(88)+1));
            save.println();
        }
        
        save.close();
    }
}
