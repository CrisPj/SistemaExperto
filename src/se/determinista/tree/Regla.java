package se.determinista.tree;

/**
 * Created by Andrés on 31/08/2016.
 */
public class Regla
{


    public static byte ID_SIZE = 1;
    public static int RECORDS_QUANTITY = 5;
    public static int SINGULAR_RECORD_SIZE = 20;
    public static int RECORD_SIZE = +ID_SIZE + (SINGULAR_RECORD_SIZE * RECORDS_QUANTITY + SINGULAR_RECORD_SIZE) * 2;

    private byte id;
    private String[] records;
    private String consequent;

    /**
     * Creates a new empty Regla
     */
    public Regla() {
    }

    /**
     * Constructs a Regla specifying its Id, records and consequent
     *
     * @param _id
     * @param _records
     * @param _consequent
     */
    public Regla(byte _id, String[] _records, String _consequent) {
        id = _id;
        records = _records;
        consequent = _consequent;
    }

    /**
     * Returns the Regla id
     *
     * @return byte
     */
    public byte getId() {
        return id;
    }

    /**
     * Sets the rule id in a byte data type
     * @param _id
     */
    public void setId(byte _id) {
        id = _id;
    }

    /**
     * Returns all the records for this rule
     *
     * @return String[]
     */
    public String[] getRecords() {
        return records;
    }

    /**
     * Sets the records for this rule
     * @param _records
     */
    public void setRecords(String[] _records) {
        records = _records;
    }

    /**
     * Returns the consequent of this Regla
     * @return String
     */
    public String getConsequent() {
        return consequent;
    }

    /**
     * Sets the consequent of this rule
     *
     * @param _consequent
     */
    public void setConsequent(String _consequent) {
        consequent = _consequent;
    }

    /*
    public static void printRule(Regla _rule) {
        if (_rule.obtenerRegistros() != null) {
            System.out.println("ID:" + _rule.getId() +
                    " Antecedentes: "
                    + _rule.obtenerRegistros()[0] + ","
                    + _rule.obtenerRegistros()[1] + ","
                    + _rule.obtenerRegistros()[2] + ","
                    + _rule.obtenerRegistros()[3] + ","
                    + _rule.obtenerRegistros()[4] + ""
                    + "->" + _rule.getConsequent());
        }
    }
*/

}
