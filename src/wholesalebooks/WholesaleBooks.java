package wholesalebooks;

import actions.BookOrderHistory;
import actions.CategoryReport;
import actions.CreateCategory;
import actions.DeleteCategory;
import actions.DiscountCategory;
import actions.EndOfYearProcedure;
import actions.PublisherReport;
import actions.SalesPerfomanceReport;
import actions.SetupTables;
import java.util.Arrays;
import menus.Menu;
import menus.MenuItem;

/**
 * The main class for wholesale books client.
 *
 * @author Joshua Barnett
 */
public class WholesaleBooks {

	public final static String setupTablesSQL = "sql/setup-tables.sql";
	public final static String insertDataSQL = "sql/insert-data.sql";

	public static Menu menu = new Menu(
			// Setup the command prompt menu and it's associated actions.
			"Wholesale Books",
			Arrays.asList(
					new MenuItem("Setup Tables", new SetupTables()),
					new MenuItem("Create Category", new CreateCategory()),
					new MenuItem("Delete Category", new DeleteCategory()),
					new MenuItem("Category Report", new CategoryReport()),
					new MenuItem("Publisher Report", new PublisherReport()),
					new MenuItem("Book Order History", new BookOrderHistory()),
					new MenuItem("Sales Perfomance Report", new SalesPerfomanceReport()),
					new MenuItem("Discount Category", new DiscountCategory()),
					new MenuItem("End Of Year Procedure", new EndOfYearProcedure())
			)
	);

	public static void main(String[] args) {

		// Connect to the database if successful the prompt the menu until the program is exited.
		if (Database.connect()) {
			while (true) {
				menu.prompt();
			}
		}

	}
}
