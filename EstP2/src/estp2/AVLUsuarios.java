/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estp2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author George
 */
class nodoAVL {

    public String codigo;//Codigo del usuario
    public String nombre;

    public int altura = 0;
    public int fe;

    //Nodos
    public nodoAVL derecha;
    public nodoAVL izquiera;
    public nodoAVL usuario;

    //Constructores
    public nodoAVL() {
    }

    public nodoAVL(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
}

//Clase para manejar el arbol de usuarios
public class AVLUsuarios {

    public nodoAVL raiz;
    public nodoAVL us;
    public int cantidad = 0;

    public AVLUsuarios() {
    }

    //Agregar nodos al arbol
    public void Agregar(String codigo, String nombre) {
        nodoAVL nuevo = new nodoAVL(codigo, nombre);//El usuario no esta registrado
        if (raiz == null) {
            raiz = nuevo;
        } else {
            raiz = AgregarNodo(nuevo, raiz);
        }
        cantidad++;
    }

    public void Agregar(nodoAVL contacto) {
        nodoAVL nuevo = new nodoAVL();//El contacto es un usuario;
        nuevo.usuario = contacto;
        if (raiz == null) {
            raiz = nuevo;
        } else {
            raiz = AgregarNodo(nuevo, raiz);
        }
        cantidad++;
    }

    public nodoAVL AgregarNodo(nodoAVL nuevo, nodoAVL raiz) {
        if (raiz == null) {
            return nuevo;
        }

        String nickNuevo;
        String nickRaiz;
        if (nuevo.usuario == null) {
            nickNuevo = nuevo.codigo;
        } else {
            nickNuevo = nuevo.usuario.codigo;
        }
        if (raiz.usuario == null) {
            nickRaiz = raiz.codigo;
        } else {
            nickRaiz = raiz.usuario.codigo;
        }

        if (nickNuevo.compareTo(nickRaiz) < 0) {
            raiz.izquiera = AgregarNodo(nuevo, raiz.izquiera);
        } else if (nickNuevo.compareTo(nickRaiz) > 0) {
            raiz.derecha = AgregarNodo(nuevo, raiz.derecha);
        } else {
            System.out.println("el nodo ya existe");
        }

        Actualizar(raiz);
        return Balancear(raiz);
    }

    private void Actualizar(nodoAVL nodo) {
        int alturaIzquierda = (nodo.izquiera == null) ? -1 : nodo.izquiera.altura;
        int alturaDerecha = (nodo.derecha == null) ? -1 : nodo.derecha.altura;

        nodo.altura = 1 + Math.max(alturaIzquierda, alturaDerecha);
        nodo.fe = alturaDerecha - alturaIzquierda;
    }

    private nodoAVL Balancear(nodoAVL nodo) {
        if (nodo.fe == -2) {
            if (nodo.izquiera.fe <= 0) {
                return IzquierdaIzquierda(nodo);
            } else {
                return IzquierdaDerecha(nodo);
            }
        } else if (nodo.fe == 2) {
            if (nodo.derecha.fe >= 0) {
                return DerechaDerecha(nodo);
            } else {
                return DerechaIzquierda(nodo);
            }
        }

        return nodo;
    }

    //Rotaciones en el arbol
    private nodoAVL IzquierdaIzquierda(nodoAVL nodo) {
        System.out.println("se realiza rotacion II");
        return RotarDerecha(nodo);
    }

    private nodoAVL IzquierdaDerecha(nodoAVL nodo) {
        nodo.izquiera = RotarIzquierda(nodo.izquiera);
        System.out.println("se realizo rotacion ID");
        return IzquierdaIzquierda(nodo);
    }

    private nodoAVL DerechaDerecha(nodoAVL nodo) {
        System.out.println("se realizo rotacion DD");
        return RotarIzquierda(nodo);
    }

    private nodoAVL DerechaIzquierda(nodoAVL nodo) {
        nodo.derecha = RotarDerecha(nodo.derecha);
        System.out.println("se realizo rotacion DI");
        return DerechaDerecha(nodo);
    }

    private nodoAVL RotarIzquierda(nodoAVL nodo) {
        nodoAVL derecha = nodo.derecha;
        nodo.derecha = derecha.izquiera;
        derecha.izquiera = nodo;
        Actualizar(nodo);
        Actualizar(derecha);
        return derecha;
    }

