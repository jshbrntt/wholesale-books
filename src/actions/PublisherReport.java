package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import menus.Menu;
import types.Function;
import types.Publisher;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 4 of the assignment.
 *
 * @author Joshua Barnett
 */
public class PublisherReport implements Action {

	@Override
	public boolean execute() {

		try {

			// Forming an query to get the names of all the publishers.
			String sql = String.format(
					"SELECT %s FROM %s;",
					Publisher.NAME, Publisher.TABLE
			);

			// String for store the inputted publisher's name.
			String publisher_name = null;

			// Execute the query.
			ResultSet publishers = Database.executeQuery(sql);

			// Check the query executed correctly and hasn't returned an empty result set.
			if (publishers != null && publishers.next()) {

				// Prompt the user for a publisher name.
				ResultSetMetaData metaData = publishers.getMetaData();
				publisher_name = (String) Menu.promptForValues(metaData).get(Publisher.NAME);

			}

			// Check the input is valid.
			if (publisher_name != null) {

				// Forming the query for the publisher report.
				sql = String.format(
						"SELECT * FROM %s ('%s');",
						Function.PUBLISHER_REPORT, publisher_name
				);
				ResultSet report = Database.executeQuery(sql);

				// Check the query executed correctly and hasn't returned an empty result set.
				if (report != null && report.next()) {
					Report.printResultSet(report);
					return true;
				} else {
					System.out.println("Publisher does not exist or has no associated books.");
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
