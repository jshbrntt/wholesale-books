package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import menus.Menu;
import types.Book;
import types.Function;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 5 of the assignment.
 *
 * @author Joshua Barnett
 */
public class BookOrderHistory implements Action {

	@Override
	public boolean execute() {

		try {

			// Get the book table to prompt the user for specific data.
			String sql = String.format(
					"SELECT * FROM %s;",
					Book.TABLE
			);

			// Assume the bookid entered will be invalid.
			int bookid = -1;

			// A string to store the title of the 
			String title = null;

			// Execute the book table query.
			ResultSet books = Database.executeQuery(sql);

			// Check the result set isn't null.
			if (books != null) {

				// Get the table meta data.
				ResultSetMetaData metaData = books.getMetaData();

				// Use this to prompt the user for the bookid.
				bookid = (int) Menu.promptForValues(metaData, 1, 1).get(Book.ID);

				// Move cursor to the first entry as there should only be one book.
				books.first();

				// Get the book title.
				title = books.getString(Book.TITLE);
			}

			if (bookid >= 0) {

				// Forming the query for the book order summary function.
				sql = String.format(
						"SELECT * FROM %s ('%d');",
						Function.BOOK_ORDER_HISTORY, bookid
				);

				// Execute the query.
				ResultSet report = Database.executeQuery(sql);

				// Check the report isn't empty.
				if (report.next()) {

					// Print the function's output to the command line interface.
					Report.printResultSet(report);
				} else {
					System.out.println("Book does not exist, or has had no orders.");
					return false;
				}

				// Forming the query for the book order history summary function.
				sql = String.format(
						"SELECT * FROM %s ('%d');",
						Function.BOOK_ORDER_HISTORY_SUMMARY, bookid
				);

				// Execute the query.
				ResultSet summary = Database.executeQuery(sql);

				// Check if the returned result set is empty.
				if (summary.next()) {

					// Printing the summary line.
					System.out.printf(
							"\nSummary:\n"
							+ "Title: %s\n"
							+ "Copies Ordered: %s\n"
							+ "Total Selling Value: %s\n\n",
							title, summary.getString(1), summary.getString(2)
					);
					return true;
				} else {
					System.out.println("Book does not exist, or has had no orders.");
				}
			} else {
				System.out.println("'bookid' must be a postive integer.");
			}

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}
		return false;
	}

}
