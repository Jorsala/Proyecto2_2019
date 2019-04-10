package Estructuras;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 *
 * @author gunner
 */
public class ArbolB implements Serializable {

    public Nodo mRaiz = null;

    private int mAltura = 0;
    private int mArbol = 2;
    String textoB = "";

    public String hacerD() {
        StringBuilder b = new StringBuilder();

        b.append("digraph g { \n node [shape=record];\n");

        b.append(mRaiz.toDot());

        b.append("}");

        System.out.println(b);
        textoB = b.toString();
        return b.toString();
    }

    public ArbolB() {
    }

    public ArbolB(int k) {
        this.mArbol = k;
    }

    public ArbolB(Nodo pRaiz) {
        mArbol = pRaiz.getK();
        this.mRaiz = pRaiz;
        mAltura = 1;
    }

    public void insert(Ordenable key, Object obj) {
        if (this.mAltura == 0) {
            this.mRaiz = new Nodo(this.mArbol, key, obj);
            System.out.println("se inserto nodo");
            this.mAltura = 1;
            return;
        }

        Separar splitted = insert(this.mRaiz, key, obj, 1);

        if (splitted == null) {
        } else {

            Nodo ptr = this.mRaiz;

            this.mRaiz
                    = new Nodo(this.mArbol, splitted.getLlave(), splitted.getDato());
            this.mRaiz.mPunteros[0] = ptr;
            this.mRaiz.mPunteros[1] = splitted.getPuntero();
            this.mAltura++;
        }
    }

    protected Separar insert(Nodo node, Ordenable key, Object obj, int level) {

        Separar splitted = null;
        Nodo ptr = null;

        int i = 0;
        while ((i < node.mB) && (key.mayorQue(node.mLlaves[i]))) {
            i++;
        }

        if ((i < node.mB) && key.igualA(node.mLlaves[i])) {
            node.mDatos[i] = obj;
            return null;
        }

        if (level < this.mAltura) {

            splitted = insert(node.mPunteros[i], key, obj, level + 1);

            if (splitted == null) {
                return null;
            } else {
                key = splitted.mLlave;
                obj = splitted.mDato;
                ptr = splitted.mPuntero;
            }
        }

        i = node.mB - 1;
        while ((i >= 0)
                && (node.mLlaves[i] == null || key.menorQue(node.mLlaves[i]))) {
            node.mLlaves[i + 1] = node.mLlaves[i];
            node.mDatos[i + 1] = node.mDatos[i];
            node.mPunteros[i + 2] = node.mPunteros[i + 1];
            i--;
        }

        node.mLlaves[i + 1] = key;
        node.mDatos[i + 1] = obj;
        if (splitted != null) {
            node.mPunteros[i + 2] = splitted.mPuntero;
        }
        node.mB++;

        if (node.mB > 2 * mArbol) {

            Nodo newnode = new Nodo(this.mArbol);
            newnode.mPunteros[this.mArbol] = node.mPunteros[node.mB];
            node.mPunteros[node.mB] = null;
            node.mB = this.mArbol + 1;
            for (i = 0; i < this.mArbol; i++) {
                newnode.mLlaves[i] = node.mLlaves[i + node.mB];
                node.mLlaves[i + node.mB] = null;
                newnode.mDatos[i] = node.mDatos[i + node.mB];
                node.mDatos[i + node.mB] = null;
                newnode.mPunteros[i] = node.mPunteros[i + node.mB];
                node.mPunteros[i + node.mB] = null;
            }
            node.mB--;

            splitted
                    = new Separar(newnode, node.mLlaves[node.mB], node.mDatos[node.mB]);
            node.mLlaves[node.mB] = null;
            node.mDatos[node.mB] = null;
            newnode.mB = this.mArbol;
            node.mB = this.mArbol;

            return splitted;
        }

        return null;
    }

    public Object search(Ordenable key) {
        return search(key, mRaiz);
    }

    public Object search(Ordenable key, Nodo node) {

        if ((node == null) || (node.mB < 1)) {
            System.err.println("error");
            return null;
        }

        if (key.menorQue(node.mLlaves[0])) {
            return search(key, node.mPunteros[0]);
        }

        if (key.mayorQue(node.mLlaves[node.mB - 1])) {
            return search(key, node.mPunteros[node.mB]);
        }

        int i = 0;
        while ((i < node.mB - 1) && (key.mayorQue(node.mLlaves[i]))) {
            i++;
        }

        if (key.igualA(node.mLlaves[i])) {
            return node.mDatos[i];
        }

        return search(key, node.mPunteros[i]);

    }

    public int getAltura() {
        return mAltura;
    }
    
    //para generar la grafica del arbol B
    public void graficarB(String texto) throws IOException, InterruptedException {
        FileWriter fichero = null;
        PrintWriter fs = null;

        fichero = new FileWriter("./arbolB.dot");
        fs = new PrintWriter(fichero);
        fs.println(texto);
        fs.close();

        String path = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
        String fileInput = "./arbolB.dot";
        String fileOutPut = System.getProperty("user.dir") + "\\arbolB.png";
        String tParam = "-Tpng";
        String tOparam = "-o";

        String[] cmd = new String[5];
        cmd[0] = path;
        cmd[1] = tParam;
        cmd[2] = fileInput;
        cmd[3] = tOparam;
        cmd[4] = fileOutPut;

        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(cmd);
        p.waitFor();
        p.destroy();

    }
    
    public void hacerGraB() throws IOException, InterruptedException{
        hacerD();
        graficarB(textoB);
    }
}

//Clase IntegerChain
class IntegerChain1 extends Ordenable implements Serializable {

    private Integer mLlave = null;

    public IntegerChain1(int pValor) {
        mLlave = new Integer(pValor);
    }

    public IntegerChain1(Integer pValor) {
        mLlave = pValor;
    }

    public Object getKey() {
        return mLlave;
    }

    public boolean igualA(Ordenable pObjeto) {
        return mLlave.equals(pObjeto.getKey());
    }

    public boolean menorQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer) pObjeto.getKey()) < 0;
    }

    public boolean mayorQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer) pObjeto.getKey()) > 0;
    }

    public boolean menorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer) pObjeto.getKey()) <= 0;
    }

    public boolean mayorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer) pObjeto.getKey()) >= 0;
    }

    public Ordenable minKey() {
        return new IntegerChain(Integer.MIN_VALUE);
    }

    

}
