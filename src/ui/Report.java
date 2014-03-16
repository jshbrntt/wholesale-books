package ui;

import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Report {

	public static void showResultSet(String title, ResultSet rs) {

		System.out.println("Displaying Table...");
		
		ResultSetTableModel model = new ResultSetTableModel();
		model.setResultSet(rs);
		
		JTable reportTable = new JTable(model);
		JScrollPane reportPane = new JScrollPane(reportTable);
		JFrame frame = new JFrame();
		
		frame.add(reportPane);
		frame.pack();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setTitle(title);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
}
