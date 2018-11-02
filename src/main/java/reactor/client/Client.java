package reactor.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String addr = "192.168.2.55";
		int port = 8083;
		Socket soket = new Socket(addr, port);
		PrintWriter out = new PrintWriter(soket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(soket.getInputStream()));
		BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)));
        String input;
        while((input = stdIn.readLine()) != null) {
        	out.write(input);
        	out.flush();
        	if(input.equalsIgnoreCase("exit")) {
        		break;
        	}
        	System.out.println("server:" + reader.readLine());
        }
        soket.close();
	}

}
