package agency;

import express.DynExpress;
import express.Express;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

public class App {

	public static ConcurrentHashMap<String, String> users;
	public static ConcurrentHashMap<String, String> appointments;

	static {
		users = new ConcurrentHashMap<>();
		appointments = new ConcurrentHashMap<>();
	}

	public static void main(String[] args) {
		Integer port = 8080;
		Express app = new Express();
		app.bind(new UserController());
		app.bind(new AppointmentController());
		System.out.println("Listening on port " + port);
		app.listen(port);
	}
}

class UserController {

	@DynExpress(context = "/users", method = RequestMethod.GET)
	public void readAll(Request req, Response res) {
		String response = "[";
		for (Entry<String, String> entry : App.users.entrySet()) {
			response += entry.getValue() + ",";
		}
		if (response.length() > 1) {
			response = Util.removeLastChar(response);
		}
		response += "]";
		res.send(response);
	}

	@DynExpress(context = "/users/:id", method = RequestMethod.GET)
	public void read(Request req, Response res) {
		String response = "";
		String id = req.getParam("id");
		String json = App.users.get(id);
		if (json == null) {
			response = Util.getError("User with id " + id + " doesnt exists!");
		} else {
			response = json;
		}
		res.send(response);
	}

	@DynExpress(context = "/users", method = RequestMethod.PATCH)
	public void update(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json == "") {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			User user = g.fromJson(json, User.class);
			if (user.id == "") {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.users.get(user.id);
				if (storedJson == null) {
					Error error = new Error("User with id " + user.id + " doesnt exists!");
					response = g.toJson(error);
				} else {
					App.users.put(user.id, g.toJson(user));
					response = g.toJson(user);
				}
			}
		}
		res.send(response);
	}

	@DynExpress(context = "/users/:id", method = RequestMethod.DELETE)
	public void delete(Request req, Response res) {
		String response = "";
		String id = req.getParam("id");
		String json = App.users.get(id);
		if (json == null) {
			response = Util.getError("User with id " + id + " doesnt exists!");
		} else {
			App.users.remove(id);
			response = json;

			// Also must delete child records in appointments

		}
		res.send(response);
	}

	@DynExpress(context = "/users", method = RequestMethod.POST)
	public void create(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json == "") {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			User user = g.fromJson(json, User.class);
			if (user.id == "") {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.users.get(user.id);
				if (storedJson != null) {
					Error error = new Error("User with id " + user.id + " exists!");
					response = g.toJson(error);
				} else {
					App.users.put(user.id, g.toJson(user));
					response = g.toJson(user);
				}
			}
		}
		res.send(response);
	}
}

class User {
	public String id;
	public String name;

	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}
}

class AppointmentController {

	@DynExpress(context = "/appointments", method = RequestMethod.GET)
	public void readAll(Request req, Response res) {
		String response = "[";
		for (Entry<String, String> entry : App.appointments.entrySet()) {
			response += entry.getValue() + ",";
		}
		if (response.length() > 1) {
			response = Util.removeLastChar(response);
		}
		response += "]";
		res.send(response);
	}

	@DynExpress(context = "/appointments/:id", method = RequestMethod.GET)
	public void read(Request req, Response res) {
		String response = "";
		String id = req.getParam("id");
		String json = App.appointments.get(id);
		if (json == null) {
			response = Util.getError("Appointment with id " + id + " doesnt exists!");
		} else {
			response = json;
		}
		res.send(response);
	}

	@DynExpress(context = "/appointments", method = RequestMethod.PATCH)
	public void update(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json == "") {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			Appointment appointment = g.fromJson(json, Appointment.class);
			if (appointment.id == "") {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.appointments.get(appointment.id);
				if (storedJson == null) {
					Error error = new Error("Appointment with id " + appointment.id + " doesnt exists!");
					response = g.toJson(error);
				} else {
					App.appointments.put(appointment.id, g.toJson(appointment));
					response = g.toJson(appointment);
				}
			}
		}
		res.send(response);
	}

	@DynExpress(context = "/appointments/:id", method = RequestMethod.DELETE)
	public void delete(Request req, Response res) {
		String response = "";
		String id = req.getParam("id");
		String json = App.appointments.get(id);
		if (json == null) {
			response = Util.getError("Appointment with id " + id + " doesnt exists!");
		} else {
			App.appointments.remove(id);
			response = json;
		}
		res.send(response);
	}

	@DynExpress(context = "/appointments/user/:id", method = RequestMethod.DELETE)
	public void deleteForUser(Request req, Response res) {
		String response = "[";
		String userid = req.getParam("id");
		Gson g = new Gson();

		for (Entry<String, String> entry : App.appointments.entrySet()) {
			String json = entry.getValue();
			Appointment appointment = g.fromJson(json, Appointment.class);
			if (appointment.userid.equals(userid)) {
				App.appointments.remove(appointment.id);
				response += json + ",";
			}
		}
		if (response.length() > 1) {
			response = Util.removeLastChar(response);
		}
		response += "]";
		res.send(response);
	}

	@DynExpress(context = "/appointments", method = RequestMethod.POST)
	public void create(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json == "") {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			Appointment appointment = g.fromJson(json, Appointment.class);
			if (appointment.id == "") {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.appointments.get(appointment.id);
				if (storedJson != null) {
					Error error = new Error("Appointment with id " + appointment.id + " exists!");
					response = g.toJson(error);
				} else {
					App.appointments.put(appointment.id, g.toJson(appointment));
					response = g.toJson(appointment);
				}
			}
		}
		res.send(response);
	}
}

class Appointment {
	public String id;
	public String title;
	public String from;
	public String to;
	public String userid;

	public Appointment(String id, String title, String from, String to, String userid) {
		this.id = id;
		this.title = title;
		this.from = from;
		this.to = to;
		this.userid = userid;
	}
}

class Error {
	public String error;

	public Error(String error) {
		this.error = error;
	}
}

class Util {

	public static String ConvertToString(InputStream in) {
		String json = "";
		try {
			json = Util.fromStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String fromStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		return out.toString();
	}

	public static String removeLastChar(String str) {
		return str.substring(0, str.length() - 1);
	}

	public static String getError(String errorText) {
		Error error = new Error(errorText);
		Gson g = new Gson();
		return g.toJson(error);
	}

}
