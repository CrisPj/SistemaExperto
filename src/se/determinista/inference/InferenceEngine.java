package se.determinista.inference;

import se.determinista.files.ArchivoMaestro;
import se.determinista.files.ArchivoHechos;
import se.determinista.tree.Regla;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andrés on 01/09/2016.
 */
public class InferenceEngine {

    private ArchivoMaestro mfile;
    private ArchivoHechos ffile;
    private ArrayList<Byte> appliedRules;
    private ArrayList<Byte> conflictSet;
    private String goal = null;

    /**
     *Constructs a new InferenceEngine with the specified Knowledge Base and Facts Base
     */
    public InferenceEngine(ArchivoMaestro _mfile, ArchivoHechos _ffile) {
        mfile = _mfile;
        mfile.generarArbol();
        ffile = _ffile;
        appliedRules = new ArrayList<>();
        conflictSet = new ArrayList<>();
    }

    /**
     * Initializes the engine asking the user for the goal to reach.
     */
    public void initialize() {
        System.out.print("Ingrese la meta que deesea alcanzar, NONE para inferir sin meta específica, o TERMINAR para cancelar");
        goal = new Scanner(System.in).next();
        if (!goal.equals("TERMINAR"))
            forwardChaining();
    }

    /**
     * Starts the forward chaining with the specified Knowledge base, facts base and goal
     */
    private void forwardChaining() {
        conflictSet.add((byte) 1);
        while (!isContainedInFacts(goal) && (conflictSet.size() > 0 && conflictSet != null)) {
            conflictSet = equate(mfile, ffile);
            if (conflictSet != null && conflictSet.size() > 0) {
                byte ruleID = resolveConflictSet(conflictSet);
                applyRuleAndUpdateFacts(ruleID);
            }
        }
        if (isContainedInFacts(goal) && !goal.equals("NONE"))
            System.out.println("\nÉXITO\n");
        else {
            System.out.println("Estatus:\n");
            ffile.imprimirHechos();
            System.out.println("\nFin del reporte\n");
        }
    }

    /**
     * Search for the goal in the Facts Base
     *
     * @param _goal
     * @return
     */
    private boolean isContainedInFacts(String _goal) {
        if (ffile.obtenerHechos().contains(_goal))
            return true;
        else
            return false;
    }

    /**
     * Equates the Master File and the Facts base to obtain a conflict set of Rules
     * @param _knowledgebase
     * @param _factsbase
     * @return
     */
    private ArrayList<Byte> equate(ArchivoMaestro _knowledgebase, ArchivoHechos _factsbase) {
        ArrayList<Byte> rulesID = new ArrayList<>();
        ArrayList<Regla> reglas = _knowledgebase.mostrarTodasReglas();
        for (Regla regla : reglas) {
            String records[] = regla.getReglas();
            ArrayList<String> facts = _factsbase.obtenerHechos();
            boolean flag = true;
            for (int i = 0; i < records.length; i++) {
                String s = records[i].trim();
                if (!facts.contains(records[i].trim()) && !s.isEmpty()) {
                    flag = false;
                    i = records.length;
                }
            }
            if (flag && !refractRule(regla.getLlave()))
                rulesID.add(regla.getLlave());
        }
        return rulesID;
    }

    /**
     * Refracts the specified rule and returns true if was already applied.
     * @param _ruleID
     * @return
     */
    private boolean refractRule(byte _ruleID) {
        if (appliedRules.contains(_ruleID))
            return true;
        else
            return false;
    }

    /**
     * Chooses a rule from the conflict set.
     *
     * @param _rulesID
     * @return The Regla with lowest ID number
     */
    private byte resolveConflictSet(ArrayList<Byte> _rulesID) {
        byte chosen = _rulesID.get(0);
        for (byte ruleID : _rulesID) {
            if (ruleID < chosen)
                chosen = ruleID;
        }
        return chosen;
    }

    /**
     * Applies the specified rule and update the Facts Base with the rule consequent
     * @param _ruleID
     */
    private void applyRuleAndUpdateFacts(byte _ruleID) {
        ffile.insertarHecho(mfile.obtenerRegla(_ruleID).getConsequente());
        appliedRules.add(_ruleID);
    }
}