package wholesalebooks;

import actions.CategoryReport;
import actions.CreateCategory;
import actions.DeleteCategory;
import actions.PublisherReport;
import actions.SetupTables;
import java.util.Arrays;
import menus.Menu;
import menus.MenuItem;

public class WholesaleBooks {

	public final static String setupTablesSQL = "sql/setup-tables.sql";
	public final static String insertDataSQL = "sql/insert-data.sql";

	public static Menu menu = new Menu(
			
			"Wholesale Books",
			Arrays.asList(
					new MenuItem("Setup Tables", new SetupTables()),
					new MenuItem("Create Category", new CreateCategory()),
					new MenuItem("Delete Category", new DeleteCategory()),
					new MenuItem("Category Report", new CategoryReport()),
					new MenuItem("Publisher Report", new PublisherReport())
			)
	);

	public static void main(String[] args) {

		if (Database.connect()) {
			while (true) {
				menu.prompt();
			}
		}

	}
}

//		while (true) {
//			System.out.print("Category ID:\t");
//			categoryID = Input.getPositiveInt();
//			ResultSet resultSet = Database.integerExists("Category", "CategoryID", categoryID);
//			try {
//				resultSet.next();
//				System.out.printf(
//						"Category Already Exists:\n"
//						+ "ID:\t%d\n"
//						+ "Name:\t%s\n"
//						+ "Type:\t%s\n",
//						resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)
//				);
//			} catch (SQLException ex) {
//				break;
//			}
//		}
//
//		while (true) {
//			System.out.print("Category Name:\t");
//			categoryName = Input.getString();
//			ResultSet resultSet = Database.stringExists("Category", "Name", categoryName);
//			try {
//				resultSet.next();
//				System.out.printf(
//						"Category Already Exists:\n"
//						+ "ID:\t%d\n"
//						+ "Name:\t%s\n"
//						+ "Type:\t%s\n",
//						resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)
//				);
//			} catch (SQLException ex) {
//				break;
//			}
//		}
//
//		System.out.print("Category Type:\n0:\tFiction\n1:\tNon-fiction\n");
//		while (true) {
//			System.out.print("Choice:\t");
//			categoryType = CategoryType.getTypeByOrdinal(Input.getPositiveInt());
//			if (categoryType == null) {
//				System.out.println("Invalid Choice!");
//			} else {
//				break;
//			}
//		}
//
//		if (Database.createCategory(categoryID, categoryName, categoryType)) {
//			System.out.printf(
//					"Created Category:\n"
//					+ "ID:\t%d\n"
//					+ "Name:\t%s\n"
//					+ "Type:\t%s\n",
//					categoryID, categoryName, categoryType
//			);
//		} else {
//			System.out.println("Category Creation Failed!");
//		}
