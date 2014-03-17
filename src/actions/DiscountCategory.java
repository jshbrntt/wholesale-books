package actions;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import menus.Action;

public class DiscountCategory implements Action {

	@Override
	public boolean execute() {
//		try {
			
			double percentage = 0;

			while (true) {

				System.out.println("\npercentage:");
				// INPUT:
				Scanner scanner = new Scanner(System.in);

				try {
					percentage = scanner.nextDouble();
					if (percentage <= 0) {
						throw new InputMismatchException();
					} else {
						break;
					}
				} catch (InputMismatchException ex) {
					System.out.println("Invalid Input:\nMust be a valid non-negative percentage.");
				}
			}
			
			System.out.println(Double.toString(percentage));
			
			

//		} catch (SQLException ex) {
//			if (ex.getMessage() != null) {
//				System.out.println(ex.getMessage());
//			}
//		}

		return false;
	}

}
