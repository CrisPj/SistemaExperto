package se.determinista.arbol;

public class Regla
{

    public static int CANTIDAD_REGISTROS = 5;
    public static int TAM_REGISTRO = 20;

    private byte llave;
    private String[] reglas;
    private String consequente;

    public Regla() {
    }

    public Regla(byte llave, String[] reglas, String consequente) {
        this.llave = llave;
        this.reglas = reglas;
        this.consequente = consequente;
    }

    public byte getLlave() {
        return llave;
    }

    public void setLlave(byte llave) {
        this.llave = llave;
    }

    public String[] getReglas() {
        return reglas;
    }

    public void setReglas(String[] reglas) {
        this.reglas = reglas;
    }

    public String getConsequente() {
        return consequente;
    }

    public void setConsequente(String consequente) {
        this.consequente = consequente;
    }

}
