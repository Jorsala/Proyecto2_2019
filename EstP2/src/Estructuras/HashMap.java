/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras;

/**
 *
 * @author gunner
 */
 class TablaHash {
      private int key;
      private NodoValor nodo;
      TablaHash(int key,  NodoValor nodo) {
            this.key = key;
            this.nodo = nodo;
      }     
 
      public int getKey() {
            return key;
      }
 
      public NodoValor getValue() {
            return nodo;
      }
}
public class HashMap {
      private final static int TABLE_SIZE = 7;
 
      TablaHash[] table;
 
     public HashMap() {
            table = new TablaHash[TABLE_SIZE];
            for (int i = 0; i < TABLE_SIZE; i++)
                  table[i] = null;
      }
 
      public NodoValor get(int key) {
            int hash = (key % TABLE_SIZE);
            while (table[hash] != null && table[hash].getKey() != key)
                  hash = (hash + 1) % TABLE_SIZE;
            if (table[hash] == null)
                  return null;
            else
                  return table[hash].getValue();
      }
 
      public void put(int key, NodoValor value) {
            int hash = (key % TABLE_SIZE);
            while (table[hash] != null && table[hash].getKey() != key)
                  hash = (hash + 1) % TABLE_SIZE;
            table[hash] = new TablaHash(key, value);
            
      }
}
