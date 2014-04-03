package menus;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Command line menu utility class for handling the various action.
 *
 * @author Joshua Barnett
 */
public class Menu {

	/**
	 * The menu's label.
	 */
	public String label;

	/**
	 * All of the menu items.
	 */
	public List<MenuItem> items;

	/**
	 * Creates a new Menu object.
	 *
	 * @param label The menu's label.
	 * @param items All of the menu items.
	 */
	public Menu(String label, List<MenuItem> items) {
		this.label = label;
		this.items = items;
	}

	/**
	 * Print the menu and prompt the user for a particular MenuItem.
	 *
	 * @return The menu item that was selected.
	 */
	public int prompt() {
		System.out.printf("\n%s:\n", label);
		for (int i = 0; i < items.size(); i++) {
			System.out.printf("%d:\t%s\n", i, items.get(i).label);
		}
		int choice;
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("\nChoice:\t\n");
			try {
				choice = scanner.nextInt();
				System.out.println();
			} catch (InputMismatchException ex) {
				System.out.println("Invalid Input!");
				scanner.next();
				continue;
			}
			try {
				MenuItem item = items.get(choice);
				boolean status = item.action.execute();
				System.out.printf("%s: %s!\n", item.label, (status ? "Successful" : "Failed"));
				return choice;
			} catch (IndexOutOfBoundsException ex) {
				System.out.println("Invalid Selection!");
			}
		}
	}

	/**
	 * Prompt the user for all the values related to a particular result set.
	 *
	 * @param metaData The meta data you want to obtain values for.
	 * @return A map of all the user's input values.
	 */
	public static Map<String, Object> promptForValues(ResultSetMetaData metaData) {
		try {
			return promptForValues(metaData, 1, metaData.getColumnCount());
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return null;
		}

	}

	/**
	 * Prompt the user for values relating to a particular range of columns in a
	 * result set.
	 *
	 * @param metaData The meta data you want to obtain values for.
	 * @param start The start of the column range.
	 * @param end The end of the column range.
	 * @return A map of all the user's input.
	 */
	public static Map<String, Object> promptForValues(ResultSetMetaData metaData, int start, int end) {

		Map<String, Object> values = new HashMap();
		try {

			if (start < 1 || start > metaData.getColumnCount()) {
				throw new IndexOutOfBoundsException("Illegal start index!");
			}

			if (end < 1 || end > metaData.getColumnCount()) {
				throw new IndexOutOfBoundsException("Illegal end index!");
			}

			Scanner scanner;

			for (int i = start; i <= end; i++) {

				int type = metaData.getColumnType(i);
				int precision = metaData.getPrecision(i);
				String label = metaData.getColumnLabel(i);

				while (true) {

					System.out.printf("%s:\n", label);
					try {

						scanner = new Scanner(System.in);

						// Validate input depending on the SQL type.
						switch (type) {

							case Types.INTEGER:

								int integer = scanner.nextInt();

								values.put(label, integer);

								break;

							case Types.VARCHAR:

								String varchar = scanner.nextLine();

								// Check the user's input isn't longer than the VARCHAR's maximum precision/length.
								if (varchar.length() > precision) {
									throw new InputMismatchException(
											String.format(
													"String is too long for VARCHAR precision. (%d, %d)",
													varchar.length(),
													precision)
									);
								}

								values.put(label, varchar);

								break;

							case Types.DATE:

								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
								String input = scanner.nextLine();
								dateFormat.setLenient(false);

								// Check the user's input matches a particular format.
								try {
									dateFormat.parse(input);
									values.put(label, input);
								} catch (ParseException ex) {
									System.out.println("Invalid date, must be a real date and in the format must be 'yyyy-MM-dd'.");
								}

								break;
						}
						if (values.containsKey(label)) {
							System.out.println();
							break;
						}
					} catch (InputMismatchException ex) {
						String message = ex.getMessage();
						if (message != null) {
							System.out.printf("Invalid Input:\n%s\n\n", message);
						} else {
							System.out.println("Invalid Input\n");
						}
					}
				}
			}
		} catch (SQLException | IndexOutOfBoundsException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return null;
		}

		return values;

	}

}
