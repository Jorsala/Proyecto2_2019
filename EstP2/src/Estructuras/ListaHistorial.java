/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras;

import java.io.Serializable;

/**
 *
 * @author gunner
 */
public class ListaHistorial implements Serializable {
  public NodoLista inicio = null;
   public NodoLista fin =null;
    public ListaHistorial(){} // constructor
    
    public void insertar(NodoLista nuevo) // metodo insertar
    {
        //  nuevo = new NodoLista();
          if(inicio == null)
          {
             inicio = nuevo;
             fin = nuevo;
          }else
          {
             fin.next = nuevo;
              nuevo.before = fin;
              fin = nuevo;
         
          }
    
    }
     

}


