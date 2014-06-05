package com.christian.satellitelink;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class TelnetConnection {
	protected static Context mContext;
	public TelnetConnection(Context applicationContext) {
		mContext =applicationContext;
	}
	static String s="";
    static String outputStrings="";

	final static ArrayList<String> commandArray = new ArrayList<>();

	public static void readArray(){
		try{
		FileInputStream input = mContext.openFileInput("commands.txt"); // Open input stream
		DataInputStream din = new DataInputStream(input);
		int sz = din.readInt(); // Read line count
		for (int i=0;i<sz;i++) { // Read lines
		   String line = din.readUTF();
		   commandArray.add(line);
		}
		din.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static String readDisconnect(){
		try{
		FileInputStream input = mContext.openFileInput("disconnect.txt"); // Open input stream
		DataInputStream din = new DataInputStream(input);
		int sz = din.readInt(); // Read line count
		for (int i=0;i<sz;i++) { // Read lines
		   String line = din.readUTF();
		   return line;
		}
		din.close();
		}catch(FileNotFoundException e){
			return "AT+CGACT=0,7";
		}catch(IOException e){
		
			e.printStackTrace();
		}
		return null;
	}
public static String SendConnectCommand(String serverIP, String serverPortS){
	readArray();
	Socket servSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int serverPort= Integer.parseInt(serverPortS);

    try {
        servSocket = new Socket(serverIP, serverPort);
        s =s+ "\nStablishing connection to: "+ servSocket.getInetAddress()+":"+servSocket.getPort();
        Log.d("TELNET",s );
        outputStrings="\n"+s;

        out = new PrintWriter(servSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(servSocket.getInputStream()));
        
        //Send commands
        for (int i=0;i<commandArray.size();i++){
        	out.println(commandArray.get(i));
        	s=s+in.read();
            Log.d("TELNET",s );
            outputStrings= "\n"+s;
        }
        
        out.close();
        in.close();
        servSocket.close();
    } catch (IOException e) {
    	Log.d("TELNET", e.getMessage());
        outputStrings ="\n"+e.getMessage();
    }
	return outputStrings;

   
}
public static String SendDisconnectCommand(String serverIP, String serverPortS){
	Socket servSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int serverPort= Integer.parseInt(serverPortS);
    String disconnect = readDisconnect();


    try {
        servSocket = new Socket(serverIP, serverPort);
    	s=s+"Sending logout to: "+ servSocket.getInetAddress()+":"+servSocket.getPort();

        Log.d("TELNET",s );
        outputStrings="\n"+s;
        out = new PrintWriter(servSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(servSocket.getInputStream()));
        
        //Send commands
        out.println(disconnect);
        s=s+in.read();
        Log.d("TELNET",s );
        outputStrings= "\n"+s;        
        out.close();
        in.close();
        servSocket.close();
    } catch (IOException e) {
    	Log.d("TELNET", e.getMessage());
        outputStrings="\n"+e.getMessage();
    }
	return outputStrings;

   
}
}
