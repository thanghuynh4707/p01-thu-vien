package library;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    private final Scanner scanner;

    public InputHelper() {
        scanner = new Scanner(System.in);
    }

    // Nhap chuoi khong rong
    public String inputNonEmpty(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty!");
        }
    }

    // Nhap ma: khong rong, toi da 20 ky tu, khong co khoang trang
    public String inputId(String message) {
        while (true) {
            String id = inputNonEmpty(message);

            if (id.length() <= 20 && !id.contains(" ")) {
                return id;
            }

            System.out.println(
                "ID must have at most 20 characters and no spaces!"
            );
        }
    }

    // Nhap so nguyen
    public int inputInt(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer!");
            }
        }
    }

    // Nhap so tien kieu long
    public long inputLong(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            try {
                long number = Long.parseLong(value);

                if (number >= 0) {
                    return number;
                }

                System.out.println("Value cannot be negative!");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    // Nhap ngay theo dinh dang yyyy-MM-dd
    public LocalDate inputDate(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println(
                    "Invalid date! Please use yyyy-MM-dd."
                );
            }
        }
    }

    // Nhap lua chon co gioi han
    public int inputChoice(String message, int min, int max) {
        while (true) {
            int choice = inputInt(message);

            if (choice >= min && choice <= max) {
                return choice;
            }

            System.out.println(
                "Please choose a number from " + min + " to " + max + "."
            );
        }
    }

    // Nhap lua chon Yes/No
    public boolean confirm(String message) {
        while (true) {
            System.out.print(message + " (Y/N): ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            }

            if (answer.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Please enter Y or N!");
        }
    }
}
