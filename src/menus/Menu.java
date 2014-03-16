package menus;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {

	public String label;
	public List<MenuItem> items;

	public Menu(String label, List<MenuItem> items) {
		this.label = label;
		this.items = items;
	}

	public int prompt() {
		System.out.printf("\n%s:\n", label);
		for (int i = 0; i < items.size(); i++) {
			System.out.printf("%d:\t%s\n", i, items.get(i).label);
		}
		int choice;
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("\nChoice:\t\n");
			try {
				choice = scanner.nextInt();
				System.out.println();
			} catch (InputMismatchException ex) {
				System.out.println("Invalid Input!");
				scanner.next();
				continue;
			}
			try {
				MenuItem item = items.get(choice);
				boolean status = item.action.execute();
				System.out.printf("%s: %s!\n", item.label, (status ? "Successful" : "Failed"));
				return choice;
			} catch (IndexOutOfBoundsException ex) {
				System.out.println("Invalid Selection!");
			}
		}
	}

	public static Map<String, Object> promptForValues(ResultSetMetaData metaData) {
		try {
			return promptForValues(metaData, 1, metaData.getColumnCount());
		} catch (SQLException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return null;
		}

	}

	public static Map<String, Object> promptForValues(ResultSetMetaData metaData, int start, int end) {
		
		Map<String, Object> values = new HashMap();
		try {
			
			if (start < 1 || start > metaData.getColumnCount()) {
				throw new IndexOutOfBoundsException("Illegal start index!");
			}
				
			if (end < 1 || end > metaData.getColumnCount()) {
				throw new IndexOutOfBoundsException("Illegal end index!");
			}
			
			Scanner scanner = new Scanner(System.in);
			
			for (int i = start; i <= end; i++) {
				
				int type = metaData.getColumnType(i);
				int precision = metaData.getPrecision(i);
				String label = metaData.getColumnLabel(i);
				
				while (true) {
					
					System.out.printf("%s:\t\n", label);
					try {
						
						scanner = new Scanner(System.in);
						
						switch (type) {
							
							case Types.INTEGER:

								int integer = scanner.nextInt();
								
								values.put(label, integer);

								break;
								
							case Types.VARCHAR:

								String varchar = scanner.nextLine();

								if (varchar.length() > precision) {
									throw new InputMismatchException(
											String.format(
													"String is too long for VARCHAR precision. (%d, %d)",
													varchar.length(),
													precision)
									);
								}

								values.put(label, varchar);

								break;
						}
						if (values.containsKey(label)) {
							System.out.println();
							break;
						}
					} catch (InputMismatchException ex) {
						String message = ex.getMessage();
						if (message != null) {
							System.out.printf("Invalid Input:\t%s\n\n", message);
						} else {
							System.out.println("Invalid Input\n");
						}
						scanner.nextLine();
					}
				}
			}
		} catch (SQLException | IndexOutOfBoundsException ex) {
			if (ex.getMessage() != null) {
				System.out.println(ex.getMessage());
			}
			return null;
		}
		
		return values;

	}
}
