package determinista.inferencia;

import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;
import determinista.modelos.Regla;

import java.util.ArrayList;

public class motorInferencia
{

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private ArrayList<Integer> reglasAplicadas;
    private ArrayList<Integer> conjuntoConflicto;
    private String meta = null;
    private ArrayList<ArrayList<ArrayList<String>>> listaMJ = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<Regla> justs = new ArrayList<>();

    public motorInferencia(ArchivoMaestro archivoMaestro, ArchivoHechos archivoHechos)
    {
        this.archivoMaestro = archivoMaestro;
        this.archivoMaestro.generarArbol();
        this.archivoHechos = archivoHechos;

        reglasAplicadas = new ArrayList<Integer>();
        conjuntoConflicto = new ArrayList<Integer>();
        justs.clear();
    }

    public ArrayList<Regla> justificacion()
    {
        return justs;
    }

    public void inicializar(boolean opcion, String metita)
    {
        meta = metita;
        System.out.print("Ingrese la meta que se desea alcanzar o deje vacio para inferir sin meta.");
        if (!meta.equals("TERMINAR"))
            if (opcion)
                encadenamientoHaciaDelante();
            else neuronal();
    }

    private void neuronal() {

    }

    private void encadenamientoHaciaDelante()
    {
        //
        if (archivoHechos.obtenerHechos().size()==0)
            return;
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
                justs.add(archivoMaestro.obtenerRegla(idRegla));

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
            System.out.println("Éxito, meta alcanzada: "+ meta +
                            ".\nPuedes ver módulo de justificación para más detalles.");
        else if (meta.equals(""))
            System.out.println( "Se ha llegado a la meta: "
                    + archivoHechos.obtenerHechos().get(archivoHechos.obtenerHechos().size()-1)
                    + ".\nPuedes ver módulo de justificación para más detalles.");
        else
        {
            System.out.println("Estatus:\n");
            archivoHechos.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
    }

    private ArrayList<String> byteToStringList(ArrayList<Integer> array){
        ArrayList<String> arrayStr = new ArrayList<String>();
        for (Integer anArray : array) {
            arrayStr.add(anArray.toString());
        }
        return arrayStr;
    }


    private boolean estaEnHechos(String meta)
    {
        return archivoHechos.obtenerHechos().contains(meta);
    }

    private ArrayList<Integer> equiparar2(ArchivoMaestro baseConocimiento, String m)
    {
        ArrayList<Integer> idReglas = new ArrayList<>();
        ArrayList<Regla> reglas = baseConocimiento.imprimirReglas();
        for (Regla regla : reglas) {
                if (m.equals(regla.getConsecuente())) {
                    if (!refraccionRegla(regla.getLlave())) {
                        Integer llave = regla.getLlave();
                        idReglas.add(llave);
                    }
                }
        }
//        hechos.clear();
  //      idReglas.stream().map(idRegla -> Arrays.asList(reglas.get(idRegla).getReglas())).forEach(hechos::addAll);
        return idReglas;
    }


    private ArrayList<Integer> equiparar(ArchivoMaestro baseConocimiento, ArchivoHechos baseHechos)
    {
        ArrayList<Integer> idReglas = new ArrayList<>();
        ArrayList<Regla> reglas = baseConocimiento.imprimirReglas();
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
        aux = aux.trim();

        if (!estaEnHechos(aux))
            archivoHechos.insertarHecho(aux);
        reglasAplicadas.add(idRegla);
    }

}