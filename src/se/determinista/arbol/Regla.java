package se.determinista.arbol;

public class Regla
{

    public static int CANTIDAD_REGISTROS = 5;
    public static int TAM_REGISTRO = 20;

    private byte llave;
    private String[] reglas;
    private String consecuente;

    public Regla() {
    }

    public Regla(byte llave, String[] reglas, String consecuente) {
        this.llave = llave;
        this.reglas = reglas;
        this.consecuente = consecuente;
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

    public String getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(String consecuente) {
        this.consecuente = consecuente;
    }

}
