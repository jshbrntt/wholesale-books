package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import menus.Action;
import types.Function;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 7 of the assignment.
 *
 * @author Joshua barnett
 */
public class DiscountCategory implements Action {

	@Override
	public boolean execute() {
		try {

			String sql;
			Scanner scanner;
			double percentage;
			int categoryid;

			while (true) {

				// Prompt the user for a categoryid.
				System.out.println("\ncategoryid:");
				scanner = new Scanner(System.in);
				try {

					categoryid = scanner.nextInt();
					System.out.println();

					// Validate the input.
					if (categoryid < 0) {
						throw new InputMismatchException();
					}

					// Check the category has associated books.
					sql = "SELECT * FROM book WHERE categoryid = " + categoryid + ";";
					ResultSet category = Database.executeQuery(sql);

					// Check if the result set is empty.
					if (category.next()) {
						break;
					} else {
						System.out.println("There are no books associated with this category.");
						return false;
					}

				} catch (InputMismatchException ex) {
					System.out.println("No category with that 'categoryid' exists.");
				}
			}

			// Prompt the user for a discount percentage.
			while (true) {
				System.out.println("\ndiscount_percentage:");
				scanner = new Scanner(System.in);
				try {
					percentage = scanner.nextDouble();
					System.out.println();

					// Validating the percentage.
					if (percentage <= 0 || percentage >= 100.00) {
						throw new InputMismatchException();
					} else {
						break;
					}
				} catch (InputMismatchException ex) {
					System.out.println("Invalid Input:\nMust be a valid non-negative percentage between '0' and '100'.");
				}
			}

			// Executing the discount category function using the valid percentage.
			ResultSet discounted = Database.executeQuery(
					String.format(
							"SELECT * FROM %s(%s, %s);",
							Function.DISCOUNT_CATEGORY, Integer.toString(categoryid), Double.toString(percentage)
					)
			);

			// Check the query executed correct and that the returned result isn't empty.
			if (discounted != null && discounted.next()) {
				Report.printResultSet(discounted);
			}

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}

		return false;
	}

}
