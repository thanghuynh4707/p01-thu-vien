
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class Menu {

    private final LibraryService service;
    private final FileStorage storage;
    private final InputHelper input;

    public Menu(LibraryService service, FileStorage storage) {
        this.service = service;
        this.storage = storage;
        this.input = new InputHelper();
    }

    // Menu chinh
    public void run() {
        boolean running = true;

        while (running) {
            System.out.println("\n========== LIBRARY MANAGEMENT ==========");
            System.out.println("1. Manage publications");
            System.out.println("2. Manage readers");
            System.out.println("3. Borrow and return");
            System.out.println("4. Reports and statistics");
            System.out.println("5. Save data");
            System.out.println("0. Exit");

            int choice = input.inputChoice("Choose: ", 0, 5);

            try {
                switch (choice) {
                    case 1:
                        publicationMenu();
                        break;
                    case 2:
                        readerMenu();
                        break;
                    case 3:
                        loanMenu();
                        break;
                    case 4:
                        reportMenu();
                        break;
                    case 5:
                        saveData();
                        break;
                    case 0:
                        if (input.confirm("Save before exiting?")) {
                            saveData();
                        }
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    // ================= PUBLICATIONS =================

    private void publicationMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========== PUBLICATIONS ==========");
            System.out.println("1. Add book");
            System.out.println("2. Add journal");
            System.out.println("3. List publications");
            System.out.println("4. Find publication by ID");
            System.out.println("5. Search by title");
            System.out.println("6. Update publication");
            System.out.println("7. Delete publication");
            System.out.println("0. Back");

            int choice = input.inputChoice("Choose: ", 0, 7);

            try {
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        addJournal();
                        break;
                    case 3:
                        listPublications();
                        break;
                    case 4:
                        findPublication();
                        break;
                    case 5:
                        searchPublication();
                        break;
                    case 6:
                        updatePublication();
                        break;
                    case 7:
                        deletePublication();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addBook() {
        String id = input.inputId("Book ID: ");
        String title = input.inputNonEmpty("Title: ");
        String author = input.inputNonEmpty("Author: ");

        service.addPublication(new Book(id, title, author));
        saveData();

        System.out.println("Book added successfully!");
    }

    private void addJournal() {
        String id = input.inputId("Journal ID: ");
        String title = input.inputNonEmpty("Title: ");
        String issue = input.inputNonEmpty("Issue number: ");

        service.addPublication(new Journal(id, title, issue));
        saveData();

        System.out.println("Journal added successfully!");
    }

    private void listPublications() {
        Collection<Publication> publications =
                service.getPublications();

        if (publications.isEmpty()) {
            System.out.println("No publications found.");
            return;
        }

        for (Publication p : publications) {
            System.out.println(p);
            System.out.println("--------------------");
        }
    }

    private void findPublication() {
        String id = input.inputId("Publication ID: ");

        Publication p = service.findPublicationById(id);

        if (p == null) {
            System.out.println("Publication not found.");
        } else {
            System.out.println(p);
        }
    }

    private void searchPublication() {
        String keyword = input.inputNonEmpty("Title keyword: ");

        List<Publication> results =
                service.searchPublicationsByTitle(keyword);

        if (results.isEmpty()) {
            System.out.println("No matching publications.");
            return;
        }

        for (Publication p : results) {
            System.out.println(p);
        }
    }

    private void updatePublication() {
        String id = input.inputId("Publication ID: ");

        Publication p = service.findPublicationById(id);

        if (p == null) {
            System.out.println("Publication not found.");
            return;
        }

        System.out.println("Current information: " + p);

        String title = input.inputNonEmpty("New title: ");
        String detail;

        if (p instanceof Book) {
            detail = input.inputNonEmpty("New author: ");
        } else {
            detail = input.inputNonEmpty("New issue number: ");
        }

        service.updatePublication(id, title, detail);
        saveData();

        System.out.println("Publication updated successfully!");
    }

    private void deletePublication() {
        String id = input.inputId("Publication ID: ");

        service.deletePublication(id);
        saveData();

        System.out.println("Publication deleted successfully!");
    }

    // ================= READERS =================

    private void readerMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========== READERS ==========");
            System.out.println("1. Add reader");
            System.out.println("2. List readers");
            System.out.println("3. Find reader");
            System.out.println("4. Update reader");
            System.out.println("5. Delete reader");
            System.out.println("0. Back");

            int choice = input.inputChoice("Choose: ", 0, 5);

            try {
                switch (choice) {
                    case 1:
                        addReader();
                        break;
                    case 2:
                        listReaders();
                        break;
                    case 3:
                        findReader();
                        break;
                    case 4:
                        updateReader();
                        break;
                    case 5:
                        deleteReader();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addReader() {
        String id = input.inputId("Reader ID: ");
        String name = input.inputNonEmpty("Full name: ");
        String contact = input.inputNonEmpty("Contact: ");

        service.addReader(new Reader(id, name, contact));
        saveData();

        System.out.println("Reader added successfully!");
    }

    private void listReaders() {
        Collection<Reader> readers = service.getReaders();

        if (readers.isEmpty()) {
            System.out.println("No readers found.");
            return;
        }

        for (Reader r : readers) {
            System.out.println(r);
        }
    }

    private void findReader() {
        String id = input.inputId("Reader ID: ");

        Reader r = service.findReaderById(id);

        if (r == null) {
            System.out.println("Reader not found.");
        } else {
            System.out.println(r);
        }
    }

    private void updateReader() {
        String id = input.inputId("Reader ID: ");

        Reader r = service.findReaderById(id);

        if (r == null) {
            System.out.println("Reader not found.");
            return;
        }

        String name = input.inputNonEmpty("New full name: ");
        String contact = input.inputNonEmpty("New contact: ");

        service.updateReader(id, name, contact);
        saveData();

        System.out.println("Reader updated successfully!");
    }

    private void deleteReader() {
        String id = input.inputId("Reader ID: ");

        service.deleteReader(id);
        saveData();

        System.out.println("Reader deleted successfully!");
    }

    // ================= BORROW AND RETURN =================

    private void loanMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========== BORROW AND RETURN ==========");
            System.out.println("1. Borrow publication");
            System.out.println("2. Return publication");
            System.out.println("3. List all loans");
            System.out.println("4. Find loan by ID");
            System.out.println("0. Back");

            int choice = input.inputChoice("Choose: ", 0, 4);

            try {
                switch (choice) {
                    case 1:
                        borrowPublication();
                        break;
                    case 2:
                        returnPublication();
                        break;
                    case 3:
                        listLoans();
                        break;
                    case 4:
                        findLoan();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void borrowPublication() {
        String readerId = input.inputId("Reader ID: ");
        String publicationId = input.inputId("Publication ID: ");
        LocalDate borrowDate = input.inputDate(
                "Borrow date (yyyy-MM-dd): ");

        Loan loan = service.borrow(
                readerId, publicationId, borrowDate);

        saveData();

        System.out.println("Borrow successful!");
        System.out.println(loan);
    }

    private void returnPublication() {
        String loanId = input.inputId("Loan ID: ");
        LocalDate returnDate = input.inputDate(
                "Return date (yyyy-MM-dd): ");

        long fee = service.returnLoan(loanId, returnDate);

        saveData();

        System.out.println("Return successful!");
        System.out.println("Late fee: " + fee + " VND");
    }

    private void listLoans() {
        List<Loan> loans = service.getLoans();

        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private void findLoan() {
        String id = input.inputId("Loan ID: ");

        Loan loan = service.findLoanById(id);

        if (loan == null) {
            System.out.println("Loan not found.");
        } else {
            System.out.println(loan);
        }
    }

    // ================= REPORTS =================

    private void reportMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========== REPORTS ==========");
            System.out.println("1. Active loans of a reader");
            System.out.println("2. Available publications");
            System.out.println("3. Overdue loans at a given date");
            System.out.println("4. Total fees collected");
            System.out.println("0. Back");

            int choice = input.inputChoice("Choose: ", 0, 4);

            try {
                switch (choice) {
                    case 1:
                        activeLoansOfReader();
                        break;
                    case 2:
                        availablePublications();
                        break;
                    case 3:
                        overdueLoans();
                        break;
                    case 4:
                        totalFees();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void activeLoansOfReader() {
        String readerId = input.inputId("Reader ID: ");

        List<Loan> loans =
                service.getActiveLoansOfReader(readerId);

        if (loans.isEmpty()) {
            System.out.println("This reader has no active loans.");
            return;
        }

        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private void availablePublications() {
        List<Publication> publications =
                service.getAvailablePublications();

        if (publications.isEmpty()) {
            System.out.println("No publications available.");
            return;
        }

        for (Publication p : publications) {
            System.out.println(p);
        }
    }

    private void overdueLoans() {
        LocalDate date = input.inputDate(
                "Check overdue loans at (yyyy-MM-dd): ");

        List<Loan> loans = service.getOverdueLoans(date);

        if (loans.isEmpty()) {
            System.out.println("No overdue loans.");
            return;
        }

        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private void totalFees() {
        long total = service.getTotalFee();

        System.out.println("Total collected fees: " + total + " VND");
    }

    // ================= FILE STORAGE =================

    private void saveData() {
        try {
            storage.save(service);
        } catch (IOException e) {
            System.out.println("Cannot save data: " + e.getMessage());
        }
    }
}
