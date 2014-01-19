package Interface;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.*;

public class central extends JFrame implements ActionListener {

	JButton refresh = new JButton("Iniciar comunicación");
	JButton upload = new JButton("Iniciar Robot");
	JButton LED = new JButton("Encender LED");
	JTextField uptxt = new JTextField();
	JPanel arriba = new JPanel();
	JPanel abajo = new JPanel();
	JEditorPane console = new JEditorPane();
	Boolean state=true;
	String temp = "";
	String[] letras = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z", " " };
	int[] keys = { KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D,
			KeyEvent.VK_E, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H,
			KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
			KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_O, KeyEvent.VK_P,
			KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T,
			KeyEvent.VK_U, KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_X,
			KeyEvent.VK_Y, KeyEvent.VK_Z, KeyEvent.VK_SPACE };
	

	central() {
		super("Panel de Control -Joystick LDH-");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		FlowLayout low = new FlowLayout();
		GridLayout grid = new GridLayout(0, 3);
		arriba.setLayout(low);
		abajo.setLayout(grid);

		refresh.addActionListener(this);
		refresh.setActionCommand("act");

		LED.addActionListener(this);
		LED.setActionCommand("LED");
		
		console.setEditable(false);
		console.setContentType("text/html");

		arriba.add(upload);
		upload.addActionListener(this);
		upload.setActionCommand("robot");

		abajo.add(uptxt);
		abajo.add(refresh);
		abajo.add(LED);

		add(arriba, BorderLayout.NORTH);
		add(abajo, BorderLayout.SOUTH);

		add(console, BorderLayout.CENTER);
	}

	public void addmsg(String msg) {
		temp = temp + msg;
		console.setText("<html><head><style type=\"text/css\">body{background-color:black; color:green;}</style></head><body>"
				+ temp + "</body></html>");
	}

	public void robot(String msg) throws AWTException {

		Robot rob = new Robot();
		String[] tempo = new String[msg.length()];
		for (int i = 0; i < msg.length(); i++) {
			tempo[i] = msg.substring(i, i + 1);
		}
		rob.delay(500);
		for (int i = 0; i < tempo.length; i++) {
			boolean mayus = false;
			int key = 0;
			for (int u = 0; u < letras.length; u++) {
				if (tempo[i].equals(letras[u])) {
					key = u;
				} else if (tempo[i].equalsIgnoreCase(letras[u])) {
					key = u;
					mayus = true;
				}
			}
			rob.delay(200);
			if (tempo[i].equalsIgnoreCase("ñ")) {
				rob.keyPress(KeyEvent.VK_CONTROL);
				rob.keyPress(KeyEvent.VK_ALT);
				rob.keyPress(KeyEvent.VK_4);
				rob.keyRelease(KeyEvent.VK_4);
				rob.keyRelease(KeyEvent.VK_ALT);
				rob.keyRelease(KeyEvent.VK_CONTROL);
				rob.keyPress(KeyEvent.VK_N);
				rob.keyRelease(KeyEvent.VK_N);
			} else if (tempo[i] != "Ñ" && tempo[i] != "ñ") {
				if (mayus == true) {
					rob.keyPress(KeyEvent.VK_SHIFT);
					rob.keyPress(keys[key]);
					rob.keyRelease(keys[key]);
					rob.keyRelease(KeyEvent.VK_SHIFT);
				} else if (mayus == false) {
					rob.keyPress(keys[key]);
					rob.keyRelease(keys[key]);
				}
			}

		}
	}

