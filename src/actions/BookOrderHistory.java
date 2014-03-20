package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import menus.Menu;
import types.Book;
import ui.Report;
import wholesalebooks.Database;

public class BookOrderHistory implements Action {

	public static final String BOOK_ORDER_HISTORY = "book_order_history";
	public static final String BOOK_ORDER_HISTORY_SUMMARY = "book_order_history_summary";
	
	@Override
	public boolean execute() {

		
		try {
			String sql = String.format(
					"SELECT * FROM %s;",
					Book.TABLE
			);
			int bookid = -1;
			String title = null;

			ResultSet books = Database.executeQuery(sql);
			if (books != null) {
				ResultSetMetaData metaData = books.getMetaData();
				bookid = (int) Menu.promptForValues(metaData, 1, 1).get(Book.ID);
				books.first();
				title = books.getString(Book.TITLE);
			}

			if (bookid >= 0) {
				sql = String.format(
						"SELECT * FROM %s ('%d');",
						BOOK_ORDER_HISTORY, bookid
				);
				ResultSet report = Database.executeQuery(sql);
				if (report.next()) {
					Report.showResultSet(String.format("Book Order History Report: %s", title), report);
				} else {
					System.out.println("Book does not exist.");
					return false;
				}
				
				sql = String.format(
						"SELECT * FROM %s ('%d');",
						BOOK_ORDER_HISTORY_SUMMARY, bookid
				);
				ResultSet summary = Database.executeQuery(sql);
				if (summary.next()) {
					System.out.printf(
							"\nSummary:\n" +
							"Title: %s\n" +
							"Copies Ordered: %s\n" +
							"Total Selling Value: %s\n\n",
							title, summary.getString(1), summary.getString(2)
					);
					return true;
				} else {
					System.out.println("Book does not exist.");
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
