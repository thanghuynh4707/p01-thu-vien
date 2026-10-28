package library;

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
        checkId(loanId, "Ma phieu");
        checkId(readerId, "Ma doc gia");
        checkId(publicationId, "Ma tai lieu");
        
        if(borrowDate == null || dueDate == null){
            throw new IllegalArgumentException("Ngay muon va han tra khong duoc null!");
        }
        if(dueDate.isBefore(borrowDate.plusDays(7))){
            throw new IllegalArgumentException("Han tra khong dung quy dinh 7 ngay!");
        }
        if(returnDate != null && returnDate.isBefore(borrowDate)){
            throw new IllegalArgumentException("Ngay tra khong duoc truoc ngay muon!");
        }
        if(finalFee < 0){
            throw new IllegalArgumentException("Phi phat khong duoc am!");
        }
        if(status == null){
            throw new IllegalArgumentException("Trang thai khong hop le!");
        }
        if(status == LoanStatus.BORROWING){
            if(returnDate != null || finalFee != 0){
                throw new IllegalArgumentException("Phieu dang muon khong duoc co ngay tra hoac phi phat!");
            }
        } else if (status == LoanStatus.RETURNED){
            if(returnDate == null){
                throw new IllegalArgumentException("Phieu da tra phai co ngay tra!");
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
       if(loanId == null || loanId.trim().isEmpty()
               || loanId.trim().length() > 20
               || loanId.trim().matches(".*\\s+.*")){
           throw new IllegalArgumentException(name + "khong hop le!");
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
    public boolean isActive(){
        return status == LoanStatus.BORROWING;
    }
    public boolean isOverdueAt(LocalDate date){
        if(date == null){
            throw new IllegalArgumentException("Ngay kiem tra khong duoc de trong!");
        }
        
        return isActive() && dueDate.isBefore(date);
        
    }
    public void markReturned(LocalDate returnDate, long fee){
        if(!isActive()){
            throw new IllegalArgumentException("Phieu nay da duoc tra!");
        }
        if(returnDate == null || returnDate.isBefore(borrowDate)){
            throw new IllegalArgumentException("Ngay tra khong hop le!");
        }
        if(fee < 0){
            throw new IllegalArgumentException("Phi phat khong duoc am!");
        }
        this.returnDate = returnDate;
        this.finalFee = fee;
        this.status = LoanStatus.RETURNED;
    }
    @Override
    public String toString(){
        
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
