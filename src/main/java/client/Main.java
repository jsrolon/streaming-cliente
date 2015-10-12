package client;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {

	private JFrame jf;

	private JPanel jp;

	private JLabel jl;

	public Main() {
		
		System.out.println("main started");

		jf = new JFrame();
		jf.setSize(800, 600);
		jp = new JPanel();
		jl = new JLabel();
		jp.add(jl);
		jf.add(jp);
		jf.setVisible(true);

		new Thread() {
			public void run() {
				int i = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (true) {
					try {
						DatagramSocket ds = new DatagramSocket(1337);
						DatagramPacket dp = new DatagramPacket(new byte[58880], 58880);
						ds.receive(dp);
						ds.close();

						baos.write(dp.getData(), 0, 58880);
						i++;
						if (i == 12) {
							// baos.flush();
							// byte[] bytes = baos.toByteArray();
							// InputStream bais = new
							// ByteArrayInputStream(bytes);
							// try {
							// BufferedImage bi = ImageIO.read(bais);
							// jl.setIcon(new ImageIcon(bi));
							// } catch(NullPointerException e) {
							// System.out.println("error recovering image from
							// bytes");
							// }
							BufferedImage currentImage = new BufferedImage(640, 
									368, BufferedImage.TYPE_3BYTE_BGR);
							byte[] imgData = ((DataBufferByte) currentImage.getRaster().getDataBuffer()).getData();
							System.arraycopy(baos.toByteArray(), 0, imgData, 0, 706560);
							jl.setIcon(new ImageIcon(currentImage));
							i = 0;
							baos = new ByteArrayOutputStream(706560);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Main();
//	}
}
