package ui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {

	private ResultSet rs;
	private ResultSetMetaData rsmd;
	private int columnCount;
	private ArrayList<String> columnNames;
	private HashMap<String, ArrayList<Object>> table;

	public void setResultSet(ResultSet rs) {

		try {

			columnNames = new ArrayList<>(columnCount);
			table = new HashMap<>(columnCount);

			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();

			// Initialize columns...
			for (int i = 1; i <= columnCount; i++) {
				String columnName = rsmd.getColumnName(i);
				columnNames.add(columnName);
				table.put(columnName, new ArrayList<>(columnCount));
			}

			// Parse result set to column lists...
			rs.beforeFirst();
			while (rs.next()) {
				for (int i = 0; i < columnNames.size(); i++) {
					String columnName = columnNames.get(i);
					table.get(columnName).add(rs.getObject(columnName));
				}
			}
			
			fireTableStructureChanged();
			
		} catch (SQLException | IndexOutOfBoundsException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}

	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return table.get(columnNames.get(columnIndex)).get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return table.get(columnNames.get(0)).size();
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}
}
