package agency;

import java.util.concurrent.ConcurrentHashMap;
import express.Express;

public class App {

	// In memory storage for JSON
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