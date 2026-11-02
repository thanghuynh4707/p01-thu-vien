/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable {

    private String loanId;
    private String readerId;
    private String publicationId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    private LocalDate returnDate;
    private long finalFee;
    private LoanStatus status;

    public Loan(String loanId, String readerId, String publicationId, LocalDate borrowDate) {
        this(loanId, readerId, publicationId,
                borrowDate,
                borrowDate == null ? null : borrowDate.plusDays(7),
                null, 0, LoanStatus.BORROWING);
    }

    public Loan(String loanId, String readerId, String publicationId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, long finalFee, LoanStatus status) {
        checkId(loanId, "Loan ID");
        checkId(readerId, "Reader ID");
        checkId(publicationId, "Publication ID");

        if (borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Borrow date and due date must not be null!");
        }
        if (!dueDate.equals(borrowDate.plusDays(7))) {
            throw new IllegalArgumentException("Due date must be exactly 7 days after borrow date!");
        }
        if (returnDate != null && returnDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Return date must not be before borrow date!");
        }
        if (finalFee < 0) {
            throw new IllegalArgumentException("Late fee must not be negative!");
        }
        if (status == null) {
            throw new IllegalArgumentException("Invalid status!");
        }
        if (status == LoanStatus.BORROWING) {
            if (returnDate != null || finalFee != 0) {
                throw new IllegalArgumentException("Active loan must not have a return date or late fee!");
            }
        } else if (status == LoanStatus.RETURNED) {
            if (returnDate == null) {
                throw new IllegalArgumentException("Returned loan must have a return date!");
            }
        }

        this.loanId = loanId;
        this.readerId = readerId;
        this.publicationId = publicationId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.finalFee = finalFee;
        this.status = status;
    }

    private void checkId(String loanId, String name) {
        if (loanId == null || loanId.trim().isEmpty()
                || loanId.trim().length() > 20
                || loanId.trim().matches(".*\\s+.*")) {
            throw new IllegalArgumentException(name + " is invalid!");
        }
    }

    public String getLoanId() {
        return this.loanId;
    }

    public String getReaderId() {
        return this.readerId;
    }

    public String getPublicationId() {
        return this.publicationId;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public long getFinalFee() {
        return this.finalFee;
    }

    public LoanStatus getStatus() {
        return this.status;
    }

    public boolean isActive() {
        return status == LoanStatus.BORROWING;
    }

    public boolean isOverdueAt(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Lookup date must not be empty!");
        }

        return isActive() && dueDate.isBefore(date);

    }

    public void markReturned(LocalDate returnDate, long fee) {
        if (!isActive()) {
            throw new IllegalArgumentException("This loan has already been returned!");
        }
        if (returnDate == null || returnDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Invalid return date!");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Late fee must not be negative!");
        }
        this.returnDate = returnDate;
        this.finalFee = fee;
        this.status = LoanStatus.RETURNED;
    }

    @Override
    public String toString() {

        return "Loan{"
                + "loanId='" + this.loanId + '\''
                + ", readerId='" + this.readerId + '\''
                + ", publicationId='" + this.publicationId + '\''
                + ", borrowDate=" + this.borrowDate
                + ", dueDate=" + this.dueDate
                + ", returnDate=" + this.returnDate
                + ", finalFee=" + this.finalFee
                + ", status=" + this.status
                + '}';

    }

}
