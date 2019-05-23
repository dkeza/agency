package agency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import com.google.gson.Gson;

// Collection of utilities
public class Util {

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

	public static boolean LessOrEqual(String from, String to) {
		boolean lessOrEqual = false;
		ZonedDateTime fromDateTime = Util.ConvertDateTime(from);
		ZonedDateTime toDateTime = Util.ConvertDateTime(to);
		int compareResult = fromDateTime.compareTo(toDateTime);

		if (compareResult == -1 || compareResult == 0) {
			lessOrEqual = true;
		}

		return lessOrEqual;
	}

	public static boolean GreaterOrEqual(String from, String to) {
		boolean greaterOrEqual = false;
		ZonedDateTime fromDateTime = Util.ConvertDateTime(from);
		ZonedDateTime toDateTime = Util.ConvertDateTime(to);
		int compareResult = fromDateTime.compareTo(toDateTime);

		if (compareResult == 1 || compareResult == 0) {
			greaterOrEqual = true;
		}

		return greaterOrEqual;
	}

	public static ZonedDateTime ConvertDateTime(String jsonDateTime) {
		ZonedDateTime javaDateTime = ZonedDateTime.parse(jsonDateTime);
		return javaDateTime;
	}

}

// Used to create JSON with error message
class Error {
	public String error;

	public Error(String error) {
		this.error = error;
	}
}
