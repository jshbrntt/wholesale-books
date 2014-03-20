package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import menus.Action;
import ui.Report;
import wholesalebooks.Database;

public class CategoryReport implements Action {

	public static final String CATEGORY_REPORT = "category_report";
	public static final String CATEGORY_REPORT_SUMMARY_LINE = "category_report_summary_line";

	@Override
	public boolean execute() {


		try {
			
			// Category Report:
			String sql = String.format("SELECT * FROM %s;", CATEGORY_REPORT);
			ResultSet report = Database.executeQuery(sql);
			if (report != null && report.next()) {
				Report.showResultSet("Category Report", report);
			}

			// Summary Line:
			sql = String.format("SELECT * FROM %s;", CATEGORY_REPORT_SUMMARY_LINE);
			ResultSet summary = Database.executeQuery(sql);
			ResultSetMetaData metaData = summary.getMetaData();
			if (summary.next()) {
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
