package se.determinista.tree;

/**
 * Created by Andrés on 31/08/2016.
 */
public class Rule {


    public static byte ID_SIZE = 1;
    public static int RECORDS_QUANTITY = 5;
    public static int SINGULAR_RECORD_SIZE = 20;
    public static int RECORD_SIZE = +ID_SIZE + (SINGULAR_RECORD_SIZE * RECORDS_QUANTITY + SINGULAR_RECORD_SIZE) * 2;

    private byte id;
    private String[] records;
    private String consequent;

    public Rule() {
    }

    public Rule(byte _id, String[] _records, String _consequent) {
        id = _id;
        records = _records;
        consequent = _consequent;
    }

    public static void printRule(Rule _rule) {
        if (_rule.getRecords() != null) {
            System.out.println("ID:" + _rule.getId() +
                    " Antecedentes: "
                    + _rule.getRecords()[0] + ","
                    + _rule.getRecords()[1] + ","
                    + _rule.getRecords()[2] + ","
                    + _rule.getRecords()[3] + ","
                    + _rule.getRecords()[4] + ""
                    + "->" + _rule.getConsequent());
        }
    }

    public byte getId() {
        return id;
    }

    public void setId(byte _id) {
        id = _id;
    }

    public String[] getRecords() {
        return records;
    }

    public void setRecords(String[] _records) {
        records = _records;
    }

    public String getConsequent() {
        return consequent;
    }

    public void setConsq(String _consequent) {
        consequent = _consequent;
    }

}
