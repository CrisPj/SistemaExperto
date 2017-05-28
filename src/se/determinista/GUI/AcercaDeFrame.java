package se.determinista.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AcercaDeFrame extends JFrame {

	private JPanel contentPane;

	public AcercaDeFrame() {
		setTitle("-=Sistema Experto=- Acerca De");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 522, 591);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel img_mt = new JLabel("");
		img_mt.setBounds(10, 11, 512, 512);
		//Image img = new ImageIcon(this.getClass().getResource("/python.png")).getImage();
		img_mt.setIcon(new ImageIcon(AcercaDeFrame.class.getResource("/img/python.png")));
		contentPane.add(img_mt);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnAceptar);
	}
}
