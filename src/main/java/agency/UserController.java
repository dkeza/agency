package agency;

import java.util.Map.Entry;
import com.google.gson.Gson;
import express.DynExpress;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;

public class UserController {

	// Create
	@DynExpress(context = "/users", method = RequestMethod.POST)
	public void create(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json.equals("")) {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			User user = g.fromJson(json, User.class);
			if (user.id.equals("")) {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.users.get(user.id);
				if (storedJson != null) {
					Error error = new Error("User with id " + user.id + " exists!");
					response = g.toJson(error);
				} else {
					if (!user.Validate()) {
						Error error = new Error("Invalid user!");
						response = g.toJson(error);
					} else {
						App.users.put(user.id, g.toJson(user));
						response = g.toJson(user);
					}
				}
			}
		}
		res.setContentType("application/json");
		res.send(response);
	}

	// Read
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
		res.setContentType("application/json");
		res.send(response);
	}

	// Update
	@DynExpress(context = "/users", method = RequestMethod.PATCH)
	public void update(Request req, Response res) {
		String response = "";
		Gson g = new Gson();
		String json = Util.ConvertToString(req.getBody());

		if (json.equals("")) {
			Error error = new Error("Internal error 1");
			response = g.toJson(error);
		} else {
			User user = g.fromJson(json, User.class);
			if (user.id.equals("")) {
				Error error = new Error("Internal error 2");
				response = g.toJson(error);
			} else {
				String storedJson = App.users.get(user.id);
				if (storedJson == null) {
					Error error = new Error("User with id " + user.id + " doesnt exists!");
					response = g.toJson(error);
				} else {
					if (!user.Validate()) {
						Error error = new Error("Invalid user!");
						response = g.toJson(error);
					} else {
						App.users.put(user.id, g.toJson(user));
						response = g.toJson(user);
					}
				}
			}
		}
		res.setContentType("application/json");
		res.send(response);
	}

	// Delete
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
			// In production here should be done HTTP Request to
			// http://localhost:8080/appointments/user/:id
			Appointment.deleteForUser(id);

		}
		res.setContentType("application/json");
		res.send(response);
	}

	// Read all
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
		res.setContentType("application/json");
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

	public boolean Validate() {
		boolean allowed = true;

		// Check id and name
		if (this.id.length() == 0 || this.name.length() == 0) {
			allowed = false;
			return allowed;
		}

		return allowed;
	}
}
