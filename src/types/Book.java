package types;

/**
 * Constants relating to the book table.
 * @author Joshua Barnett
 */
public class Book {

	public static final String TABLE = "book";
	public static final String ID = "bookid";
	public static final String TITLE = "title";
	public static final String PRICE = "price";

	public static final String FICTION = "Fiction";
	public static final String NONFICTION = "Non-fiction";
	
	public static void print(int id, String title, String price, String categoryId, String publisherId) {

		System.out.printf("\n%s:\t%d\n", Book.ID, id);
		System.out.printf("%s:\t%s\n", Book.TITLE, title);
		System.out.printf("%s:\t%d\n", Book.PRICE, price);
		System.out.printf("%s:\t%s\n", Category.ID, categoryId);
		System.out.printf("%s:\t%s\n\n", Publisher.ID, publisherId);

	}
}
