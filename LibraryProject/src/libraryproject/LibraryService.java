/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LibraryService implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_ACTIVE_LOANS = 3;

    private Map<String, Publication> publications;
    private Map<String, Reader> readers;
    private List<Loan> loans;
    private long nextLoanNumber;

    public LibraryService() {
        this.publications = new LinkedHashMap<String, Publication>();
        this.readers = new LinkedHashMap<String, Reader>();
        this.loans = new ArrayList<Loan>();
        this.nextLoanNumber = 1;
    }

    public Collection<Publication> getPublications() {
        return Collections.unmodifiableCollection(this.publications.values());
    }

    public Collection<Reader> getReaders() {
        return Collections.unmodifiableCollection(this.readers.values());
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(this.loans);
    }

    public long getNextLoanNumber() {
        return this.nextLoanNumber;
    }

    public void setNextLoanNumber(long n) {
        if (n < 1) {
            throw new IllegalArgumentException("Next loan number must be at least 1.");
        }
        this.nextLoanNumber = n;
    }

    public void addPublication(Publication p) {
        if (p == null) {
            throw new IllegalArgumentException("Publication must not be null.");
        }
        if (this.publications.containsKey(p.getId())) {
            throw new IllegalArgumentException("Publication ID already exists: " + p.getId());
        }
        this.publications.put(p.getId(), p);
    }

    public Publication findPublicationById(String id) {
        return this.publications.get(id);
    }

    public List<Publication> searchPublicationsByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword must not be empty.");
        }
        String key = keyword.trim().toLowerCase();
        List<Publication> result = new ArrayList<>();
        for (Publication p : this.publications.values()) {
            if (p.getTitle().toLowerCase().contains(key)) {
                result.add(p);
            }
        }
        return result;
    }

    public void updatePublication(String id, String newTitle, String newAuthorOrIssue) {
        Publication p = this.findPublicationById(id);
        if (p == null) {
            throw new IllegalArgumentException("Publication not found: " + id);
        }

        if (newTitle == null || newTitle.trim().isEmpty()
                || newAuthorOrIssue == null || newAuthorOrIssue.trim().isEmpty()) {
            throw new IllegalArgumentException("Title and author/issue must not be empty.");
        }
        p.setTitle(newTitle);
        if (p instanceof Book) {
            ((Book) p).setAuthor(newAuthorOrIssue);
        } else if (p instanceof Journal) {
            ((Journal) p).setIssue(newAuthorOrIssue);
        }
    }

    public void deletePublication(String id) {
        if (this.findPublicationById(id) == null) {
            throw new IllegalArgumentException("Publication not found: " + id);
        }
        for (Loan loan : this.loans) {
            if (loan.getPublicationId().equals(id)) {
                throw new IllegalStateException(
                        "Cannot delete: publication " + id + " already has loans (including returned loans).");
            }
        }
        this.publications.remove(id);
    }

    public void addReader(Reader r) {
        if (r == null) {
            throw new IllegalArgumentException("Reader must not be null.");
        }
        if (this.readers.containsKey(r.getId())) {
            throw new IllegalArgumentException("Reader ID already exists: " + r.getId());
        }
        this.readers.put(r.getId(), r);
    }

    public Reader findReaderById(String id) {
        return this.readers.get(id);
    }

    public void updateReader(String id, String newName, String newContact) {
        Reader r = this.findReaderById(id);
        if (r == null) {
            throw new IllegalArgumentException("Reader not found: " + id);
        }
        if (newName == null || newName.trim().isEmpty()
                || newContact == null || newContact.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name and contact must not be empty.");
        }
        r.setFullName(newName);
        r.setContact(newContact);
    }

    public void deleteReader(String id) {
        if (this.findReaderById(id) == null) {
            throw new IllegalArgumentException("Reader not found: " + id);
        }
        for (Loan loan : this.loans) {
            if (loan.getReaderId().equals(id)) {
                throw new IllegalStateException(
                        "Cannot delete: reader " + id + " already has loans (including returned loans).");
            }
        }
        this.readers.remove(id);
    }

    public Loan borrow(String readerId, String publicationId, LocalDate borrowDate) {
        if (borrowDate == null) {
            throw new IllegalArgumentException("Invalid borrow date.");
        }
        if (this.findReaderById(readerId) == null) {
            throw new IllegalArgumentException("Reader not found: " + readerId);
        }
        if (this.findPublicationById(publicationId) == null) {
            throw new IllegalArgumentException("Publication not found: " + publicationId);
        }
        if (!this.isAvailable(publicationId)) {
            throw new IllegalStateException("Publication " + publicationId + " is currently on loan.");
        }
        if (this.countActiveLoansOf(readerId) >= MAX_ACTIVE_LOANS) {
            throw new IllegalStateException("Reader " + readerId + " has already borrowed "
                    + MAX_ACTIVE_LOANS + " items.");
        }

        String loanId = String.format("L%03d", this.nextLoanNumber);
        this.nextLoanNumber++;
        while (this.findLoanById(loanId) != null) {
            loanId = String.format("L%03d", this.nextLoanNumber);
            this.nextLoanNumber++;
        }
        Loan loan = new Loan(loanId, readerId, publicationId, borrowDate);
        this.loans.add(loan);
        return loan;
    }

    public long returnLoan(String loanId, LocalDate returnDate) {
        if (returnDate == null) {
            throw new IllegalArgumentException("Invalid return date.");
        }
        Loan loan = this.findLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found: " + loanId);
        }
        if (!loan.isActive()) {
            throw new IllegalStateException("Loan " + loanId + " has already been returned.");
        }
        if (returnDate.isBefore(loan.getBorrowDate())) {
            throw new IllegalArgumentException("Return date must not be before the borrow date.");
        }
        Publication p = this.findPublicationById(loan.getPublicationId());
        if (p == null) {
            throw new IllegalStateException("Publication of loan " + loanId + " not found.");
        }

        long daysLate = Math.max(0, ChronoUnit.DAYS.between(loan.getDueDate(), returnDate));
        long fee = daysLate * p.getFinePerDay();

        loan.markReturned(returnDate, fee);
        return fee;
    }

    public Loan findLoanById(String loanId) {
        for (Loan loan : this.loans) {
            if (loan.getLoanId().equals(loanId)) {
                return loan;
            }
        }
        return null;
    }

    public boolean isAvailable(String publicationId) {
        for (Loan loan : this.loans) {
            if (loan.isActive() && loan.getPublicationId().equals(publicationId)) {
                return false;
            }
        }
        return true;
    }

    public int countActiveLoansOf(String readerId) {
        int count = 0;
        for (Loan loan : this.loans) {
            if (loan.isActive() && loan.getReaderId().equals(readerId)) {
                count++;
            }
        }
        return count;
    }

    public List<Loan> getActiveLoansOfReader(String readerId) {
        List<Loan> result = new ArrayList<Loan>();
        for (Loan loan : this.loans) {
            if (loan.isActive() && loan.getReaderId().equals(readerId)) {
                result.add(loan);
            }
        }
        return result;
    }

    public List<Publication> getAvailablePublications() {
        List<Publication> result = new ArrayList<Publication>();
        for (Publication p : this.publications.values()) {
            if (this.isAvailable(p.getId())) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Loan> getOverdueLoans(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid lookup date.");
        }
        List<Loan> result = new ArrayList<Loan>();
        for (Loan loan : this.loans) {
            if (loan.isOverdueAt(date)) {
                result.add(loan);
            }
        }
        return result;
    }

    public long getTotalFee() {
        long total = 0;
        for (Loan loan : this.loans) {
            if (!loan.isActive()) {
                total += loan.getFinalFee();
            }
        }
        return total;
    }
}
