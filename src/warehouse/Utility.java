package warehouse;

import java.util.Scanner;

public class Utility {
    public Scanner sc = new Scanner(System.in);

    //Check for empty strings
    public String emptyStringValidator(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();

            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }

            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    //Check for Integers
    public int integerValidator(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid whole number.");
            }
        }
    }

    //Check for Double
    public double doubleValidator(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();

            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid whole number.");
            }

        }
    }

    //Check correct Role
    public <T extends Enum<T>> T enumValidator(Class<T> enumClass, String prompt) {
        while (true) {
            System.out.print(prompt + ": "); // Just prints your custom prompt now!
            String input = sc.nextLine().trim().toUpperCase();

            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //Confirm before process
    public boolean confirmationCheck(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("Please enter 'y' for yes or 'n' for no.");
        }
    }

    public String phoneValidator(String prompt) {
        //From internet
        // Allows optional '+' at the start, digits, spaces, hyphens, and parentheses, length 7 to 15
        String phoneRegex = "^(?:\\+44|0)7\\d{9}$";

        while (true) {
            String input = emptyStringValidator(prompt);
            if (input.matches(phoneRegex)) {
                return input;
            }
            System.out.println("Invalid phone number format. Please enter a valid phone number (e.g., 07123456789 or +447123456789).");
        }
    }

}
