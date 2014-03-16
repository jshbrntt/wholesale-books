package types;

public class Category {

	public static final String TABLE = "category";
	public static final String ID = "categoryid";
	public static final String NAME = "name";
	public static final String TYPE = "categorytype";

	public static final String FICTION = "Fiction";
	public static final String NONFICTION = "Non-fiction";

	public static void print(int id, String name, String type) {

		System.out.printf("\n%s:\t%d\n", Category.ID, id);
		System.out.printf("%s:\t\t%s\n", Category.NAME, name);
		System.out.printf("%s:\t%s\n\n", Category.TYPE, type);

	}
}
