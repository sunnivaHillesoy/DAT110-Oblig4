package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import okhttp3.*;


public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";
	private String host = "localhost";
	private int port = 8080;


	public void doPostAccessEntry(String message) {		
		try (Socket s = new Socket(host, port)) {

			Gson gson = new Gson();
			// construct the HTTP request
			String jsonbody = gson.toJson(new AccessMessage(message));

			String httpPutReq = "POST " + logpath + " HTTP/1.1\r\n" + "Host: " + host + "\r\n"
					+ "Content-type: application/json\r\n" + "Content-length: " + jsonbody.length() + "\r\n"
					+ "Connection: close\r\n" + "\r\n" + jsonbody + "\r\n";
			
			// send the response over the TCP connection
			OutputStream output = s.getOutputStream();

			PrintWriter prinwrit = new PrintWriter(output, false);
			prinwrit.print(httpPutReq);
			prinwrit.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner reader = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (reader.hasNext()) {

				String nextline = reader.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				if (nextline.isEmpty()) {
					header = false;
				}
			}

			System.out.println("BODY:");
			System.out.println(jsonresponse.toString());

			reader.close();

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

//		final MediaType JSON
//	    = MediaType.parse("application/json; charset=utf-8");
//		
//		RequestBody body = RequestBody.create(JSON, message);
//		OkHttpClient client = new OkHttpClient();
//
//		Request request = new Request.Builder()
//				.url("http://localhost:8080"+logpath)
//				.post(body)
//				.build();
//
//		System.out.println(request.toString());
//
//		try (Response response = client.newCall(request).execute()) {
//			System.out.println (response.body().string());
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {
		AccessCode code = new AccessCode();
		
		try (Socket s = new Socket(host, port)) {

			// construct the GET request
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";

			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httpgetrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}

			}
	
			System.out.println(jsonresponse.toString());
			Gson gson = new Gson();
			code = gson.fromJson(jsonresponse.toString(), AccessCode.class);
			scan.close();


		} catch (IOException ex) {
			System.err.println(ex);
		}
//		AccessCode code = null;
//		
//		OkHttpClient client = new OkHttpClient();
//		
//		Request req = new Request.Builder()
//				.url("http://localhost:8080" + codepath)
//				.get()
//				.build();
//		
//		System.out.println(req.toString());
//		
//		try(Response response = client.newCall(req).execute()){
//			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//			Gson gson = new Gson();
//			code = gson.fromJson(response.body().string(), AccessCode.class);
//			
//		} catch (IOException e){
//			e.printStackTrace();
//		}
//		
		return code;
	}
}
