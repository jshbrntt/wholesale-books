package menus;

/**
 * A menu item of menu.
 *
 * @author Joshua Barnett
 */
public class MenuItem {

	/**
	 * The label of the menu item which will be printed.
	 */
	public String label;

	/**
	 * The action you want to trigger when this item is selected by the user.
	 */
	public Action action;

	/**
	 * Creates a MenuItem object.
	 *
	 * @param label The menu item's label.
	 * @param action The action you want to trigger upon selection.
	 */
	public MenuItem(String label, Action action) {
		this.label = label;
		this.action = action;
	}

}
