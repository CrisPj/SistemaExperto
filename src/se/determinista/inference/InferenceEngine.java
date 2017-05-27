package se.determinista.inference;

import se.determinista.files.ArchivoMaestro;
import se.determinista.files.FactsFile;
import se.determinista.tree.Rule;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andrés on 01/09/2016.
 */
public class InferenceEngine {

    private ArchivoMaestro mfile;
    private FactsFile ffile;
    private ArrayList<Byte> appliedRules;
    private ArrayList<Byte> conflictSet;
    private String goal = null;

    /**
     *Constructs a new InferenceEngine with the specified Knowledge Base and Facts Base
     */
    public InferenceEngine(ArchivoMaestro _mfile, FactsFile _ffile) {
        mfile = _mfile;
        mfile.generateTree();
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
            ffile.printAllFacts();
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
        if (ffile.getAllFacts().contains(_goal))
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
    private ArrayList<Byte> equate(ArchivoMaestro _knowledgebase, FactsFile _factsbase) {
        ArrayList<Byte> rulesID = new ArrayList<>();
        ArrayList<Rule> rules = _knowledgebase.getAllRulesRecords();
        for (Rule rule : rules) {
            String records[] = rule.getRecords();
            ArrayList<String> facts = _factsbase.getAllFacts();
            boolean flag = true;
            for (int i = 0; i < records.length; i++) {
                String s = records[i].trim();
                if (!facts.contains(records[i].trim()) && !s.isEmpty()) {
                    flag = false;
                    i = records.length;
                }
            }
            if (flag && !refractRule(rule.getId()))
                rulesID.add(rule.getId());
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
     * @return The Rule with lowest ID number
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
        ffile.insertFact(mfile.getRule(_ruleID).getConsequent());
        appliedRules.add(_ruleID);
    }
}