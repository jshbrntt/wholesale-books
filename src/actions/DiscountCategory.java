package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import menus.Action;
import ui.Report;
import wholesalebooks.Database;

public class DiscountCategory implements Action {

	@Override
	public boolean execute() {
		try {

			String sql;
			Scanner scanner;
			double percentage;
			int categoryid;

			// INPUT:
			while (true) {
				System.out.println("\ncategory_id:");
				scanner = new Scanner(System.in);
				try {
					
					categoryid = scanner.nextInt();
					System.out.println();
					
					if (categoryid < 0) {
						throw new InputMismatchException();
					}

					sql = "SELECT * FROM book WHERE categoryid = " + categoryid + ";";
					ResultSet category = Database.executeQuery(sql);

					if (category.next()) {
						break;
					} else {
						System.out.println("Category contains no books.");
						return false;
					}

				} catch (InputMismatchException ex) {
					System.out.println("No category with that 'categoryid' exists.");
				}
			}
			
			while (true) {
				System.out.println("\ndiscount_percentage:");
				scanner = new Scanner(System.in);
				try {
					percentage = scanner.nextDouble();
					System.out.println();
					if (percentage <= 0 || percentage >= 100.00) {
						throw new InputMismatchException();
					} else {
						break;
					}
				} catch (InputMismatchException ex) {
					System.out.println("Invalid Input:\nMust be a valid non-negative percentage between '0' and '100'.");
				}
			}
			
			ResultSet discounted = Database.executeQuery(
					String.format(
							"SELECT * FROM discount_category(%s, %s);",
							Integer.toString(categoryid), Double.toString(percentage)
					)
			);
			
			if (discounted != null && discounted.next()) {
				Report.showResultSet("Discounted Books", discounted);
			}

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}

		return false;
	}

}
