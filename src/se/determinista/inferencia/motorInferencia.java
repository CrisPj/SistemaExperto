package se.determinista.inferencia;

import se.determinista.archivos.ArchivoMaestro;
import se.determinista.archivos.ArchivoHechos;
import se.determinista.arbol.Regla;

import java.util.ArrayList;
import java.util.Scanner;

public class motorInferencia
{

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private ArrayList<Byte> reglasAplicadas;
    private ArrayList<Byte> conjuntoConflicto;
    private String meta = null;

    public motorInferencia(ArchivoMaestro archivoMaestro, ArchivoHechos archivoHechos) {
        this.archivoMaestro = archivoMaestro;
        this.archivoMaestro.generarArbol();
        this.archivoHechos = archivoHechos;
        reglasAplicadas = new ArrayList<>();
        conjuntoConflicto = new ArrayList<>();
    }

    public void inicializar() {
        System.out.print("Ingrese la meta que deesea alcanzar, NONE para inferir sin meta específica, o TERMINAR para cancelar");
        meta = new Scanner(System.in).next();
        if (!meta.equals("TERMINAR"))
            encadenamientoHaciaDelante();
    }

    private void encadenamientoHaciaDelante() {
        conjuntoConflicto.add((byte) 1);
        while (!estaEnHechos(meta) && (conjuntoConflicto.size() > 0 && conjuntoConflicto != null)) {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            if (conjuntoConflicto != null && conjuntoConflicto.size() > 0) {
                byte idRegla = resolverConjuntoConflicto(conjuntoConflicto);
                aplicarRegla(idRegla);
            }
        }
        if (estaEnHechos(meta) && !meta.equals("NONE"))
            System.out.println("\nÉXITO\n");
        else {
            System.out.println("Estatus:\n");
            archivoHechos.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
    }

    private boolean estaEnHechos(String meta) {
        return archivoHechos.obtenerHechos().contains(meta);
    }

    private ArrayList<Byte> equiparar(ArchivoMaestro baseConocimiento, ArchivoHechos baseHechos) {
        ArrayList<Byte> idReglas = new ArrayList<>();
        ArrayList<Regla> reglas = baseConocimiento.mostrarTodasReglas();
        for (Regla regla : reglas) {
            String records[] = regla.getReglas();
            ArrayList<String> hechos = baseHechos.obtenerHechos();
            boolean bandera = true;
            for (int i = 0; i < records.length; i++) {
                String s = records[i].trim();
                if (!hechos.contains(records[i].trim()) && !s.isEmpty()) {
                    bandera = false;
                    i = records.length;
                }
            }
            if (bandera && !refraccionRegla(regla.getLlave()))
                idReglas.add(regla.getLlave());
        }
        return idReglas;
    }

    private boolean refraccionRegla(byte idRegla) {
        return reglasAplicadas.contains(idRegla);
    }


    private byte resolverConjuntoConflicto(ArrayList<Byte> idReglas) {
        byte regla = idReglas.get(0);
        for (byte ruleID : idReglas) {
            if (ruleID < regla)
                regla = ruleID;
        }
        return regla;
    }

    private void aplicarRegla(byte idRegla) {
        archivoHechos.insertarHecho(archivoMaestro.obtenerRegla(idRegla).getConsequente());
        reglasAplicadas.add(idRegla);
    }
}