package Percepton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
  Rzut ukosny wersja 01 ~ Adrian
*/
public class Neuron {

    static final Scanner NULL = null; 
    static Random los = new Random(); 
    double sum = 0;
    double[] weights;      // tablica wag
    double[][] values;          //tablica wartości wejść NAUKA, V -> Alfa
    double [][] tests;         // tablica wartości danych TESTOWYCH
    double[] results;     // Wyniki wartości nauki
    double[] results2;     // Wyniki wartości testowych 
    double[] MSE_tab;
    int size = 0, distance = 0, iter = 0;
    double eta = 0.05;      //współczynik uczenia                 
    double exit = 1;         //wyjscie neuronu 
    double localError = 0;
    Scanner read;
    double zmienna = 0, theta = 1, g = 9.8;
    
    Neuron()
    {
        try {
            read = new Scanner(new File("file.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Blad odczytu pliku danych uczenia!");
        }
        distance = read.nextInt();          // odleglosc na ktora trafic trzeba
        size = read.nextInt();
        values = new double[2][size];       // odczyt z pliku, pierwsze zmienna ilosc przykladow
        weights = new double[3];            // ilosc wejsc + 1
        results = new double[size];
        MSE_tab = new double[1000];
        randomWeights();
        readFile();
    }
    
    void randomWeights()
    {
        for(int i=0;i<3;i++)
            weights[i] = (los.nextDouble() * 2) - 1;
    }
    
    void readFile()
    {
        int tmp=0;
        double y=0;
        while(read.hasNextDouble()) {               // odczyt z pliku, po 2 wartosci, V, Alfa
            values[0][tmp]=read.nextDouble();
            values[1][tmp]=read.nextDouble();
            y = (values[0][tmp]*values[0][tmp])/g*Math.sin(Math.toRadians(2*values[1][tmp]));
            if (y>=distance){
                results[tmp] = 1;
            }
            else{results[tmp] = 0;
            }
            tmp++;
        }
    }
    
    double count(double a, double b)
    {
        double suma = 0;
        suma = a * weights[0] + b * weights[1] + weights[2]; 
        if(suma >= theta)   //Funkcja progowa unipolarna
            suma = 1;
        else 
            suma = 0;
        return suma;
    }
    
    void learn()
    {
        double globalError, y, MSE;
        do
        {
            globalError=0;
            
            for(int i=0; i<size; i++) 
            {
                y = count(values[0][i],values[1][i]); 
                localError = results[i]-(int)y;
                weights[0] += eta*localError*values[0][i];
                weights[1] += eta*localError*values[1][i];
                weights[2] += eta*localError;
                globalError = globalError + (localError*localError);
                //System.out.println(values[0][i] +"   "+values[1][i] +"  |  "+ results[i]+"  => "+y );
            }
            MSE = globalError*globalError/size;
            MSE_tab[iter] = MSE;
            iter++;
            System.out.println("Iteracja: "+iter+ " MSE = "+MSE);
        }
        while(iter<1000);        // *********************************************************************
        
        System.out.print("Nauczony! Liczba iteracji: "+iter+"   Wagi: ");
        for (int j = 0; j < 3; j++) {
            System.out.print(weights[j]+"  |  ");
        }
        System.out.println();
        test();
    }
    
    double think(double a, double b){
        double tmp, y;
        tmp = a * weights[0] + b * weights[1] + weights[2];
        if(tmp>=1){
            y = 1;
        }
        else{
            y = 0;
        }
        return y;
    }
    
    void test() {
        try {
            read = new Scanner(new File("test.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Blad w odczycie piku testowego!");
        }
        size = read.nextInt();
        tests = new double[2][size];
        results2 = new double[size];
        int tmp = 0; 
        double x;
        while(read.hasNextDouble()) {
            tests[0][tmp]=read.nextDouble();
            tests[1][tmp]=read.nextDouble();
            x = (tests[0][tmp]*tests[0][tmp])/g*Math.sin(Math.toRadians(2*tests[1][tmp]));
            if (x>=distance){
                results2[tmp] = 1;
            }
            else{results2[tmp] = 0;
            }
            tmp++;
        }
        int stan=0;
        for(int i=0;i<size;i++) {
            double y = think(tests[0][i],tests[1][i]);
            if (y == results2[i]) {
                stan++;
            }
            //System.out.println(tests[0][i]+"   "+tests[1][i]+"   "+results2[i]+"   => "+ y);
        }
        System.out.println("Liczba iteracji: "+iter+"   Poprawnie: "+stan+" / "+size);
        save(stan, size);
}
    
    void save(int stan, int siz){
        PrintWriter sav = null;
        try {
            sav = new PrintWriter("MSE.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
        }
        sav.println(stan);
        sav.println(siz);
        for (int i = 0; i < 1000; i++) {
            sav.println(MSE_tab[i]);
        }
        sav.close();
    }
    
    public static void main(String[] args) {
        Neuron neuron = new Neuron();
        neuron.learn();
        //GenerateFile p = new GenerateFile(5000);
        //double s = (10*10)/2*0.1*10;    // wzor   V, g, f
    }
    
}
