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

	@Override
	public boolean execute() {

		try {
			String sql = String.format(
					"SELECT %s FROM %s;",
					Publisher.NAME, Publisher.TABLE
			);
			String publisher = null;

			ResultSet publishers = Database.executeQuery(sql);
			if (publishers != null) {
				ResultSetMetaData metaData = publishers.getMetaData();
				publisher = (String) Menu.promptForValues(metaData).get(Publisher.NAME);

			}

			if (publisher != null) {
				sql = String.format(
						"SELECT * FROM pub_report('%s');",
						publisher
				);
				ResultSet report = Database.executeQuery(sql);
				if (report.next()) {
					Report.showResultSet(String.format("Publisher Report: %s", publisher), report);
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
