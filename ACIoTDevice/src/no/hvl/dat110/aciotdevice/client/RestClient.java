package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";
	private static final String host = "localhost";
	private static final int port = 8080;

	public void doPostAccessEntry(String message) {
		Gson gson = new Gson();
		
		try (Socket s = new Socket(host, port)){
			String jsonbody = gson.toJson(new AccessMessage(message));
			
			//Construct the HTTP request
			String httppostrequest = "POST " + logpath + " HTTP/1.1\r\n" + 
			        "Host: " + host + "\r\n" +
					"Content-type: application/json\r\n" + 
			        "Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + 
			        "\r\n" + 
					jsonbody + 
					"\r\n";
			
			//Send the HTTP request
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httppostrequest);
			pw.flush();
			
			//Read the HTTP response
			InputStream in = s.getInputStream();
			Scanner sc = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;
			
			while(sc.hasNext()) {
				String nextline = sc.nextLine();
				
				if(header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}
				
				if(nextline.isEmpty()) {
					header = false;
				}
			}
			System.out.println("BODY: ");
			System.out.println(jsonresponse.toString());
			sc.close();
			
		} catch(IOException e) {
			System.err.println(e);
		}
		
		
		
		
		// TODO: implement a HTTP POST on the service to post the message
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {
		AccessCode code = null;
		
		try(Socket s = new Socket(host, port)){
			
			//Construct the HTTP request
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";
			
			//send the HTTP request
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httpgetrequest);
			pw.flush();
			
			//read the HTTP response
			InputStream in = s.getInputStream();
			
			Scanner sc = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (sc.hasNext()) {

				String nextline = sc.nextLine();

				if (!header) {
					jsonresponse.append(nextline);
				} 

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}

			}
			
			sc.close();
			
			
		} catch(IOException e) {
			System.err.println(e);
		}
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		return code;
	}
}
