/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {

    private LibraryService service;
    private FileStorage storage;
    private Scanner scanner;

    public Menu(LibraryService service, FileStorage storage) {
        if (service == null || storage == null) {
            throw new IllegalArgumentException("service and storage must not be null.");
        }
        this.service = service;
        this.storage = storage;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        try {
            while (running) {
                this.showMenu();
                int choice = this.readInt("Choose an option", 0, 6);
                switch (choice) {
                    case 1:
                        this.handlePublications();
                        break;
                    case 2:
                        this.handleReaders();
                        break;
                    case 3:
                        this.handleBorrow();
                        break;
                    case 4:
                        this.handleReturn();
                        break;
                    case 5:
                        this.handleReports();
                        break;
                    case 6:
                        try {
                        this.storage.save(this.service);
                        System.out.println("Data saved to " + this.storage.getFilePath());
                    } catch (IllegalStateException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    break;
                    case 0:
                        this.handleSaveAndExit();
                        running = false;
                        break;
                    default:
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nNo more input available, program stopped (data not saved).");
        }
    }

    public void showMenu() {
        System.out.println();
        System.out.println("===== LIBRARY MANAGEMENT =====");
        System.out.println("1. Manage publications");
        System.out.println("2. Manage readers");
        System.out.println("3. Borrow");
        System.out.println("4. Return");
        System.out.println("5. Reports and statistics");
        System.out.println("6. Save data");
        System.out.println("0. Exit");
    }

    public void handlePublications() {
        int choice = -1;
        while (choice != 0) {
            System.out.println();
            System.out.println("--- Manage publications ---");
            System.out.println("1. Add book");
            System.out.println("2. Add journal");
            System.out.println("3. List all");
            System.out.println("4. Find by ID");
            System.out.println("5. Search by part of title");
            System.out.println("6. Update");
            System.out.println("7. Delete");
            System.out.println("0. Back");
            choice = this.readInt("Choose", 0, 7);
            try {
                if (choice == 1) {
                    String id = this.readId("Book ID");
                    String title = this.readString("Book title");
                    String author = this.readString("Author");
                    this.service.addPublication(new Book(id, title, author));
                    System.out.println("Book added.");
                } else if (choice == 2) {
                    String id = this.readId("Journal ID");
                    String title = this.readString("Journal title");
                    String issue = this.readString("Issue");
                    this.service.addPublication(new Journal(id, title, issue));
                    System.out.println("Journal added.");
                } else if (choice == 3) {
                    if (this.service.getPublications().isEmpty()) {
                        System.out.println("No publications yet.");
                    }
                    for (Publication p : this.service.getPublications()) {
                        System.out.println(p + " | "
                                + (this.service.isAvailable(p.getId()) ? "available" : "on loan"));
                    }
                } else if (choice == 4) {
                    Publication p = this.service.findPublicationById(this.readId("Publication ID"));
                    System.out.println(p == null ? "Not found." : p.toString());
                } else if (choice == 5) {
                    List<Publication> found
                            = this.service.searchPublicationsByTitle(this.readString("Keyword"));
                    if (found.isEmpty()) {
                        System.out.println("Not found.");
                    }
                    for (Publication p : found) {
                        System.out.println(p);
                    }
                } else if (choice == 6) {
                    String id = this.readId("ID of publication to update");
                    String newTitle = this.readString("New title");
                    String newExtra = this.readString("New author (book) or new issue (journal)");
                    this.service.updatePublication(id, newTitle, newExtra);
                    System.out.println("Updated.");
                } else if (choice == 7) {
                    this.service.deletePublication(this.readId("ID of publication to delete"));
                    System.out.println("Deleted.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Rejected: " + e.getMessage());
            }
        }
    }

    public void handleReaders() {
        int choice = -1;
        while (choice != 0) {
            System.out.println();
            System.out.println("--- Manage readers ---");
            System.out.println("1. Add reader");
            System.out.println("2. List all");
            System.out.println("3. Find by ID");
            System.out.println("4. Update name / contact");
            System.out.println("5. Delete");
            System.out.println("0. Back");
            choice = this.readInt("Choose", 0, 5);
            try {
                if (choice == 1) {
                    String id = this.readId("Reader ID");
                    String name = this.readString("Full name");
                    String contact = this.readString("Contact information");
                    this.service.addReader(new Reader(id, name, contact));
                    System.out.println("Reader added.");
                } else if (choice == 2) {
                    if (this.service.getReaders().isEmpty()) {
                        System.out.println("No readers yet.");
                    }
                    for (Reader r : this.service.getReaders()) {
                        System.out.println(r + " | currently borrowing "
                                + this.service.countActiveLoansOf(r.getId()) + " item(s)");
                    }
                } else if (choice == 3) {
                    Reader r = this.service.findReaderById(this.readId("Reader ID"));
                    System.out.println(r == null ? "Not found." : r.toString());
                } else if (choice == 4) {
                    String id = this.readId("ID of reader to update");
                    String name = this.readString("New full name");
                    String contact = this.readString("New contact");
                    this.service.updateReader(id, name, contact);
                    System.out.println("Updated.");
                } else if (choice == 5) {
                    this.service.deleteReader(this.readId("ID of reader to delete"));
                    System.out.println("Deleted.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Rejected: " + e.getMessage());
            }
        }
    }

    public void handleBorrow() {
        System.out.println();
        System.out.println("--- Borrow ---");
        String readerId = this.readId("Reader ID");
        String publicationId = this.readId("Publication ID");
        LocalDate borrowDate = this.readDate("Borrow date (yyyy-MM-dd)");
        try {
            Loan loan = this.service.borrow(readerId, publicationId, borrowDate);
            System.out.println("Loan created: " + loan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    public void handleReturn() {
        System.out.println();
        System.out.println("--- Return ---");
        String loanId = this.readId("Loan ID");
        LocalDate returnDate = this.readDate("Return date (yyyy-MM-dd)");
        try {
            long fee = this.service.returnLoan(loanId, returnDate);
            Loan loan = this.service.findLoanById(loanId);
            System.out.println("Returned: " + loan);
            System.out.println("Late fee: " + String.format("%,d", fee) + " VND");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    public void handleReports() {
        int choice = -1;
        while (choice != 0) {
            System.out.println();
            System.out.println("--- Reports and statistics ---");
            System.out.println("1. Active loans of a reader");
            System.out.println("2. Available publications");
            System.out.println("3. Overdue loans at a given date");
            System.out.println("4. Total fees (returned loans)");
            System.out.println("5. All loans (including history)");
            System.out.println("0. Back");
            choice = this.readInt("Choose", 0, 5);
            try {
                switch (choice) {
                    case 1:
                        String readerId = this.readId("Reader ID");
                        if (this.service.findReaderById(readerId) == null) {
                            System.out.println("Reader not found.");
                        } else {
                            List<Loan> list = this.service.getActiveLoansOfReader(readerId);
                            if (list.isEmpty()) {
                                System.out.println("This reader has no active loans.");
                            }
                            for (Loan loan : list) {
                                System.out.println(loan);
                            }
                        }
                        break;
                    case 2: {
                        List<Publication> list = this.service.getAvailablePublications();
                        if (list.isEmpty()) {
                            System.out.println("No publications available.");
                        }
                        for (Publication p : list) {
                            System.out.println(p);
                        }
                        break;
                    }
                    case 3: {
                        LocalDate date = this.readDate("Date to check (yyyy-MM-dd)");
                        List<Loan> list = this.service.getOverdueLoans(date);
                        if (list.isEmpty()) {
                            System.out.println("No overdue loans on " + date + ".");
                        }
                        for (Loan loan : list) {
                            System.out.println(loan);
                        }
                        break;
                    }
                    case 4:
                        System.out.println("Total fees: " + String.format("%,d", this.service.getTotalFee()) + " VND");
                        break;
                    case 5:
                        if (this.service.getLoans().isEmpty()) {
                            System.out.println("No loans yet.");
                        }
                        for (Loan loan : this.service.getLoans()) {
                            System.out.println(loan);
                        }
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Rejected: " + e.getMessage());
            }
        }
    }

    public void handleSaveAndExit() {
        String answer = "";
        while (!answer.equals("y") && !answer.equals("n")) {
            answer = this.readString("Save data before exiting? (y = yes, n = no)").toLowerCase();
        }
        boolean done = answer.equals("n");
        while (!done) {
            try {
                this.storage.save(this.service);
                System.out.println("Data saved to " + this.storage.getFilePath());
                done = true;
            } catch (IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
                String retry = "";
                while (!retry.equals("y") && !retry.equals("n")) {
                    retry = this.readString("Not saved. Try again? (y = retry, n = exit without saving)").toLowerCase();
                }
                done = retry.equals("n");
            }
        }
        System.out.println("Goodbye!");
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = this.scanner.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Must not be empty. Please try again.");
        }
    }

    public String readId(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = this.scanner.nextLine().trim();
            if (!s.isEmpty() && s.length() <= 20 && !s.matches(".*\\s.*")) {
                return s;
            }
            System.out.println("ID must be non-empty, at most 20 characters, with no spaces. Try again.");
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = this.scanner.nextLine().trim();
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use the format yyyy-MM-dd (e.g. 2026-10-01).");
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + min + "-" + max + "]: ");
            String s = this.scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(s);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer.");
            }
        }
    }
}
