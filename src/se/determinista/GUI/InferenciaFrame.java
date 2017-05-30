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

public class InferenciaFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtEncadenamientoAtras;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EncadenamientoAdelanteFrame frame = new EncadenamientoAdelanteFrame();
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
	public InferenciaFrame(motorInferencia motorInferencia, String tipoDeInferencia) {
		setTitle("-=Sistema Experto=- "+tipoDeInferencia);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblEncadenamientoHaciaAdelante = new JLabel(""+tipoDeInferencia);
		lblEncadenamientoHaciaAdelante.setHorizontalAlignment(SwingConstants.CENTER);
		lblEncadenamientoHaciaAdelante.setBounds(111, 12, 259, 15);
		panel.add(lblEncadenamientoHaciaAdelante);
		
		JTextArea txtrIngreseLaMeta = new JTextArea();
		txtrIngreseLaMeta.setEditable(false);
		txtrIngreseLaMeta.setWrapStyleWord(true);
		txtrIngreseLaMeta.setLineWrap(true);
		txtrIngreseLaMeta.setText("Ingrese la meta que deesea alcanzar, NADA para inferir sin meta específica, o TERMINAR para cancelar");
		txtrIngreseLaMeta.setBounds(12, 36, 456, 51);
		panel.add(txtrIngreseLaMeta);
		
		txtEncadenamientoAtras = new JTextField();
		txtEncadenamientoAtras.setBounds(12, 96, 456, 28);
		panel.add(txtEncadenamientoAtras);
		txtEncadenamientoAtras.setColumns(10);
		
		JButton btnInferir = new JButton("Inferir");
		btnInferir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String meta = txtrIngreseLaMeta.getText();
				if(tipoDeInferencia.equals("Encadenamiento Hacia Adelante")){
					motorInferencia.inicializar(true,meta);
				}else if(tipoDeInferencia.equals("Encadenamiento Hacia Atras")){
					motorInferencia.inicializar(false,meta);
				}
			}
		});
		btnInferir.setBounds(182, 133, 117, 25);
		panel.add(btnInferir);
	}
}
