package se.determinista.inferencia;

import se.determinista.arbol.Regla;
import se.determinista.archivos.ArchivoHechos;
import se.determinista.archivos.ArchivoMaestro;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class motorInferencia
{

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private ArrayList<Byte> reglasAplicadas;
    private ArrayList<Byte> conjuntoConflicto;
    private String meta = null;
    ArrayList<ArrayList<ArrayList<String>>> listaMJ;

    public motorInferencia(ArchivoMaestro archivoMaestro, ArchivoHechos archivoHechos)
    {
        this.archivoMaestro = archivoMaestro;
        this.archivoMaestro.generarArbol();
        this.archivoHechos = archivoHechos;
        reglasAplicadas = new ArrayList<>();
        conjuntoConflicto = new ArrayList<>();
        this.listaMJ = new ArrayList<ArrayList<ArrayList<String>>>();
    }

    public void justificacion()
    {

    }

    public void inicializar(boolean opcion, String metita)
    {
        meta = metita;
        System.out.print("Ingrese la meta que se desea alcanzar o deje vacio para inferir sin meta.");
        if (!meta.equals("TERMINAR"))
            if (opcion)
                encadenamientoHaciaDelante();
            else encadenamientoHaciaAtras();
    }

    private void encadenamientoHaciaDelante()
    {
        //
        int ciclo = 1;
        ArrayList<ArrayList<ArrayList<String>>> listaMJ = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> rowMJ = new ArrayList<ArrayList<String>>();
        ArrayList<String> campMJ = new ArrayList<String>();
        //
        conjuntoConflicto.add((byte) 1);
        while (!estaEnHechos(meta) && (conjuntoConflicto.size() > 0 && conjuntoConflicto != null))
        {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            if (conjuntoConflicto != null && conjuntoConflicto.size() > 0)
            {
                byte idRegla = resolverConjuntoConflicto(conjuntoConflicto);
                //
                campMJ.add(Integer.toString(ciclo++));
                rowMJ.add(campMJ);
                campMJ = new ArrayList<String>();               //ciclo
                rowMJ.add(archivoHechos.obtenerHechos());       //hechos
                rowMJ.add(byteToStringList(conjuntoConflicto)); //conjunto conflicto
                campMJ.add(Integer.toString(idRegla));
                rowMJ.add(campMJ);
                campMJ = new ArrayList<String>();               //solucion
                listaMJ.add(rowMJ);
                rowMJ = new ArrayList<ArrayList<String>>();     //add lista
                //
                aplicarRegla(idRegla);
            }
        }
        if (estaEnHechos(meta) && !meta.equals(""))
            JOptionPane.showMessageDialog(null, "Exito, meta alcanzada; " + meta, "EXITO", JOptionPane.INFORMATION_MESSAGE);
        else if (meta.equals(""))
            JOptionPane.showMessageDialog(null, "Exito, meta alcanzada: " + archivoHechos.obtenerHechos().get(archivoHechos.obtenerHechos().size()-1), "EXITO", JOptionPane.INFORMATION_MESSAGE);
        else
        {
            System.out.println("Estatus:\n");
            archivoHechos.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
    }

    private ArrayList<String> byteToStringList(ArrayList<Byte> array){
        ArrayList<String> arrayStr = new ArrayList<String>();
        for (int i=0; i<array.size(); i++){
            arrayStr.add(array.get(i).toString());
        }
        return arrayStr;
    }

    private void encadenamientoHaciaAtras()
    {
        System.out.println(Verificar(archivoHechos, meta) ? "EXITO" : "FALLO");
    }

    private boolean Verificar(ArchivoHechos archivoHechos, String meta)
    {
        boolean verificado = false;
        if (estaEnHechos(meta))
            return true;
        else
        {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            while ((conjuntoConflicto != null && conjuntoConflicto.size() > 0) && !verificado)
            {
                byte id = resolverConjuntoConflicto(conjuntoConflicto);
                conjuntoConflicto.remove(id);
                ArrayDeque<String> nuevasMetas = new ArrayDeque<>();
                nuevasMetas.add(archivoMaestro.obtenerRegla(id).getConsecuente());
                verificado = true;
                while (!nuevasMetas.isEmpty() && verificado)
                {
                    String Meta = nuevasMetas.pop();
                    verificado = Verificar(archivoHechos, Meta);
                    if (verificado)
                    {
                        aplicarRegla(id);
                    }
                }
            }
        }
        return verificado;
    }

    private boolean estaEnHechos(String meta)
    {
        return archivoHechos.obtenerHechos().contains(meta);
    }

    private ArrayList<Byte> equiparar(ArchivoMaestro baseConocimiento, ArchivoHechos baseHechos)
    {
        ArrayList<Byte> idReglas = new ArrayList<>();
        ArrayList<Regla> reglas = baseConocimiento.mostrarTodasReglas();
        for (Regla regla : reglas)
        {
            String records[] = regla.getReglas();
            ArrayList<String> hechos = baseHechos.obtenerHechos();
            boolean bandera = true;
            for (int i = 0; i < records.length; i++)
            {
                String s = records[i].trim();
                if (!hechos.contains(records[i].trim()) && !s.isEmpty())
                {
                    bandera = false;
                    i = records.length;
                }
            }
            if (bandera && !refraccionRegla(regla.getLlave()))
                idReglas.add(regla.getLlave());
        }
        return idReglas;
    }

    private boolean refraccionRegla(byte idRegla)
    {
        return reglasAplicadas.contains(idRegla);
    }


    private byte resolverConjuntoConflicto(ArrayList<Byte> idReglas)
    {
        byte regla = idReglas.get(0);
        for (byte ruleID : idReglas)
            if (ruleID < regla)
                regla = ruleID;
        return regla;
    }

    private void aplicarRegla(byte idRegla)
    {
        String aux = archivoMaestro.obtenerRegla(idRegla).getConsecuente();
        aux = aux.replace("\u0000", "");
        if (!estaEnHechos(aux))
            archivoHechos.insertarHecho(aux);
        reglasAplicadas.add(idRegla);
    }

    public ArrayList<ArrayList<ArrayList<String>>> getListaMJ(){
        return listaMJ;
    }
}