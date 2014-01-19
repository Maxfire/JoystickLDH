package Interface;

import gnu.io.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

public class escuchaArd {
	central main=new central();
    SerialPort mySP;
    Scanner mySC;
    CommPortIdentifier myCPI = null;
    
	public void run() throws UnsupportedCommOperationException, PortInUseException{
        Enumeration commports = CommPortIdentifier.getPortIdentifiers();

        while (commports.hasMoreElements()) {
            myCPI = (CommPortIdentifier) commports.nextElement();
            System.out.println(myCPI.getClass());
            if (myCPI.getName().equals("COM4")) {
            	System.out.println("ON");
                break;
            }
        }
        CommPort puerto = myCPI.open("Puerto Serial", 2000);
        mySP = (SerialPort) puerto;
        mySP.setSerialPortParams(115200, mySP.DATABITS_8, mySP.STOPBITS_1, mySP.PARITY_NONE);
    	while(true){
		try {
			mySC = new Scanner(mySP.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mySC.hasNext()){
			main.addmsg(mySC.next());

		}}
	}
	
	public static void main(String[]Args) throws PortInUseException, UnsupportedCommOperationException{
		escuchaArd ini=new escuchaArd();
		ini.run();
		
	}
}
