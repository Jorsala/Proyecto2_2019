package Estructuras;

import java.io.Serializable;

public class IntegerChain extends Ordenable implements Serializable {

    private Integer mLlave = null;

    public IntegerChain(int pValor) {
        mLlave = new Integer(pValor);
    }

    public IntegerChain(Integer pValor) {
        mLlave = pValor;
    }

    public Object getKey() {
        return mLlave;
    }
    
    public boolean igualA(Ordenable pObjeto) {
        return mLlave.equals(pObjeto.getKey());
    }

    public boolean menorQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer)pObjeto.getKey()) < 0;
    }
    
    public boolean mayorQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer)pObjeto.getKey()) > 0;
    }
    
    public boolean menorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer)pObjeto.getKey()) <= 0;
    }
  
    public boolean mayorOIgualQue(Ordenable pObjeto) {
        return mLlave.compareTo((Integer)pObjeto.getKey()) >= 0;
    }
    
    public Ordenable minKey() {
        return new IntegerChain(Integer.MIN_VALUE);
    }

}
