package ui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Report {

	public static void printResultSet(ResultSet rs) {

		try {

			// Reset result set cursor position.
			rs.beforeFirst();

			// Initialize first level of the 2d string array.
			ArrayList<ArrayList<String>> cache = new ArrayList<>();

			// Get result set meta data for column information.
			ResultSetMetaData rsmd = rs.getMetaData();

			// Get number of columns in the result set.
			int columnCount = rsmd.getColumnCount();

			// Initialize integer array for recording the longest lengths for each column.
			int[] maxLengths = new int[columnCount];

			// Insert column labels into cache, and include them in max length calculation.
			for (int i = 0; i < columnCount; i++) {
				cache.add(new ArrayList<String>());
				String value = rsmd.getColumnLabel(i + 1);
				maxLengths[i] = Math.max(maxLengths[i], value.length());
				cache.get(i).add(0, value);
			}

			// Insert column values into cache after the labels, and update max length value.
			while (rs.next()) {
				for (int i = 0; i < columnCount; i++) {
					String value = rs.getString(i + 1);
					maxLengths[i] = Math.max(maxLengths[i], value.length());
					cache.get(i).add(value);
				}
			}

			// Use the max length value to insert spacer characters after column labels.
			for (int i = 0; i < columnCount; i++) {
				char[] spacer = new char[maxLengths[i]];
				Arrays.fill(spacer, '-');
				cache.get(i).add(1, new String(spacer));
			}

			// Get number of rows from the cached data.
			int rowCount = cache.get(0).size();

			// Iterate over the cache (2d string array) printing values with column seperator characters.
			for (int y = 0; y < rowCount; y++) {
				for (int x = 0; x < columnCount; x++) {
					System.out.print("| ");
					String value = cache.get(x).get(y);
					int buffer = (maxLengths[x] - value.length());
					for (int i = 0; i < buffer; i++) {
						value += " ";
					}
					System.out.print(value);
					System.out.print(" ");
				}
				System.out.println("|");
			}
			
			// Spacer between next bit of output.
			System.out.println();

		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
