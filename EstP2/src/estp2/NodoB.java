package estp2;



import static com.sun.org.apache.xerces.internal.util.FeatureState.is;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import jdk.nashorn.internal.runtime.JSType;
import static jdk.nashorn.internal.runtime.JSType.isNumber;
import static sun.nio.cs.Surrogate.is;

/**
 *
 * @author George
 */


 class Historial {
        public Object key;
        public int x;
        public int y;
        public String unidadAtacante;
        public String resultado;
        public String tipoUnidadAtacada;
        public String nickEmisor;
        public String nickReceptor;
       

        public Historial siguiente;
        public Historial anterior;

        public Historial() {
        }

        public Historial(int x, int y, String unidadAtacante, String resultado, String tipoAtacada, String nickE, String nickR, int tiempo, int noAtaque) {
            this.x = x;
            this.y = y;
            this.unidadAtacante = unidadAtacante;
            this.resultado = resultado;
            tipoUnidadAtacada = tipoAtacada;
            nickEmisor = nickE;
            nickReceptor = nickR;
          
          //  this.tiempo = tiempo;
            //this.noAtaque = noAtaque;
        }

        public void SetKey(Object key) {
            this.key = key;
        }
    }



public class NodoB {
    
    public Object[] keys;
        public Historial[] values;
        public NodoB[] child;
        public int n;
        public boolean leaf;

        public NodoB() { }
        
        public NodoB (int t, boolean l) {
            keys = new Object[2 * t - 1];
            values = new Historial[2 * t - 1];
            child = new NodoB[2 * t];
            leaf = l;
            n = 0;
        }
        public void insertNonFull(Object key, Historial value, int t) {
            if (leaf) {
                int x = n;
                while (x > 0 && comparar(keys[x - 1], key, values[x - 1], value) > 0) {
                    keys[x] = keys[x - 1];
                    values[x] = values[x - 1];
                    x--;
                }

                keys[x] = key;
                values[x] = value;
                n++;
            } else {
                int x = 0;
                while (x < n && comparar(keys[x], key, values[x], value) < 0) {
                    x++;
                }
                try {
                    if (child[x].n == (2 * t - 1)) {//Rama llena
                        splitChild(x, this, t);
                        int i = x;
                        int m = this.child[x].n - 1;
                        //this.leaf = false;
                        if (comparar(this.child[x].keys[m], key, this.child[x].values[m], value) < 0)
                            i++;

                        this.child[i].insertNonFull(key, value, t);
                        //if(child[x].leaf == true)
                        //child[x + 1].leaf = true;
                    } else {
                        child[x].insertNonFull(key, value, t);
                    }
                } catch (Exception e){}
            }
        }

        public void splitChild(int ce, NodoB y, int t) {
            int mid = (y.child[ce].n / 2);
            int x = n;
            while (x > ce) {
                keys[x] = keys[x - 1];
                values[x] = values[x - 1];
                x--;
            }
            keys[x] = child[ce].keys[mid];
            values[x] = child[ce].values[mid];
            int tempChildN = child[ce].n;
            child[ce].n--;
            y.n++;
            NodoB nbtn = new NodoB(t, true);
            int i = 0;
            // int j = mid;
            while (mid < tempChildN - 1) {
                nbtn.child[i] = child[ce].child[++mid];
                nbtn.keys[i] = child[ce].keys[mid];
                nbtn.values[i] = child[ce].values[mid];
                i++;
                child[ce].n--;
            }
            nbtn.child[i] = child[ce].child[++mid];
            nbtn.n = i;
            y.child[ce + 1] = nbtn;
            if (child[ce].leaf != false) {
                nbtn.leaf = true;
            } else {
                nbtn.leaf = false;
            }
        }

        public int comparar(Object myKey, Object newKey, Historial myValue, Historial newValue) {
            int a;
            if (newKey instanceof String) {
                a = myKey.toString().compareTo(newKey.toString());
            } else if ( isNumber(newKey)) {
                a = Integer.parseInt(myKey.toString()) - Integer.parseInt(newKey.toString());
            } else {
                a = ((Comparable)myKey).compareTo(newKey);
            }

           // if(a == 0)
              //  return (myValue.noAtaque - newValue.noAtaque);
           // else
               return a;
        }
        
        
    
}

class BTree{
        public int t;
        public NodoB root;

        public BTree() { }

        public BTree (int t) {
            this.t = t;
            root = null;
        }

        public void insert(Object key, Historial value) {
            if (root == null) {
                root = new NodoB(t, true);
                root.keys[0] = key;
                root.values[0] = value;
                root.n++;
            }
            else {
                if (root.n == (2 * t - 1)) {// Raíz está llena
                    NodoB ch = new NodoB(t, false);
                    ch.child[0] = root;
                    root = ch;
                    ch.splitChild(0, root, t);
                    int i = 0;
                    int m = ch.child[0].n - 1;
                    if (comparar(ch.child[0].keys[m], key, ch.child[0].values[m], value) < 0)
                        i++;
                    ch.child[i].insertNonFull(key, value, t);
                    //ch.child[1].leaf = true;
                } else {
                    root.insertNonFull(key, value, t);
                }
            }
        }

