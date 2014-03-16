package actions;

import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import menus.Action;
import types.Book;
import types.Category;
import ui.ResultSetTableModel;
import wholesalebooks.Database;

public class CategoryReport implements Action {

	public static final String AVERAGE_PRICE = "averageprice";
	public static final String TOTAL_BOOKS = "totalbooks";

	@Override
	public boolean execute() {

		String sql = String.format(
				"SELECT %s, "
				+ "ROUND(AVG(%s),2) AS %s, "
				+ "COUNT(%s) AS %s "
				+ "FROM (%s NATURAL JOIN %s) "
				+ "GROUP BY %s "
				+ "ORDER BY %s DESC;",
				Category.NAME,
				Book.PRICE, AVERAGE_PRICE,
				Category.ID, TOTAL_BOOKS,
				Book.TABLE, Category.TABLE,
				Category.NAME,
				TOTAL_BOOKS
		);

		JFrame frame = new JFrame();

		ResultSet report = Database.executeQuery(sql);
		if (report != null) {

			// Show Table.
			ResultSetTableModel model = new ResultSetTableModel();
			model.setResultSet(report);
			JTable table = new JTable(model);
			JScrollPane pane = new JScrollPane(table);
			frame.add(pane);

		}

		frame.pack();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setTitle("Category Report");
		frame.setVisible(true);

//		ResultSet summary = Database.executeQuery(sql);
		return false;

	}

}
