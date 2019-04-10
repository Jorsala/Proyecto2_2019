package Estructuras;

import java.io.Serializable;

public class KeyChain extends Ordenable implements Serializable {
    
    public KeyChain(String pValor) {
        mLlave = pValor;
    }    
    private String mLlave = null;    

    public Object getKey() {
        return mLlave;
    }
    
    public boolean igualA(Ordenable pObjeto) {
        return mLlave.equals(pObjeto.getKey());
    }

    public boolean menorQue(Ordenable pObjeto) {
        return mLlave.compareTo((String)pObjeto.getKey()) < 0;
    }
    
    public boolean mayorQue(Ordenable pObjeto) {
        return mLlave.compareTo((String)pObjeto.getKey()) > 0;
    }
    
    public boolean menorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((String)pObjeto.getKey()) <= 0;
    }
    
    public boolean mayorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((String)pObjeto.getKey()) >= 0;
    }
    
    public Ordenable minKey() {
        return new KeyChain("");
    }

}
