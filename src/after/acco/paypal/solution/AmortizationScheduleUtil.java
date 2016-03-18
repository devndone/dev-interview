package after.acco.paypal.solution;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;

public class AmortizationScheduleUtil {
	
	public static void printf(final Console console, final String formatString, Object... args) {
		
		try {
			if (console != null) {
				console.printf(formatString, args);
			} else {
				System.out.print(String.format(formatString, args));
			}
		} catch (IllegalFormatException e) {
			System.err.print("Error printing...\n");
		}
	}
	
	public static void print(final Console console, String s) {
		printf(console, "%s", s);
	}
	
	public static String readLine(final Console console, String userPrompt) throws IOException {
		String line = "";
		
		if (console != null) {
			line = console.readLine(userPrompt);
		} else {
			// print("console is null\n");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			print(console, userPrompt);
			line = bufferedReader.readLine();
		}
		line.trim();
		return line;
	}

}
