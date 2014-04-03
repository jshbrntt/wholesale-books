package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import menus.Action;
import menus.Menu;
import types.Function;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 6 of the assignment.
 *
 * @author Joshua Barnett
 */
public class SalesPerfomanceReport implements Action {

	@Override
	public boolean execute() {

		try {

			// Get table to use when prompting the user for input.
			String sql = "SELECT orderdate AS start_date, orderdate AS end_date FROM shoporder;";
			ResultSet shopOrder = Database.executeQuery(sql);

			// Check the query executed correctly and hasn't returned an empty result set.
			if (shopOrder != null && shopOrder.next()) {

				// Prompt the user for a date range using the meta data of the result set.
				Map<String, Object> dates = Menu.promptForValues(shopOrder.getMetaData());

				// Get the user's input.
				String startDate = (String) dates.get("start_date");
				String endDate = (String) dates.get("end_date");

				// Form a query using the inputted dates.
				sql = String.format("SELECT * FROM %s ('%s', '%s');",
						Function.SALES_PERFORMANCE_REPORT, startDate, endDate);
				ResultSet salesReport = Database.executeQuery(sql);

				// Check the query executed correctly and hasn't returned an empty result set.
				if (salesReport != null && salesReport.next()) {

					// Print the sales report table.
					Report.printResultSet(salesReport);
					return true;
				}
			}

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}

		return false;

	}

}
