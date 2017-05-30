package se.determinista.GUI;

import se.determinista.inferencia.motorInferencia;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class InferenciaFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtInputMeta;

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
		
		JTextArea txtrInfo = new JTextArea();
		txtrInfo.setEditable(false);
		txtrInfo.setWrapStyleWord(true);
		txtrInfo.setLineWrap(true);
		txtrInfo.setText("Ingrese la meta que se desea alcanzar o deje vacio para inferir sin meta.");
		txtrInfo.setBounds(12, 36, 456, 51);
		panel.add(txtrInfo);
		
		txtInputMeta = new JTextField();
		txtInputMeta.setBounds(12, 96, 456, 28);
		panel.add(txtInputMeta);
		txtInputMeta.setColumns(10);
		
		JButton btnInferir = new JButton("Inferir");
		btnInferir.addActionListener(e -> {
            String meta = txtInputMeta.getText();
            if(tipoDeInferencia.equals("Encadenamiento Hacia Adelante")){
                motorInferencia.inicializar(true,meta);
            }else if(tipoDeInferencia.equals("Encadenamiento Hacia Atras")){
                motorInferencia.inicializar(false,meta);
            }
        });
		btnInferir.setBounds(182, 133, 117, 25);
		panel.add(btnInferir);
	}
}
