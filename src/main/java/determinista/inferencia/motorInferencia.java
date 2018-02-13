package determinista.inferencia;

import determinista.arbol.Regla;
import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

//import static determinista.GUI.MakeTable.makeTable;

public class motorInferencia
{

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private ArrayList<Integer> reglasAplicadas;
    private ArrayList<Integer> conjuntoConflicto;
    private String meta = null;
    private ArrayList<ArrayList<ArrayList<String>>> listaMJ = new ArrayList<ArrayList<ArrayList<String>>>();

    public motorInferencia(ArchivoMaestro archivoMaestro, ArchivoHechos archivoHechos)
    {
        this.archivoMaestro = archivoMaestro;
        this.archivoMaestro.generarArbol();
        this.archivoHechos = archivoHechos;
        reglasAplicadas = new ArrayList<Integer>();
        conjuntoConflicto = new ArrayList<Integer>();
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
        ArrayList<ArrayList<String>> rowMJ = new ArrayList<ArrayList<String>>();
        ArrayList<String> campMJ = new ArrayList<String>();
        //
        conjuntoConflicto.add((int) 1);
        while (!estaEnHechos(meta) && (conjuntoConflicto.size() > 0 && conjuntoConflicto != null))
        {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            if (conjuntoConflicto != null && conjuntoConflicto.size() > 0)
            {
                Integer idRegla = resolverConjuntoConflicto(conjuntoConflicto);
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
            JOptionPane.showMessageDialog(null, "Éxito, meta alcanzada: " + meta +
                    ".\nPuedes ver módulo de justificación para más detalles.",
                    "EXITO", JOptionPane.INFORMATION_MESSAGE);
        else if (meta.equals(""))
            JOptionPane.showMessageDialog(null, "Se ha llegado a la meta: "
                    + archivoHechos.obtenerHechos().get(archivoHechos.obtenerHechos().size()-1)
                    + ".\nPuedes ver módulo de justificación para más detalles.",
                    "EXITO", JOptionPane.INFORMATION_MESSAGE);
        else
        {
            System.out.println("Estatus:\n");
            archivoHechos.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
    }

    private ArrayList<String> byteToStringList(ArrayList<Integer> array){
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
        //Se reducen bytes de meta:
        meta = meta.replace("\u0000", "");
        boolean verificado = false;
        if (estaEnHechos(meta))
            return true;
        else
        {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            while ((conjuntoConflicto != null && conjuntoConflicto.size() > 0) && !verificado)
            {
                Integer id = resolverConjuntoConflicto(conjuntoConflicto);
                // Obtener id del elemento a remover
                int idRM = -1;
                for (int i=0; i<conjuntoConflicto.size();i++){
                    if (conjuntoConflicto.get(i)==id){
                        idRM = i;
                    }
                }
                conjuntoConflicto.remove(idRM);
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

    private ArrayList<Integer> equiparar(ArchivoMaestro baseConocimiento, ArchivoHechos baseHechos)
    {
        ArrayList<Integer> idReglas = new ArrayList<>();
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

    private boolean refraccionRegla(int idRegla)
    {
        return reglasAplicadas.contains(idRegla);
    }


    private Integer resolverConjuntoConflicto(ArrayList<Integer> idReglas)
    {
        Integer regla = idReglas.get(0);
        for (Integer ruleID : idReglas)
            if (ruleID < regla)
                regla = ruleID;
        return regla;
    }

    private void aplicarRegla(int idRegla)
    {
        String aux = archivoMaestro.obtenerRegla(idRegla).getConsecuente();
        //aux = aux.replace("\u0000", "");
        aux = aux.trim();

        if (!estaEnHechos(aux))
            archivoHechos.insertarHecho(aux);
        reglasAplicadas.add(idRegla);
    }

    public ArrayList<ArrayList<ArrayList<String>>> getListaMJ(){
        return listaMJ;
    }

    public void showMJ(){
        String[] titulosMast = {"Ciclo","Base de hechos","Conjunto conflicto","Resolución"};
        String tituloMast = "Justificación";
 //       makeTable(titulosMast, getArrayDataMJ(listaMJ), tituloMast);
    }
    public String[][] getArrayDataMJ(ArrayList<ArrayList<ArrayList<String>>> lista){
        String[][] listaStr = new String[lista.size()][4];
        for (int i=0; i<lista.size(); i++){
            listaStr[i][0] = lista.get(i).get(0).get(0); //ciclo
            listaStr[i][1] = getListToStr(lista.get(i).get(1));
            listaStr[i][2] = getListToStr(lista.get(i).get(2));
            listaStr[i][3] = lista.get(i).get(3).get(0); //ciclo
        }
        return listaStr;
    }
    private String getListToStr(ArrayList<String> list){
        String BHstr = "{";
        for (int i=0; i<list.size()-1; i++){
            BHstr += list.get(i);
            BHstr += ", ";
        }
        BHstr += list.get(list.size()-1);
        BHstr += "}";
        return BHstr;
    }
}