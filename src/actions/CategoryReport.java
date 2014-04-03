package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import static types.Function.CATEGORY_REPORT;
import static types.Function.CATEGORY_REPORT_SUMMARY_LINE;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 3 of the assignment.
 *
 * @author Joshua Barnett
 */
public class CategoryReport implements Action {

	@Override
	public boolean execute() {

		try {

			// Forming the query for the category report function.
			String sql = String.format("SELECT * FROM %s;", CATEGORY_REPORT);

			// Execute the query.
			ResultSet report = Database.executeQuery(sql);

			// Check the query didn't fail and isn't empty.
			if (report != null && report.next()) {

				// Print the contents of the report result set.
				Report.printResultSet(report);
			}

			// Forming the query for the category report summary function.
			sql = String.format("SELECT * FROM %s;", CATEGORY_REPORT_SUMMARY_LINE);

			// Execute the query.
			ResultSet summary = Database.executeQuery(sql);

			//Check if the result set is empty.
			if (summary.next()) {
				ResultSetMetaData metaData = summary.getMetaData();
				// Print the summary.
				System.out.printf("\nSummary:\n%s: %s\n%s: %s\n\n",
						metaData.getColumnLabel(1), summary.getString(1),
						metaData.getColumnLabel(2), summary.getString(2)
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
