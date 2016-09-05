package se.determinista.inference;

import se.determinista.files.FactsFile;
import se.determinista.files.MasterFile;
import se.determinista.tree.Rule;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andrés on 01/09/2016.
 */
public class InferenceEngine {

    private MasterFile mfile;
    private FactsFile ffile;
    private ArrayList<Byte> appliedRules;
    private ArrayList<Byte> conflictiveSet;
    private String goal = null;

    /**
     *Constructs a new InferenceEngine with the specified Knowledge Base and Facts Base
     */
    public InferenceEngine(MasterFile _mfile, FactsFile _ffile) {
        mfile = _mfile;
        mfile.generateTree();
        ffile = _ffile;
        appliedRules = new ArrayList<>();
        conflictiveSet = new ArrayList<>();
    }

    public void initialize() {
        System.out.print("Ingrese la meta que deesea alcanzar, NONE para inferir sin meta específica, o \"x\" para cancelar");
        goal = new Scanner(System.in).next();
        if (!goal.equals("x"))
            forwardChaining();
    }

    private void forwardChaining() {
        conflictiveSet.add((byte) 1);
        while (!isContainedInFacts(goal) && (conflictiveSet.size() > 0 && conflictiveSet != null)) {
            conflictiveSet = equate(mfile, ffile);
            if (conflictiveSet != null && conflictiveSet.size() > 0) {
                byte ruleID = resolveConflictiveSet(conflictiveSet);
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

    private boolean isContainedInFacts(String _goal) {
        if (ffile.getAllFacts().contains(_goal))
            return true;
        else
            return false;
    }

    private ArrayList<Byte> equate(MasterFile _knowledgebase, FactsFile _factsbase) {
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

    private boolean refractRule(byte _ruleID) {
        if (appliedRules.contains(_ruleID))
            return true;
        else
            return false;
    }

    private byte resolveConflictiveSet(ArrayList<Byte> _rulesID) {
        byte chosen = _rulesID.get(0);
        for (byte ruleID : _rulesID) {
            if (ruleID < chosen)
                chosen = ruleID;
        }
        return chosen;
    }

    private void applyRuleAndUpdateFacts(byte _ruleID) {
        ffile.insertFact(mfile.getRule(_ruleID).getConsequent());
        appliedRules.add(_ruleID);
    }
}