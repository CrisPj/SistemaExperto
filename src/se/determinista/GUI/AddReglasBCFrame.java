package se.determinista.GUI;

import se.determinista.arbol.Regla;
import se.determinista.archivos.ArchivoMaestro;
import se.determinista.common.Constantes;

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

public class AddReglasBCFrame extends JFrame {

	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private JTextField txtAgregarReglas;
	private ArchivoMaestro archivoMaestro;

	/**
	 * Create the frame.
	 * @param archivoMaestro
	 */
	public AddReglasBCFrame(ArchivoMaestro archivoMaestro)
	{
		this.archivoMaestro = archivoMaestro;
		setResizable(false);
		setTitle("-=Sistema Experto=- Agregar Reglas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Aqui se podria poner el la X que se ocupa.
		setBounds(100, 100, 500, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBounds(12, 12, 476, 149);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblAgregarReglas = new JLabel("Agregar Reglas");
		lblAgregarReglas.setHorizontalAlignment(SwingConstants.CENTER);
		lblAgregarReglas.setBounds(142, 12, 192, 15);
		panel.add(lblAgregarReglas);

		txtAgregarReglas = new JTextField();
		txtAgregarReglas.setHorizontalAlignment(SwingConstants.CENTER);
		txtAgregarReglas.setBounds(12, 72, 452, 25);
		panel.add(txtAgregarReglas);
		txtAgregarReglas.setColumns(10);

		JButton btnAgregarRegla = new JButton("Agregar");
		btnAgregarRegla.addActionListener(e -> {
			//Cositas Para el sistema Experto...
			if (new java.io.File(Constantes.NOMBRE_ARCHIVOS + Constantes.EXTENCION_CONOCIMIENTO).exists())
			{
				try
					{
						archivoMaestro.nuevoRegistro(mostrarRegla(txtAgregarReglas.getText()));
						JOptionPane.showMessageDialog(null, "¡Regla Agregada!", "Exito al agregar la regla.", JOptionPane.INFORMATION_MESSAGE);
						txtAgregarReglas.setText("");
					} catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null,"Regla mal formado","Error",JOptionPane.ERROR_MESSAGE);
					}
			} else
			{
				archivoMaestro.crearArchivo(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
			}
		});
		btnAgregarRegla.setBounds(180, 109, 117, 25);
		panel.add(btnAgregarRegla);

		JButton btnTerminarRegla = new JButton("Terminar");
		btnTerminarRegla.addActionListener(e -> {
			dispose();
		});
		btnTerminarRegla.setBounds(347, 109, 117, 25);
		panel.add(btnTerminarRegla);

		JLabel lblLasReglasLlevan = new JLabel("Las reglas llevan el siguente formato: ID-Ant&Ant...-Cons");
		lblLasReglasLlevan.setHorizontalAlignment(SwingConstants.CENTER);
		lblLasReglasLlevan.setBounds(12, 42, 452, 15);
		panel.add(lblLasReglasLlevan);
	}
	private Regla mostrarRegla(String entrada) throws Exception {
		return new Regla(Byte.parseByte(entrada.split("-")[0]), entrada.split("-")[1].split("\\&"), entrada.split("-")[2]);
	}
}
