package se.determinista.GUI;

import se.determinista.archivos.ArchivoHechos;
import se.determinista.archivos.ArchivoMaestro;
import se.determinista.common.Constantes;
import se.determinista.inferencia.motorInferencia;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PrincipalFrame extends JFrame {

	private JPanel contentPane;


	private ArchivoMaestro archivoMaestro;
	private ArchivoHechos archivoHechos;
	private se.determinista.inferencia.motorInferencia motorInferencia;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                PrincipalFrame frame = new PrincipalFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public PrincipalFrame() {

		String nombreArchivo = Constantes.NOMBRE_ARCHIVOS;
		archivoMaestro = new ArchivoMaestro(nombreArchivo, Constantes.LECTURA_ESCRITURA);
		archivoHechos = new ArchivoHechos(nombreArchivo, Constantes.LECTURA_ESCRITURA);
		motorInferencia = new motorInferencia(archivoMaestro, archivoHechos);

		setTitle("-=Sistema Experto=- Python Team ~ Inteligencia Aritificial~ITC");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 687, 426);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(arg0 -> {
            int respuesta;
            respuesta=JOptionPane.showConfirmDialog(null, "¿Realmente desea salir del Sistema Experto?", "Salir del Sistema Experto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta==0){
                System.exit(0);
            }
        });
		mnArchivo.add(mntmSalir);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mntmAcercaDe.addActionListener(arg0 -> {
            AcercaDeFrame frame = new AcercaDeFrame();
            frame.setVisible(true);
        });
		mnAyuda.add(mntmAcercaDe);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelReglas = new JPanel();
		panelReglas.setBorder(new TitledBorder(null, "Ver Reglas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelReglas.setBounds(15, 12, 320, 170);
		contentPane.add(panelReglas);
		panelReglas.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnVerReglasBC = new JButton("Ver Reglas de la Base de Conocimiento");
		btnVerReglasBC.addActionListener(arg0 -> {
            VisorGenericoFrame frame = new VisorGenericoFrame("Reglas", archivoMaestro.imprimirReglas()); //Remplazar con las reglas segundo parametro
            frame.setLocation(0, 0);
            frame.setVisible(true);
        });
		panelReglas.add(btnVerReglasBC);
		
		JButton btnVerIndexBC = new JButton("Ver índice de reglas");
		btnVerIndexBC.addActionListener(e -> {
            VisorGenericoFrame frame = new VisorGenericoFrame("Indice de Reglas",archivoMaestro.mostrarIndex()); //Remplazar con lo del index segundo parametro
            frame.setLocation(0, 0);
            frame.setVisible(true);
        });
		panelReglas.add(btnVerIndexBC);
		
		JPanel panelBC = new JPanel();
		panelBC.setBorder(new TitledBorder(null, "Base de Conocimiento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBC.setBounds(350, 12, 320, 170);
		contentPane.add(panelBC);
		panelBC.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnAddReglasBC = new JButton("Agregar reglas a la BC");
		btnAddReglasBC.addActionListener(arg0 -> {
            AddReglasBCFrame frame = new AddReglasBCFrame(archivoMaestro);
            frame.setVisible(true);
        });
		panelBC.add(btnAddReglasBC);
		
		JButton btnDeleteReglasBC = new JButton("Borrar todas las reglas de la BC");
		btnDeleteReglasBC.addActionListener(e -> {
            int respuesta;

            respuesta=JOptionPane.showConfirmDialog(null, "¿Realmente desea BORRAR todas las Reglas?", "Borrar Reglas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta==0){
                archivoMaestro.eliminarReglas();
                JOptionPane.showMessageDialog(null, "¡Reglas Borradas!","Exito al borrar las regla.",JOptionPane.WARNING_MESSAGE);
            }else if(respuesta==1){
                JOptionPane.showMessageDialog(null, "¡Reglas sin cambios!","No hay cambios",JOptionPane.WARNING_MESSAGE);
            }
        });
		panelBC.add(btnDeleteReglasBC);
		
		JPanel panelBH = new JPanel();
		panelBH.setBorder(new TitledBorder(null, "Base de Hechos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBH.setBounds(15, 194, 320, 170);
		contentPane.add(panelBH);
		panelBH.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnVerAntecedentesBH = new JButton("Ver antecedentes de la BH");
		btnVerAntecedentesBH.addActionListener(e -> {
			try{
				VisorGenericoFrame frame = new VisorGenericoFrame("Antecedentes",archivoHechos.imprimirHechos()); //Remplazar con los antecedentes segundo parametro
				frame.setLocation(0, 0);
				frame.setVisible(true);
			}catch (Exception ex){
				JOptionPane.showMessageDialog(null, "Aún no hay antecedentes en la base de hechos.","Sin hechos",JOptionPane.WARNING_MESSAGE);
			}
        });
		panelBH.add(btnVerAntecedentesBH);
		
		JButton btnAddHechosBH = new JButton("Agregar hechos a la BH");
		btnAddHechosBH.addActionListener(e -> {
            AddHechosBCFrame frame = new AddHechosBCFrame(archivoHechos);
            frame.setVisible(true);
        });
		panelBH.add(btnAddHechosBH);
		
		JButton btnDeleteHechosBH = new JButton("Borrar todos los hechos de la BH");
		btnDeleteHechosBH.addActionListener(e -> {
            int respuesta;

            respuesta=JOptionPane.showConfirmDialog(null, "¿Realmente desea BORRAR todos los Hechos?", "Borrar Hechos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta==0){
                archivoHechos.borrarHechos();
                JOptionPane.showMessageDialog(null, "¡Hechos Borrados!","Exito al borrar los hechos.",JOptionPane.WARNING_MESSAGE);
            }else if(respuesta==1){
                JOptionPane.showMessageDialog(null, "¡Hechos sin cambios!","No hay cambios",JOptionPane.WARNING_MESSAGE);
            }
        });
		panelBH.add(btnDeleteHechosBH);
		
		JPanel panelMotor = new JPanel();
		panelMotor.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Inferencias y Justificaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelMotor.setBounds(350, 194, 320, 170);
		contentPane.add(panelMotor);
		panelMotor.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnEncadenamientoHaciaAdelante = new JButton("Encadenamiento Hacia Adelante");
		btnEncadenamientoHaciaAdelante.addActionListener(e -> {
            InferenciaFrame frame = new InferenciaFrame(motorInferencia,"Encadenamiento Hacia Adelante");
            frame.setVisible(true);
        });
		panelMotor.add(btnEncadenamientoHaciaAdelante);
		
		JButton btnEncadenamientoHaciaAtras = new JButton("Encadenamiento Hacia Atras");
		btnEncadenamientoHaciaAtras.addActionListener(e -> {

			InferenciaFrame frame = new InferenciaFrame(motorInferencia,"Encadenamiento Hacia Atras");
			frame.setVisible(true);
        });
		panelMotor.add(btnEncadenamientoHaciaAtras);
		
		JButton btnModuloDeJustificacin = new JButton("Modulo de Justificación.");
		btnModuloDeJustificacin.addActionListener(e -> {
            VisorGenericoFrame frame = new VisorGenericoFrame("Modulo de Justificación","Aqui van todas lo del modulo de justificacion"); //Remplazar con lo del modulo de justificacion segundo parametro
            frame.setLocation(0, 0);
            frame.setVisible(true);
        });
		panelMotor.add(btnModuloDeJustificacin);
	}
}