        public int comparar(Object myKey, Object newKey, Historial myValue, Historial newValue) {
            int a;
            if (newKey instanceof String) {
                a = myKey.toString().compareTo(newKey.toString());
            } else if (isNumber(myKey) ) {
                a = Integer.parseInt(myKey.toString()) - Integer.parseInt(newKey.toString());
            } else {
                a = ((Comparable)myKey).compareTo(newKey);
            }

            //if (a == 0)
              //  return (myValue.noAtaque - newValue.noAtaque);
           // else
                return a;
        }
        
        
        String Coso="";
        public String scriptB(NodoB raiz) {
            
            StringBuilder script = new StringBuilder();
            
            if (raiz != null) {
              //  script.append(raiz.values[0].noAtaque + " [shape=none, margin=0, label=<\n");
                script.append("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n");
                script.append("<TR>\n");
                //CLAVES DEL NODO RAÍZ
                for (int i = 0; i < raiz.keys.length*2 + 1; i++) {
                    if (i % 2 != 0) {//impares son keys
                        if ((i-1)/2 < raiz.n) {
                            script.append("<TD>"
                                + "Key: " + raiz.keys[(i - 1) / 2]
                //                + "<BR/>" + raiz.values[(i - 1) / 2].noAtaque
                                + "<BR/>Pos: " + raiz.values[(i - 1) / 2].x + ", " + raiz.values[(i - 1) / 2].y
                                + "<BR/>Emisor: " + raiz.values[(i - 1) / 2].nickEmisor + " - " + raiz.values[(i - 1) / 2].unidadAtacante
                                + "<BR/>Receptor: " + raiz.values[(i - 1) / 2].nickReceptor + " - " + raiz.values[(i - 1) / 2].tipoUnidadAtacada
                                //+ "<BR/>" + raiz.values[(i - 1) / 2].fecha.ToShortDateString() + " - " + raiz.values[(i - 1) / 2].strTiempo
                                + "</TD>\n");
                        } else {
                            script.append("<TD>     </TD>\n");
                        }
                    } else {//#rama = i/2
                    //    script.append("<TD PORT=\"port" + raiz.values[0].noAtaque + "a" + i/2 + "\"> </TD>\n");
                    }
                }
                script.append("</TR>\n");
                script.append("</TABLE>>];\n");
                
                //-> RAMAS DEL NODO RAÍZ
                for (int i = 0; i < raiz.child.length; i++) {//RECORRER CADA CLAVE
                    if (raiz.child[i] != null) {
                  //      script.append(raiz.child[i].values[0].noAtaque + " [shape=none, margin=0, label=<\n");
                        script.append("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n");
                        script.append("<TR>\n");

                        //CLAVES DEL NODO RAMA
                        String port = "";
                        for (int j = 0; j < raiz.child[i].keys.length*2 + 1; j++) {
                            if (j % 2 != 0) {
                                if ((j-1)/2 < raiz.child[i].n) {
                                    script.append("<TD>"
                                    + "Key: " + raiz.child[i].keys[(j - 1) / 2]
                      //              + "<BR/>" + raiz.child[i].values[(j - 1) / 2].noAtaque
                                    + "<BR/>Pos: " + raiz.child[i].values[(j - 1) / 2].x + ", " + raiz.child[i].values[(j - 1) / 2].y
                                    + "<BR/>Emisor: " + raiz.child[i].values[(j - 1) / 2].nickEmisor + " - " + raiz.child[i].values[(j - 1) / 2].unidadAtacante
                                    + "<BR/>Receptor: " + raiz.child[i].values[(j - 1) / 2].nickReceptor + " - " + raiz.child[i].values[(j - 1) / 2].tipoUnidadAtacada
                                   // + "<BR/>" + raiz.child[i].values[(j - 1) / 2].fecha.ToShortDateString() + " - " + raiz.child[i].values[(j - 1) / 2].strTiempo
                                    + "</TD>\n");

             //                       if ((j-1)/2 == raiz.child[i].n / 2)
                        //                port = raiz.child[i].values[0].noAtaque + "a" + (j-1)/2;
               //                 } else {
                                    script.append("<TD>     </TD>\n");
                 //               }
                            } else {
                          //      script.append("<TD PORT=\"port" + raiz.child[i].values[0].noAtaque + "a" + j / 2 + "\"> </TD>\n");
                            }
                        }
                        script.append("</TR>\n");
                        script.append("</TABLE>>];\n");

                       // if (port == "") port = raiz.child[i].values[0].noAtaque + "a" + 0;

                   //     script.append(raiz.values[0].noAtaque + ":port" + raiz.values[0].noAtaque + "a" + i
                     //       + " -> " + raiz.child[i].values[0].noAtaque + ":port" + port + ";\n");
                    }
                }
                
                //for(int i = 0; i < raiz.child.length; i++) {
                    if(raiz.child[i] != null)
                        script.append(scriptB(raiz.child[i]));
                }

           // }else{
                System.out.println("el arbol b esta vacio");
            }
            Coso = script.toString();
            return script.toString();
        }
        
        
        //Metodo para hacer prueba de la grafica del arbol b
        
        public void graficarB() throws IOException, InterruptedException{
            
           scriptB(root);
            
            FileWriter fichero = null;
        PrintWriter fs = null;

        fichero = new FileWriter("./arbolB.dot");
        fs = new PrintWriter(fichero);
        // ofstream fs("arbol.txt");

        fs.println("digraph G{\n");

        fs.println("\tsubgraph clusterLOG {\n");
        fs.println("label = \"Arbol b\";\n");
        fs.println("nodesep=.05;");
        fs.println("node [shape=record,width=.1,height=.1];");
        fs.println(Coso);
        fs.println("\t}\n");

        fs.println("}");
        
        

        fs.close();
        
        String path = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
        String fileInput= "./arbolB.dot";
        String fileOutPut = System.getProperty("user.dir")+"\\arbolB.png";
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
        p.destroy();System.out.println(Coso);
            
        }
    }

