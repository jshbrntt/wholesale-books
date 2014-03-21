package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import menus.Action;
import ui.Report;
import wholesalebooks.Database;

public class EndOfYearProcedure implements Action {

	public static final String END_OF_YEAR = "end_of_year";

	@Override
	public boolean execute() {

		try {
			String sql = String.format("SELECT * FROM %s();", END_OF_YEAR);
			ResultSet bonusReport = Database.executeQuery(sql);
			if (bonusReport != null && bonusReport.next()) {
				Report.printResultSet(bonusReport);
				return true;
			}
		} catch (SQLException ex) {
			Logger.getLogger(EndOfYearProcedure.class.getName()).log(Level.SEVERE, null, ex);
		}

		return false;

	}

}
