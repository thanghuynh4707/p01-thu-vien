package library;

import java.time.LocalDate;
import java.util.*;

/**
 * LibraryService quan ly toan bo logic nghiep vu cua thu vien:
 * - Quan ly Danh muc tai lieu (Sach, Tap chi)
 * - Quan ly Doc gia
 * - Quan ly Phieu muon - tra (Loan)
 * - Xu ly cac quy tac nghiep vu khat khe, kiem tra dieu kien truoc khi thay doi du lieu.
 */
public class LibraryService {
    // Luu tru tai lieu: Map<MaTaiLieu, DoiTuongTaiLieu>
    private Map<String, Publication> publications;
    
    // Luu tru doc gia: Map<MaDocGia, DoiTuongDocGia>
    private Map<String, Reader> readers;
    
    // Danh sach phieu muon
    private List<Loan> loans;
    
    // Bo dem ma phieu
    private long nextLoanNumber;

    // Constructor khoi tao
    public LibraryService() {
        this.publications = new HashMap<>();
        this.readers = new HashMap<>();
        this.loans = new ArrayList<>();
        this.nextLoanNumber = 1;
    }

    // ==================== GETTERS & SETTERS ====================
    
    public Collection<Publication> getPublications() {
        return publications.values();
    }

    public Collection<Reader> getReaders() {
        return readers.values();
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public long getNextLoanNumber() {
        return nextLoanNumber;
    }

    public void setNextLoanNumber(long nextLoanNumber) {
        this.nextLoanNumber = nextLoanNumber;
    }

    // ==================== QUAN LY TAI LIEU (PUBLICATION) ====================

    // Them tai lieu (Dung boolean de bao thanh cong/that bai thay vi nem Exception)
    public boolean addPublication(Publication p) {
        if (p == null || p.getId() == null || p.getId().isEmpty()) {
            System.out.println("Thong tin tai lieu khong hop le!");
            return false;
        }
        if (publications.containsKey(p.getId())) {
            System.out.println("Ma tai lieu da ton tai!");
            return false;
        }
        publications.put(p.getId(), p);
        return true;
    }

    // Tim tai lieu theo ID
    public Publication findPublicationById(String id) {
        if (id == null) return null;
        return publications.get(id);
    }

    // Tim kiem gan dung theo ten
    public List<Publication> searchPublicationsByTitle(String keyword) {
        List<Publication> result = new ArrayList<>();
        if (keyword == null || keyword.isEmpty()) {
            return result;
        }
        for (Publication p : publications.values()) {
            if (p.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    // Cap nhat tai lieu (Bo instanceof phuc tap, chi sua tieu de cho don gian)
    public boolean updatePublication(String id, String newTitle) {
        Publication p = findPublicationById(id);
        if (p == null) {
            System.out.println("Khong tim thay tai lieu!");
            return false;
        }
        if (newTitle == null || newTitle.isEmpty()) {
            System.out.println("Tieu de khong duoc de trong!");
            return false;
        }
        p.setTitle(newTitle);
        return true;
    }

    // Xoa tai lieu
    public boolean deletePublication(String id) {
        Publication p = findPublicationById(id);
        if (p == null) {
            System.out.println("Khong tim thay tai lieu!");
            return false;
        }
        // Kiem tra xem co ai tung muon chua
        for (Loan l : loans) {
            if (l.getPublicationId().equals(id)) {
                System.out.println("Tai lieu nay da co lich su muon, khong duoc xoa!");
                return false;
            }
        }
        publications.remove(id);
        return true;
    }

    // ==================== QUAN LY DOC GIA (READER) ====================

    public boolean addReader(Reader r) {
        if (r == null || r.getId() == null || r.getId().isEmpty()) {
            System.out.println("Thong tin doc gia khong hop le!");
            return false;
        }
        if (readers.containsKey(r.getId())) {
            System.out.println("Ma doc gia da ton tai!");
            return false;
        }
        readers.put(r.getId(), r);
        return true;
    }

    public Reader findReaderById(String id) {
        if (id == null) return null;
        return readers.get(id);
    }

    public boolean updateReader(String id, String newName, String newContact) {
        Reader r = findReaderById(id);
        if (r == null) {
            System.out.println("Khong tim thay doc gia!");
            return false;
        }
        if (newName != null && !newName.isEmpty()) {
            r.setFullName(newName);
        }
        if (newContact != null && !newContact.isEmpty()) {
            r.setContact(newContact);
        }
        return true;
    }

    public boolean deleteReader(String id) {
        Reader r = findReaderById(id);
        if (r == null) {
            System.out.println("Khong tim thay doc gia!");
            return false;
        }
        for (Loan l : loans) {
            if (l.getReaderId().equals(id)) {
                System.out.println("Doc gia nay da co lich su muon sach, khong duoc xoa!");
                return false;
            }
        }
        readers.remove(id);
        return true;
    }

    // ==================== NGHIEP VU MUON / TRA ====================

    public Loan borrow(String readerId, String publicationId, LocalDate borrowDate) {
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            System.out.println("Ma doc gia khong ton tai!");
            return null;
        }
        Publication pub = findPublicationById(publicationId);
        if (pub == null) {
            System.out.println("Ma tai lieu khong ton tai!");
            return null;
        }
        if (!isAvailable(publicationId)) {
            System.out.println("Tai lieu dang co nguoi muon!");
            return null;
        }
        if (countActiveLoansOf(readerId) >= 3) {
            System.out.println("Doc gia da dat toi da 3 phieu muon!");
            return null;
        }

        // Tao ma phieu
        String loanId = String.format("L%06d", nextLoanNumber++);
        Loan newLoan = new Loan(loanId, readerId, publicationId, borrowDate);
        loans.add(newLoan);
        return newLoan;
    }

    public long returnLoan(String loanId, LocalDate returnDate) {
        Loan loan = findLoanById(loanId);
        if (loan == null || !loan.isActive()) {
            System.out.println("Phieu muon khong hop le hoac da tra roi!");
            return -1;
        }

        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        if (daysLate < 0) daysLate = 0;

        Publication pub = findPublicationById(loan.getPublicationId());
        long finePerDay = (pub != null) ? pub.getFinePerDay() : 0;
        long finalFee = daysLate * finePerDay;

        loan.markReturned(returnDate, finalFee);
        return finalFee;
    }

    public Loan findLoanById(String loanId) {
        if (loanId == null) return null;
        for (Loan l : loans) {
            if (l.getLoanId().equalsIgnoreCase(loanId)) {
                return l;
            }
        }
        return null;
    }

    // ==================== TRA CUU VA THONG KE ====================

    public boolean isAvailable(String publicationId) {
        for (Loan l : loans) {
            if (l.getPublicationId().equals(publicationId) && l.isActive()) {
                return false;
            }
        }
        return true;
    }

    public int countActiveLoansOf(String readerId) {
        int count = 0;
        for (Loan l : loans) {
            if (l.getReaderId().equals(readerId) && l.isActive()) {
                count++;
            }
        }
        return count;
    }

    public List<Loan> getOverdueLoans(LocalDate date) {
        List<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isOverdueAt(date)) {
                result.add(l);
            }
        }
        return result;
    }

    public long getTotalFee() {
        long total = 0;
        for (Loan l : loans) {
            if (!l.isActive()) {
                total += l.getFinalFee();
            }
        }
        return total;
    }
}
