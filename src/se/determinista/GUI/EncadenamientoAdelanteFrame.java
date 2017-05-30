package se.determinista.GUI;

import se.determinista.inferencia.motorInferencia;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EncadenamientoAdelanteFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtEncadenamientoAdelante;
	private se.determinista.inferencia.motorInferencia motorInferencia;

	/**
	 * Create the frame.
	 * @param motorInferencia
	 */
	public EncadenamientoAdelanteFrame(motorInferencia motorInferencia) {
		this.motorInferencia = motorInferencia;
		setTitle("-=Sistema Experto=- Encadenamiento hacia delante");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblEncadenamientoHaciaAdelante = new JLabel("Encadenamiento hacia adelante");
		lblEncadenamientoHaciaAdelante.setBounds(111, 12, 259, 15);
		panel.add(lblEncadenamientoHaciaAdelante);
		
		JTextArea txtrIngreseLaMeta = new JTextArea();
		txtrIngreseLaMeta.setEditable(false);
		txtrIngreseLaMeta.setWrapStyleWord(true);
		txtrIngreseLaMeta.setLineWrap(true);
		txtrIngreseLaMeta.setText("Ingrese la meta que deesea alcanzar, NONE para inferir sin meta específica, o TERMINAR para cancelar");
		txtrIngreseLaMeta.setBounds(12, 36, 456, 51);
		panel.add(txtrIngreseLaMeta);
		
		txtEncadenamientoAdelante = new JTextField();
		txtEncadenamientoAdelante.setBounds(12, 96, 456, 28);
		panel.add(txtEncadenamientoAdelante);
		txtEncadenamientoAdelante.setColumns(10);
		
		JButton btnInferir = new JButton("Inferir");
		btnInferir.addActionListener(e -> {
			//motorInferencia.inicializar(true);
        });
		btnInferir.setBounds(182, 133, 117, 25);
		panel.add(btnInferir);
	}
}
