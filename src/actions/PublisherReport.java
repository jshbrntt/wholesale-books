package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import menus.Menu;
import types.Publisher;
import ui.Report;
import wholesalebooks.Database;

public class PublisherReport implements Action {

	public static final String PUBLISHER_REPORT = "publisher_report";

	@Override
	public boolean execute() {

		try {
			String sql = String.format(
					"SELECT %s FROM %s;",
					Publisher.NAME, Publisher.TABLE
			);
			
			String publisher_name = null;

			ResultSet publishers = Database.executeQuery(sql);
			if (publishers != null && publishers.next()) {
				ResultSetMetaData metaData = publishers.getMetaData();
				publisher_name = (String) Menu.promptForValues(metaData).get(Publisher.NAME);

			}

			if (publisher_name != null) {
				sql = String.format(
						"SELECT * FROM %s ('%s');",
						PUBLISHER_REPORT, publisher_name
				);
				ResultSet report = Database.executeQuery(sql);
				if (report != null && report.next()) {
					Report.printResultSet(report);
					return true;
				} else {
					System.out.println("Publisher does not exist.");
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
