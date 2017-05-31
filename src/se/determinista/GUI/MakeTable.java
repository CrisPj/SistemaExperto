package se.determinista.GUI;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class MakeTable {
    public static void makeTable(String[] titles, String[][] array, String tituloTabla)
    {
        JFrame paneTabla = new JFrame();
        paneTabla.setTitle(tituloTabla);
        paneTabla.setLayout(new BorderLayout());

        String[] columnNames = titles;

        Object[][] data = new Object[array.length][8];
        for (int i = 0; i < array.length ; i++) {
            for (int j=0; j < array[i].length; j++){
                data[i][j] = array[i][j];
            }
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPaneTable = new JScrollPane(table);
        paneTabla.add(scrollPaneTable, BorderLayout.CENTER);

        paneTabla.setSize(600, 200);
        paneTabla.setLocationRelativeTo(null);
        paneTabla.setVisible(true);
        //paneComentarios.pack();
    }
}