	CommPortIdentifier myCPI = null;
	SerialPort mySP;
	Scanner mySC, teclado;
	PrintStream myPS;
	public String mensaje = null;
	String com="COM4";
	Vector portopc=new Vector();
	public void conectar() throws PortInUseException,
			UnsupportedCommOperationException {
		Enumeration commports = CommPortIdentifier.getPortIdentifiers();
		int i=0;
		while(commports.hasMoreElements()){
						
			myCPI = (CommPortIdentifier) commports.nextElement();
			portopc.add(myCPI.getName());
			i++;
		}
		
		Object seleccion = JOptionPane.showInputDialog(
					this,
				   "Selecciona el puerto",
				   "¿Qué puerto vas a usar?",
				   JOptionPane.QUESTION_MESSAGE,
				   null, 
				   portopc.toArray(),
				   null);
			com=(String) seleccion;
		
		
		while (commports.hasMoreElements()) {
			myCPI = (CommPortIdentifier) commports.nextElement();

			if (myCPI.getName().equals(com)) {
								
				break;
			}
		}
		CommPort puerto = myCPI.open("Puerto Serial", 2000);
		mySP = (SerialPort) puerto;
		mySP.setSerialPortParams(115200, mySP.DATABITS_8, mySP.STOPBITS_1,
				mySP.PARITY_NONE);
		JOptionPane.showMessageDialog(null,
				"Conectado",
				"Conexión establecida",
				JOptionPane.INFORMATION_MESSAGE);
	}

	Thread inout = new Thread() {
		public void run() {
			try {
				try {
					in_out();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};

	public String actualizar() throws IOException {
		mySC = new Scanner(mySP.getInputStream());
		String selec = "";
		if (mySC.hasNext()) {
			addmsg((selec = mySC.next()));
			addmsg("\n");
		}
		return selec;
	}
	public void enviar(String i) throws IOException {
		OutputStream out=mySP.getOutputStream();
		out.write(i.getBytes());
		System.out.println(actualizar());
	}

	public void in_out() throws IOException, AWTException, InterruptedException {
		JOptionPane.showMessageDialog(null, "Recibiendo",
				"Conectado", JOptionPane.INFORMATION_MESSAGE);
		String selec = "";
		boolean b1 = false, b2 = false, b3 = false;
		while (true) {
			Robot rob = new Robot();
			selec = actualizar();
	//		if (selec.equals("4")) {
	//			rob.keyPress(KeyEvent.VK_SPACE);
	//		}
			if (selec.equals("9")){
				rob.keyPress(KeyEvent.VK_DOWN);
				 Thread.sleep(05);
				 rob.keyRelease(KeyEvent.VK_DOWN);
		}
				if (selec.equals("5")){
					rob.keyPress(KeyEvent.VK_DOWN);
					 Thread.sleep(05);
					rob.keyRelease(KeyEvent.VK_DOWN);
					}
			if (selec.equals("7")) {
				rob.keyPress(KeyEvent.VK_RIGHT);
				 Thread.sleep(05);
				rob.keyRelease(KeyEvent.VK_RIGHT);
			}
			if (selec.equals("3")) {
				rob.keyPress(KeyEvent.VK_RIGHT);
				 Thread.sleep(05);
				rob.keyRelease(KeyEvent.VK_RIGHT);
			}
			if (selec.equals("10")) {
				rob.keyPress(KeyEvent.VK_LEFT);
				 Thread.sleep(05);
				rob.keyRelease(KeyEvent.VK_LEFT);
			}
			if (selec.equals("6")) {
				rob.keyPress(KeyEvent.VK_LEFT);
				 Thread.sleep(05);
				rob.keyRelease(KeyEvent.VK_LEFT);
			}
			if (actualizar().equals("8")) {
//				rob.keyPress(KeyEvent.VK_DOWN);
			 rob.keyPress(KeyEvent.VK_UP);
			 Thread.sleep(05);
				rob.keyRelease(KeyEvent.VK_UP);
			}

			 if (actualizar().equals("4")) {
			 rob.keyPress(KeyEvent.VK_UP);
			 Thread.sleep(10);
				rob.keyRelease(KeyEvent.VK_UP);
			 }


		}


			 
	}

	public static void main(String[] args) {
		central ini = new central();
		ini.setVisible(true);
		ini.setLocationRelativeTo(null);
		ini.addmsg("<center>-Consola Arduino-</center></b>");
		try {
			ini.conectar();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent AE) {

		if (AE.getActionCommand().equals("act")) {
			inout.start();
		}
		if (AE.getActionCommand().equals("LED")) {
			try {
				if(state){enviar("on");state=false;}else if(!state){enviar("off");state=true;}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (AE.getActionCommand().equals("robot")) {
			try {
				Process process = Runtime.getRuntime().exec("notepad.exe");
				robot(uptxt.getText());
				Thread.sleep(1000);
				process.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
