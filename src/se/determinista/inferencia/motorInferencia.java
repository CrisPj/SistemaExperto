package se.determinista.inferencia;

import javafx.beans.property.adapter.JavaBeanObjectProperty;
import se.determinista.archivos.ArchivoMaestro;
import se.determinista.archivos.ArchivoHechos;
import se.determinista.arbol.Regla;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class motorInferencia
{

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private ArrayList<Byte> reglasAplicadas;
    private ArrayList<Byte> conjuntoConflicto;
    private String meta = null;

    public motorInferencia(ArchivoMaestro archivoMaestro, ArchivoHechos archivoHechos)
    {
        this.archivoMaestro = archivoMaestro;
        this.archivoMaestro.generarArbol();
        this.archivoHechos = archivoHechos;
        reglasAplicadas = new ArrayList<>();
        conjuntoConflicto = new ArrayList<>();
    }

    public void justificacion()
    {

    }

    public void inicializar(boolean opcion, String metita)
    {
        meta = metita;
        System.out.print("Ingrese la meta que deesea alcanzar, NADA para inferir sin meta específica, o TERMINAR para cancelar");
        //meta = new Scanner(System.in).next();
        if (!meta.equals("TERMINAR"))
            if (opcion)
                encadenamientoHaciaDelante();
            else encadenamientoHaciaAtras();
    }

    private void encadenamientoHaciaDelante()
    {
        conjuntoConflicto.add((byte) 1);
        while (!estaEnHechos(meta) && (conjuntoConflicto.size() > 0 && conjuntoConflicto != null))
        {
            conjuntoConflicto = equiparar(archivoMaestro, archivoHechos);
            if (conjuntoConflicto != null && conjuntoConflicto.size() > 0)
            {
                byte idRegla = resolverConjuntoConflicto(conjuntoConflicto);
                aplicarRegla(idRegla);
            }
        }
        if (estaEnHechos(meta) && !meta.equals("NADA"))
        {
            JOptionPane.showMessageDialog(null, "Exito, meta alcanzada; " + meta, "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } else if (meta.equals("NADA"))
            JOptionPane.showMessageDialog(null, "Exito, meta alcanzada: " + archivoHechos.obtenerHechos().get(archivoHechos.obtenerHechos().size()-1), "EXITO", JOptionPane.INFORMATION_MESSAGE);
        else
        {
            System.out.println("Estatus:\n");
            archivoHechos.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
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
        {
            if (ruleID < regla)
                regla = ruleID;
        }
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
}