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
 * Action 1 of the assignment.
 *
 * @author Joshua Barnett
 */
public class CreateCategory implements Action {

	@Override
	public boolean execute() {

		try {

			// Form a query to obtain the entire category table for meta data.
			String sql = String.format("SELECT * FROM %s;", Category.TABLE);
			ResultSet rs = Database.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			// Prompt user for values relative to result set meta data.
			Map<String, Object> values = Menu.promptForValues(rsmd);
			for (String key : values.keySet()) {
				// Validate the user's input.
				switch (key) {
					case Category.ID:
						int id = (int) values.get(key);
						if (id < 0) {
							System.out.printf("Invalid '%s' must be postive integer.\n",
									Category.ID);
							return false;
						}
						break;
					case Category.TYPE:
						String type = (String) values.get(key);
						if (!type.equalsIgnoreCase(Category.FICTION) && !type.equalsIgnoreCase(Category.NONFICTION)) {
							System.out.printf("Invalid '%s' must be either '%s' or '%s' (case insesitive).\n",
									Category.TYPE,
									Category.FICTION,
									Category.NONFICTION);
							return false;
						}
						break;
				}
			}

			int id = (int) values.get(Category.ID);
			String name = (String) values.get(Category.NAME);
			String type = (String) values.get(Category.TYPE);

			// Check for overlapping entries...
			sql = String.format(
					"SELECT * FROM %s WHERE %s = %d OR %s = '%s';",
					Category.TABLE, Category.ID, id, Category.NAME, name
			);
			ResultSet overlap = Database.executeQuery(sql);

			// If there are overlapping entries then print the conflicting category.
			if (overlap.next()) {

				System.out.println("Conflicts with existing category:");
				Category.print(
						overlap.getInt(Category.ID),
						overlap.getString(Category.NAME),
						overlap.getString(Category.TYPE)
				);
				return false;

			}
			// Insert new category.
			sql = String.format(
					"INSERT INTO Category VALUES (%d, '%s', '%s');",
					id, name, type
			);

			System.out.println("Attempting to create category...");
			Category.print(id, name, type);

			return Database.execute(sql);

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return false;
		}

	}
}
