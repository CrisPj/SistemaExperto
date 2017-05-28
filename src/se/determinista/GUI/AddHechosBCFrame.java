package se.determinista.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddHechosBCFrame extends JFrame {

	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private JTextField txtAgregarHechos;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddHechosBCFrame frame = new AddReglasBCFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public AddHechosBCFrame() {
		setResizable(false);
		setTitle("-=Sistema Experto=- Agregar Hechos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Aqui se podria poner el la X que se ocupa.
		setBounds(100, 100, 500, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBounds(12, 12, 476, 149);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblAgregarReglas = new JLabel("Agregar Hechos");
		lblAgregarReglas.setHorizontalAlignment(SwingConstants.CENTER);
		lblAgregarReglas.setBounds(142, 12, 192, 15);
		panel.add(lblAgregarReglas);
		
		txtAgregarHechos = new JTextField();
		txtAgregarHechos.setHorizontalAlignment(SwingConstants.CENTER);
		txtAgregarHechos.setBounds(12, 72, 452, 25);
		panel.add(txtAgregarHechos);
		txtAgregarHechos.setColumns(10);
		
		JButton btnAgregarHecho = new JButton("Agregar");
		btnAgregarHecho.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Cositas Para el sistema Experto...
				
				JOptionPane.showMessageDialog(null, "¡Hecho Agregado!","Exito al agregar el hecho.",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnAgregarHecho.setBounds(180, 109, 117, 25);
		panel.add(btnAgregarHecho);
		
		JButton btnTerminarHecho = new JButton("Terminar");
		btnTerminarHecho.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnTerminarHecho.setBounds(347, 109, 117, 25);
		panel.add(btnTerminarHecho);
		
		JLabel lblLasReglasLlevan = new JLabel("Agrega los hechos uno por uno.");
		lblLasReglasLlevan.setHorizontalAlignment(SwingConstants.CENTER);
		lblLasReglasLlevan.setBounds(12, 42, 452, 15);
		panel.add(lblLasReglasLlevan);
	}
}
