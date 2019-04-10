/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estp2;

import Estructuras.*;
import java.io.IOException;

/**
 *
 * @author George
 */
public class EstP2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
       
        //Para AVL
        AVLUsuarios av =  new AVLUsuarios();
        av.Agregar("AX001", "Juan");
        av.Agregar("AX101", "Juan");
        av.Agregar("AX000", "Juan");
        av.Agregar("AX201", "Juan");
        av.Agregar("AX102", "Juan");
        av.Agregar("AX141", "Juan");
        av.recorridoIn(av.raiz);
        av.Eliminar("AX102");
        System.out.println(" ");
        av.recorridoIn(av.raiz);
        av.hacerGra();
        
        ArbolB aB = new ArbolB();
        aB.insert(new IntegerChain(1), "hola");
        aB.insert(new IntegerChain(2), "hola2");
        aB.insert(new IntegerChain(3), "hola");
        aB.insert(new IntegerChain(4), "hola2");
        aB.insert(new IntegerChain(11), "hola");
        aB.insert(new IntegerChain(12), "hola2");
        aB.insert(new IntegerChain(13), "hola");
        aB.insert(new IntegerChain(21), "hola2");
        aB.hacerGraB();
        
    }
    
}
