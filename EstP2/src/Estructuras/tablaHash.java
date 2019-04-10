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
public class tablaHash implements Serializable {

    private int creator;
    private String publisher ="";
    private int tablaSize;
    private double soporteTabla;
    int elementos;
     private NodoValor tabla[];
    public tablaHash(int size) {
        ;
        this.soporteTabla = 0.0;
        this.creator = 0;
        this.publisher = "";
        this.tablaSize = size;
        this.tabla = new NodoValor[size];
        this.elementos = 0;
    }

 public int converse(String idUser) {
       
        int sumaCaracter = 0;
        int arregloSuma[] = new int[10];
        for (int i = 0; i < idUser.length(); i++) {
             sumaCaracter += (int) idUser.charAt(i);
        }
        String car = Integer.toString(sumaCaracter);
        int numero = sumaCaracter;
        int i = car.length() - 1;
        while (numero > 0) {
            arregloSuma[i] = numero % 10;
            numero /= 10;
            i--;
        }

        int pos = 0;
        String primer = "";
        String segun = "";
        for (int number = 0; number < 5; number++) {
            primer += arregloSuma[number];
        }
        for (int ke = 9; ke > 4; ke--) {
            segun += arregloSuma[ke];
        }
    
        pos = Integer.parseInt(primer) + Integer.parseInt(segun);
        pos = pos % getTablaSize();
        
        return pos;
    }

    
    private int address(String idUser) {
        int i = 1;
        int posicion;
        posicion = converse(idUser);
        int auxPosicion = posicion;
        while (getTabla()[auxPosicion] != null && i < 6) {
          
            if (getTabla()[auxPosicion].userName.equals(idUser)) {
                return -2;
            }
            auxPosicion = posicion + (i * i);
            auxPosicion = auxPosicion % getTablaSize();
            i++;
        }
        posicion = auxPosicion;
        if (i == 6 && getTabla()[posicion] != null) {
            return -1;
        }

        return posicion;
    }
public boolean buscar(String idUser, String pass) {
        NodoValor nodo;

        nodo = directioner(idUser, pass);
        
        if (nodo != null) {
           //valido
            return true;
        } else {
           //invalido :/
            return false;
        }
    }
    
    private NodoValor directioner(String idUser, String pass) {
        int i = 1;
        int pos;
        NodoValor nodoRetorno = null;
        pos = converse(idUser);
        int auxiliarPos = pos;
        while (getTabla()[auxiliarPos] != null && i < 6) {
           
            if (getTabla()[auxiliarPos].userName.equals(idUser) && getTabla()[auxiliarPos].pass.equals(pass)) {
                return getTabla()[auxiliarPos];
            }
            auxiliarPos = pos + (i * i);
            auxiliarPos = auxiliarPos % getTablaSize();
            i++;
        }
        if (i == 6) {
            nodoRetorno = null;
        }
        return nodoRetorno;

    }

public NodoValor usuario(String iduser, String pass){
    return directioner(iduser,pass);
}

 public void displayTabla() {
        for (int i = 0; i < tablaSize; i++) {
            if (getTabla()[i] != null) {
                System.out.println("Usuario: " + getTabla()[i].userName + " Contraseña: " + getTabla()[i].pass + " Correo: " + getTabla()[i].email);
            } else {
                System.out.println("Espacio vacío");
            }
        }
    }
    
    public void reHash() {
        int nuevoTamaño = getTablaSize();
        boolean siguiente_primo = false;
        boolean cos = true;
        int contador = 2;
        while (siguiente_primo == false) {
            nuevoTamaño++;
            contador = 2;
            cos = true;
            while (cos == true && contador != nuevoTamaño) {
                if (nuevoTamaño % contador == 0) {
                    cos = false;
                }
                contador++;
            }
            if (cos == true) {
              
                siguiente_primo = true;
            }
        }
        tablaHash nuevaTablaHash = new tablaHash(nuevoTamaño);
     
        for (int i = 0; i < tablaSize; i++) {
            if (getTabla()[i] != null) {
                nuevaTablaHash.put(getTabla()[i].userName, getTabla()[i].pass, getTabla()[i].email,getTabla()[i].pathI);
            }
        }

        setTablaSize(nuevoTamaño);
        setTabla(nuevaTablaHash.getTabla());
   
    }

    public void put(String idUser, String pass, String email, String path) {
        int pos = 0;
        pos = address(idUser);

        switch (pos) {
            case -1:
                System.out.println("no hay usuario");
                break;
            case -2:
                System.out.println("user name exist");
                break;
            default:
                NodoValor nuevo = new NodoValor();
                nuevo.userName = idUser;
                nuevo.pass = pass;
                nuevo.email = email;
                nuevo.pathI = path;
                //nuevo.siguiente = null;
                getTabla()[pos] = nuevo;
                elementos++;
                setSoporteTabla((double) elementos / getTablaSize());
                if (getSoporteTabla() > 0.75) {
                    reHash();
                }   System.out.println("");
                break;
        }
    }

    /**
  
     */
    public int getTablaSize() {
        return tablaSize;
    }

    /**

     */
    public void setTablaSize(int size) {
        this.tablaSize = size;
    }

    
    public double getSoporteTabla() {
        return soporteTabla;
    }


    public void setSoporteTabla(double soporte) {
        this.soporteTabla = soporte;
    }

    /**
     * @return the tabla
     */
    public NodoValor[] getTabla() {
        return tabla;
    }

    /**
     * @param tabla the tabla to set
     */
    public void setTabla(NodoValor[] tabla) {
        this.tabla = tabla;
    }

}
