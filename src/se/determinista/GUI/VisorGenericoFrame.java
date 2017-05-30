package se.determinista.GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class VisorGenericoFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public JTextPane textPane;
	private String salida[] = new String[5];
	private JPanel contentPane;
	DefaultTableModel modelo;


	public VisorGenericoFrame(String tipoDeVisor,String informacion) {
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("-=Sistema Experto=- Visor de "+tipoDeVisor);
		
		setBounds(100, 100, 450, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 49, 428, 231);
		contentPane.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Dialog", Font.PLAIN, 16));
		textPane.setLocation(-252, 0);
		textPane.setText(informacion);
		scrollPane.setViewportView(textPane);
		
		JLabel lblAhoraPuedesCopiar = new JLabel("Visor de "+tipoDeVisor);
		lblAhoraPuedesCopiar.setHorizontalAlignment(SwingConstants.CENTER);
		lblAhoraPuedesCopiar.setFont(new Font("Dialog", Font.BOLD, 14));
		lblAhoraPuedesCopiar.setBounds(52, 11, 345, 26);
		contentPane.add(lblAhoraPuedesCopiar);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Que lo cierre? 
				dispose();
			}
		});
		btnAceptar.setBounds(148, 301, 152, 23);
		contentPane.add(btnAceptar);
		
	}
	
	
   
	
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String ejemplo="TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO\n  TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO TEXTO";
					VisorGenericoFrame frame = new VisorGenericoFrame("Visor de Ejemplo",ejemplo);
					frame.setLocation(0, 0);
					frame.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
}
