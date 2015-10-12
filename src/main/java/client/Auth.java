package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Auth extends JFrame {

	private JPanel login;

	private JTextField user;

	private JPasswordField pass;

	private JButton button;
	
	private JTextField address;

	private JPanel stream;

	public Auth() {
		// config
		this.setSize(800, 600);
		login = new JPanel();
		login.setLayout(new GridLayout(7, 1));
		user = new JTextField();
		address = new JTextField();
		pass = new JPasswordField();
		login.add(new JLabel("Servidor:"));
		login.add(address);
		login.add(new JLabel("Usuario:"));
		login.add(user);
		login.add(new JLabel("Contraseña:"));
		login.add(pass);
		

		button = new JButton("Iniciar");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						try {
							Socket clientSocket = new Socket(address.getText(), 9975);
							OutputStreamWriter osw = new OutputStreamWriter(
									clientSocket.getOutputStream(), "UTF-8");
							osw.write(user.getText() + ";" + new String(pass.getPassword()) + "\n");
							osw.flush();
							
							BufferedReader br = new BufferedReader(
									new InputStreamReader(clientSocket.getInputStream()));
							String s = br.readLine();
							if(s.equals("Credentials OK dude")) {
								System.out.println(s);
								new Main();
							}
							clientSocket.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		login.add(button);
		this.add(login);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Auth();
	}
}
