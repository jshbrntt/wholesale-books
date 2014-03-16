package actions;

import menus.Action;
import wholesalebooks.Database;

public class SetupTables implements Action {

	public static final String SETUP_TABLES = "sql/setup-tables.sql";
	public static final String INSERT_DATA = "sql/insert-data.sql";
	
	@Override
	public boolean execute() {

		System.out.println("Creating tables...");
		boolean setupTables = Database.executeSQLFile(SETUP_TABLES);
		
		System.out.println("Inserting data...");
		boolean insertData = Database.executeSQLFile(INSERT_DATA);
		
		return setupTables && insertData;
		
	}
	
}
