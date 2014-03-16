package wholesalebooks;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {

	public static int getInt() {
		
		int input = 0;
		boolean valid = false;
		do {
			Scanner scanner = new Scanner(System.in);
			try {
				input = scanner.nextInt();
				valid = true;
			} catch (InputMismatchException e) {}
			if (!valid) {
				System.out.println("Integer Required!");
			}
		} while (!valid);
		
		return input;

	}
	
	public static int getPositiveInt() {
		
		int input = 0;
		boolean valid = false;
		do {
			Scanner scanner = new Scanner(System.in);
			try {
				input = scanner.nextInt();
				if (input >= 0) {
					valid = true;
				}
			} catch (InputMismatchException e) {}
			if (!valid) {
				System.out.println("Positive Integer Required!");
			}
		} while (!valid);
		
		return input;

	}
	
	public static String getString() {
		
		String input = null;
		boolean valid = false;
		do {
			Scanner scanner = new Scanner(System.in);
			try {
				input = scanner.nextLine();
				valid = true;
			} catch (InputMismatchException e) {}
			if (!valid) {
				System.out.println("String Required!");
			}
		} while (!valid);
		
		return input;

	}

}
