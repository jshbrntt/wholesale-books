package actions;

import java.sql.ResultSet;
import menus.Action;
import ui.Report;
import wholesalebooks.Database;

public class PublisherReport implements Action {

	@Override
	public boolean execute() {

		String publisher = "Black Swan";
		
		String sql = String.format(
				"SELECT * FROM pub_report('%s');",
				publisher
		);
		ResultSet report = Database.executeQuery(sql);

		if (report != null) {
			Report.showResultSet("Publisher Report", report);
		}

		return true;

	}

}
