package actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import menus.Action;
import types.Function;
import ui.Report;
import wholesalebooks.Database;

/**
 * Action 8 of the assignment
 *
 * @author Joshua Barnett
 */
public class EndOfYearProcedure implements Action {

	@Override
	public boolean execute() {

		try {
			// Forming the query for the end of year procedure.
			String sql = String.format("SELECT * FROM %s();", Function.END_OF_YEAR);
			ResultSet bonusReport = Database.executeQuery(sql);

			// Check the query executed correctly and that the returned result set isn't empty.
			if (bonusReport != null && bonusReport.next()) {

				// Print the bonus report.
				Report.printResultSet(bonusReport);
				return true;
			}
		} catch (SQLException ex) {
			Logger.getLogger(EndOfYearProcedure.class.getName()).log(Level.SEVERE, null, ex);
		}

		return false;

	}

}