    private nodoAVL RotarDerecha(nodoAVL nodo) {
        nodoAVL izquierda = nodo.izquiera;
        nodo.izquiera = izquierda.derecha;
        izquierda.derecha = nodo;
        Actualizar(nodo);
        Actualizar(izquierda);
        return izquierda;
    }

    //Buscar un nodo en el arbol AVL
    public nodoAVL Buscar(String codigo) {
        return Buscar(raiz, codigo);
    }

    private nodoAVL Buscar(nodoAVL nodo, String codigo) {
        if (nodo == null) {
            return null;
        } else {
            String nickNodo;
            if (nodo.usuario == null) {
                nickNodo = nodo.codigo;
            } else {
                nickNodo = nodo.usuario.codigo;
            }

            if (codigo.compareTo(nickNodo) < 0) {
                return Buscar(nodo.izquiera, codigo);
            }
            if (codigo.compareTo(nickNodo) > 0) {
                return Buscar(nodo.derecha, codigo);
            }
        }
        return nodo;
    }
    
    
    //Recorrido inorden del arbol
    public void recorridoIn(nodoAVL raiz) {
        if (raiz.izquiera != null) {
            recorridoIn(raiz.izquiera);
        }

        System.out.print("  " + raiz.codigo + "   ");

        if (raiz.derecha != null) {
            recorridoIn(raiz.derecha);
        }

    }
    
     String textoDot1;

    public String recorrerAVL(nodoAVL raiz) {
        String textoDOT = "";
        if (raiz != null) {

            if (raiz.izquiera != null) {

                String raizS = raiz.codigo;
                String faec = Integer.toString(raiz.fe);

//            stringstream ramasR;
//            ramasR<<raiz->izquierdo->id;
                String ramasS = raiz.izquiera.codigo;

                textoDOT = textoDOT + recorrerAVL(raiz.izquiera);
                //creacion de enelace
                textoDOT = textoDOT + "\"" + raizS + "\":f0->\"" + ramasS + "\":f1;";
            }
            String raizS = raiz.codigo;
            textoDOT = textoDOT + raizS + "[label=\"<f0>|<f1>" + "ID: " + raizS + "\\n Nombre: " + raiz.nombre   + "\\n factoe: " + Integer.toString(raiz.fe) + "|<f2>\"];\n";

            if (raiz.derecha != null) {
                String raizS1 = raiz.codigo;

                String ramasS = raiz.derecha.codigo;
                textoDOT = textoDOT + recorrerAVL(raiz.derecha);
                textoDOT = textoDOT + "\"" + raizS1 + "\":f2->\"" + ramasS + "\":f1;";
            }

        }
        textoDot1= textoDOT;
        //System.out.println(textoDOT);
        return textoDOT;
    }
    
    public void hacerGra() throws IOException, InterruptedException{
        recorrerAVL(raiz);
        graficarAVL(textoDot1);
    }

    public void graficarAVL(String textoDOT) throws IOException, InterruptedException {

        FileWriter fichero = null;
        PrintWriter fs = null;

        fichero = new FileWriter("./arbol.dot");
        fs = new PrintWriter(fichero);
        // ofstream fs("arbol.txt");

        fs.println("digraph G{\n");

        fs.println("\tsubgraph clusterLOG {\n");
        fs.println("label = \"USUARIOS\";\n");
        fs.println("nodesep=.05;");
        fs.println("node [shape=record,width=.1,height=.1];");  
        fs.println(textoDot1);
        fs.println("\t}\n");

        fs.println("}");
        
        

        fs.close();
        
        String path = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
        String fileInput= "./arbol.dot";
        String fileOutPut = System.getProperty("user.dir")+"\\arbol.png";
        String tParam = "-Tpng";
        String tOparam = "-o";
        
        String []cmd = new String[5];
        cmd[0] = path;
        cmd[1] = tParam;
        cmd[2] = fileInput;
        cmd[3]= tOparam;
        cmd[4]= fileOutPut;
        
        Runtime rt = Runtime.getRuntime();
        Process p =rt.exec(cmd);
        p.waitFor();
        p.destroy();
        

        
    }
    
    
}

