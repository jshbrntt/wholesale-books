package actions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import menus.Action;
import menus.Menu;
import types.Category;
import wholesalebooks.Database;

/**
 * Action 2 of the assignment.
 *
 * @author Joshua Barnett
 */
public class DeleteCategory implements Action {

	@Override
	public boolean execute() {

		try {

			// Getting the category table to use when prompting the user for input values.
			String sql = String.format("SELECT * FROM %s;", Category.TABLE);
			ResultSet rs = Database.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			// Prompt the user for a categoryid.
			Map<String, Object> values = Menu.promptForValues(rsmd, 1, 1);

			int id = (int) values.get(Category.ID);
			if (id < 0) {
				System.out.printf("Invalid '%s' must be postive integer.\n",
						Category.ID);
				return false;
			}

			// Verifying category exists in the table.
			sql = String.format(
					"SELECT * FROM %s WHERE %s = %d;",
					Category.TABLE, Category.ID, id
			);
			ResultSet overlap = Database.executeQuery(sql);

			// If the category exists.
			if (overlap.next()) {

				System.out.println("Attempting to delete category...");

				// Print the category being deleted.
				Category.print(
						overlap.getInt(Category.ID),
						overlap.getString(Category.NAME),
						overlap.getString(Category.TYPE)
				);

				sql = String.format(
						"DELETE FROM %s WHERE %s = %d;",
						Category.TABLE, Category.ID, id
				);

				return Database.execute(sql);

			} else {
				System.out.printf("No category with that '%s' exists.\n", Category.ID);
				return false;
			}

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return false;
		}

	}

}
