package agency;

import java.util.Map.Entry;
import com.google.gson.Gson;
import express.DynExpress;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;

public class AppointmentController {

	// Create
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
					if (!appointment.Validate()) {
						Error error = new Error("Invalid appointment!");
						response = g.toJson(error);
					} else {
						App.appointments.put(appointment.id, g.toJson(appointment));
						response = g.toJson(appointment);
					}
				}
			}
		}
		res.setContentType("application/json");
		res.send(response);
	}

	// Read
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
		res.setContentType("application/json");
		res.send(response);
	}

	// Update
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
					if (!appointment.Validate()) {
						Error error = new Error("Invalid appointment!");
						response = g.toJson(error);
					} else {
						App.appointments.put(appointment.id, g.toJson(appointment));
						response = g.toJson(appointment);
					}
				}
			}
		}
		res.setContentType("application/json");
		res.send(response);
	}

	// Delete
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
		res.setContentType("application/json");
		res.send(response);
	}

	// Read all
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
		res.setContentType("application/json");
		res.send(response);
	}

	// Read all for user
	@DynExpress(context = "/appointments/user/:id", method = RequestMethod.GET)
	public void readForUser(Request req, Response res) {
		String response = "";
		String userid = req.getParam("id");

		response = Appointment.readForUser(userid);

		res.setContentType("application/json");
		res.send(response);
	}

	// Delete all for user
	@DynExpress(context = "/appointments/user/:id", method = RequestMethod.DELETE)
	public void deleteForUser(Request req, Response res) {
		String response = "";
		String userid = req.getParam("id");

		response = Appointment.deleteForUser(userid);

		res.setContentType("application/json");
		res.send(response);
	}

}

class Appointment {

	public String id;
	public String title;
	public String from;
	public String to;
	public String[] users;

	public Appointment(String id, String title, String from, String to, String[] users) {
		this.id = id;
		this.title = title;
		this.from = from;
		this.to = to;
		this.users = users;
	}

	// Validate appointment before storing to database
	public boolean Validate() {
		boolean allowed = true;
		Gson g = new Gson();

		// Check id, from, to dates
		if (this.id.length() == 0 || this.from.length() == 0 || this.to.length() == 0) {
			allowed = false;
			return allowed;
		}
		if (Util.GreaterOrEqual(this.from, this.to)) {
			allowed = false;
			return allowed;
		}

		// Check for conflicts
		for (Entry<String, String> entry : App.appointments.entrySet()) {
			String json = entry.getValue();
			Appointment appointment = g.fromJson(json, Appointment.class);

			if (appointment.id.equals(this.id)) {
				// Don't check same appointment
				continue;
			}

			// Find user in selected appointment
			boolean found = false;
			for (String userInThisAppointment : this.users) {
				for (String userInOtherAppointment : appointment.users) {
					if (userInOtherAppointment.equals(userInThisAppointment)) {
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}

			if (found) {
				// User is found, check is appointment conflicting
				if (Util.LessOrEqual(appointment.from, this.to) && Util.GreaterOrEqual(appointment.to, this.from)) {
					allowed = false;
					break;
				}
			}
		}

		return allowed;
	}

	public static String deleteForUser(String userid) {
		String response = "[";
		Gson g = new Gson();

		for (Entry<String, String> entry : App.appointments.entrySet()) {
			String json = entry.getValue();
			Appointment appointment = g.fromJson(json, Appointment.class);

			// Find user in selected appointment
			boolean found = false;
			for (String element : appointment.users) {
				if (element.equals(userid)) {
					found = true;
					break;
				}
			}

			if (found) {
				App.appointments.remove(appointment.id);
				response += json + ",";
			}
		}
		if (response.length() > 1) {
			response = Util.removeLastChar(response);
		}
		response += "]";
		return response;
	}

	public static String readForUser(String userid) {
		String response = "[";
		Gson g = new Gson();

		for (Entry<String, String> entry : App.appointments.entrySet()) {
			String json = entry.getValue();
			Appointment appointment = g.fromJson(json, Appointment.class);

			// Find user in selected appointment
			boolean found = false;
			for (String element : appointment.users) {
				if (element.equals(userid)) {
					found = true;
					break;
				}
			}

			if (found) {
				response += json + ",";
			}
		}
		if (response.length() > 1) {
			response = Util.removeLastChar(response);
		}
		response += "]";
		return response;
	}

}
