/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estp2;

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
    }
    
}
