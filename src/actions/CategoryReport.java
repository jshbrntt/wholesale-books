package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import menus.Action;
import types.Book;
import types.Category;
import ui.Report;
import wholesalebooks.Database;

public class CategoryReport implements Action {

	public static final String AVERAGE_PRICE = "averageprice";
	public static final String BOOKS = "books";
	public static final String REPORT = "report";

	@Override
	public boolean execute() {

		String sql = String.format(
				"SELECT %s, "
				+ "ROUND(AVG(%s),2) AS %s, "
				+ "COUNT(%s) AS %s "
				+ "FROM (%s NATURAL JOIN %s) "
				+ "GROUP BY %s "
				+ "ORDER BY %s DESC;",
				Category.NAME,
				Book.PRICE, AVERAGE_PRICE,
				Category.ID, BOOKS,
				Book.TABLE, Category.TABLE,
				Category.NAME,
				BOOKS
		);

		ResultSet report = Database.executeQuery(sql);
		if (report != null) {
			Report.showResultSet("Catrgory Report", report);
		}

		sql = String.format(
				"SELECT SUM(%s), SUM(%s)"
				+ "FROM ("
				+ "SELECT ROUND(AVG(%s),2) AS %s, "
				+ "COUNT(%s) AS %s "
				+ "FROM (%s NATURAL JOIN %s) "
				+ "GROUP BY %s"
				+ ") %s;",
				AVERAGE_PRICE, BOOKS,
				Book.PRICE, AVERAGE_PRICE,
				Category.ID, BOOKS,
				Book.TABLE, Category.TABLE,
				Category.NAME,
				REPORT
		);


		ResultSet summary = Database.executeQuery(sql);
		try {
			if (summary.next()) {
				System.out.printf("\nSummary:\nTotal Average Price: £%s\nTotal Books: %s\n\n",
						summary.getString(1),
						summary.getString(2)
				);
			}
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return false;
		}

		return true;

	}

}
