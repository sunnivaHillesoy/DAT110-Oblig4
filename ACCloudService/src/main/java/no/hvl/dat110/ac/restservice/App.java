package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		
		post("accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			JsonObject jsonobject = gson.fromJson(req.body(), JsonObject.class);
			String msg = jsonobject.get("message").getAsString();
			int id = accesslog.add(msg);
			return gson.toJson(accesslog.get(id));
		});
		
		get("/accessdevice/log", (req, res) -> {
			
			return accesslog.toJson();
		});
		
		get("/accessdevice/log/:id", (req, res) -> {
			int id1 = -1;
			Gson gson = new Gson();
			
			try {
				id1 = Integer.parseInt(req.params(":id"));
			} catch(Exception e) {
				System.out.println("Not valid id");
				e.printStackTrace();
				
			}
			return gson.toJson(accesslog.get(id1));
		});
		
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			AccessCode a = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(a.getAccesscode());
			return req.body();
		});
		
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode);
		});
		
		delete("accessdevice/log/", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});
		
    }
    
}
