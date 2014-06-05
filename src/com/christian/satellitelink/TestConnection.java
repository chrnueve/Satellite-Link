package com.christian.satellitelink;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TestConnection{
    
	public static boolean isGoogleReachable() {
		boolean connected = false;
	    Socket socket;
	    String serverAddress = "www.google.es";
	    int serverTCPport= 80;
	    int timeoutMS=1000;
	    try {
	        socket = new Socket();
	        
			SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverTCPport);
	        socket.connect(socketAddress, timeoutMS);
	        if (socket.isConnected()) {
	            connected = true;
	            socket.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        socket = null;
	    }
	    return connected;
}
}