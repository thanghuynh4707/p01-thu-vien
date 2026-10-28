package library;

import library.Publication;
import library.Book;
import library.Journal;
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
    // Luu tru tai lieu theo dang Map<id, Publication> de tim kiem nhanh O(1)
    private Map<String, Publication> publications;
    
    // Luu tru doc gia theo dang Map<id, Reader>
    private Map<String, Reader> readers;
    
    // Danh sach luu toan bo cac phieu muon (ca dang muon va da tra)
    private List<Loan> loans;
    
    // Bo dem tao ma phieu duy nhat, bat buoc phai luu file de khong bi trung sau khi khoi dong lai
    private long nextLoanNumber;

    /**
     * Khoi tao dich vu thu vien voi cac cau truc du lieu rong.
     */
    public LibraryService() {
        this.publications = new HashMap<>();
        this.readers = new HashMap<>();
        this.loans = new ArrayList<>();
        this.nextLoanNumber = 1; // Bat dau dem phieu tu 1
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

    /**
     * Them tai lieu moi vao thu vien.
     * Quy tac: Ma khong duoc trung.
     */
    public void addPublication(Publication p) {
        if (p == null || p.getId() == null || p.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Thong tin tai lieu khong hop le!");
        }
        if (publications.containsKey(p.getId())) {
            throw new IllegalArgumentException("Ma tai lieu '" + p.getId() + "' da ton tai!");
        }
        publications.put(p.getId(), p);
    }

    /**
     * Tim tai lieu theo Ma (ID).
     */
    public Publication findPublicationById(String id) {
        if (id == null) return null;
        return publications.get(id.trim());
    }

    /**
     * Tim kiem tai lieu theo mot phan ten (Khong phan biet hoa thuong).
     */
    public List<Publication> searchPublicationsByTitle(String keyword) {
        List<Publication> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        for (Publication p : publications.values()) {
            if (p.getTitle().toLowerCase().contains(lowerKeyword)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Cap nhat thong tin tai lieu (Tieu de va Tac gia/So phat hanh).
     * Quy tac: Khong duoc doi Ma tai lieu va Loai tai lieu.
     */
    public void updatePublication(String id, String newTitle, String newAuthorOrIssue) {
        Publication p = findPublicationById(id);
        if (p == null) {
            throw new IllegalArgumentException("Khong tim thay tai lieu co ma: " + id);
        }
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten tieu de khong duoc de trong!");
        }
        
        // Cap nhat tieu de chung
        p.setTitle(newTitle.trim());

        // Phan ra da hinh de cap nhat thuoc tinh rieng tuy loai tai lieu
        if (p instanceof Book) {
            if (newAuthorOrIssue == null || newAuthorOrIssue.trim().isEmpty()) {
                throw new IllegalArgumentException("Tac gia sach khong duoc de trong!");
            }
            ((Book) p).setAuthor(newAuthorOrIssue.trim());
        } else if (p instanceof Journal) {
            if (newAuthorOrIssue == null || newAuthorOrIssue.trim().isEmpty()) {
                throw new IllegalArgumentException("So phat hanh tap chi khong duoc de trong!");
            }
            ((Journal) p).setIssue(newAuthorOrIssue.trim());
        }
    }

    /**
     * Xoa tai lieu.
     * Quy tac: Chi duoc xoa tai lieu CHUA TUNG co phieu muon nao trong he thong.
     */
    public void deletePublication(String id) {
        Publication p = findPublicationById(id);
        if (p == null) {
            throw new IllegalArgumentException("Khong tim thay tai lieu can xoa!");
        }
        
        // Kiem tra xem tai lieu nay da tung co phieu muon nao chua
        for (Loan l : loans) {
            if (l.getPublicationId().equals(id)) {
                throw new IllegalArgumentException("Tu choi xoa: Tai lieu nay da tung co lich su muon tra!");
            }
        }
        publications.remove(id);
    }

    // ==================== QUAN LY DOC GIA (READER) ====================

    /**
     * Them doc gia moi.
     */
    public void addReader(Reader r) {
        if (r == null || r.getId() == null || r.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Thong tin doc gia khong hop le!");
        }
        if (readers.containsKey(r.getId())) {
            throw new IllegalArgumentException("Ma doc gia '" + r.getId() + "' da ton tai!");
        }
        readers.put(r.getId(), r);
    }

    /**
     * Tim doc gia theo Ma (ID).
     */
    public Reader findReaderById(String id) {
        if (id == null) return null;
        return readers.get(id.trim());
    }

    /**
     * Cap nhat thong tin doc gia (Ho ten va lien he).
     */
    public void updateReader(String id, String newName, String newContact) {
        Reader r = findReaderById(id);
        if (r == null) {
            throw new IllegalArgumentException("Khong tim thay doc gia co ma: " + id);
        }
        if (newName != null && !newName.trim().isEmpty()) {
            r.setFullName(newName.trim());
        }
        if (newContact != null && !newContact.trim().isEmpty()) {
            r.setContact(newContact.trim());
        }
    }

    /**
     * Xoa doc gia.
     * Quy tac: Chi duoc xoa doc gia CHUA TUNG co phieu muon nao.
     */
    public void deleteReader(String id) {
        Reader r = findReaderById(id);
        if (r == null) {
            throw new IllegalArgumentException("Khong tim thay doc gia can xoa!");
        }
        for (Loan l : loans) {
            if (l.getReaderId().equals(id)) {
                throw new IllegalArgumentException("Tu choi xoa: Doc gia nay da tung co lich su muon sach!");
            }
        }
        readers.remove(id);
    }

    // ==================== NGHIEP VU MUON / TRA (BORROW / RETURN) ====================

    /**
     * Thuc hien cho muon tai lieu.
     * Quy tac kiem tra toan dien truoc khi cap nhat du lieu:
     * 1. Doc gia va tai lieu phai ton tai.
     * 2. Tai lieu phai dang san co (chua co ai muon - active).
     * 3. Mot tai lieu chi co toi da 1 phieu dang muon.
     * 4. Mot doc gia co toi da 3 phieu dang muon.
     */
    public Loan borrow(String readerId, String publicationId, LocalDate borrowDate) {
        // Kiem tra ton tai
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            throw new IllegalArgumentException("Ma doc gia khong ton tai trong he thong!");
        }
        Publication pub = findPublicationById(publicationId);
        if (pub == null) {
            throw new IllegalArgumentException("Ma tai lieu khong ton tai trong he thong!");
        }
        if (borrowDate == null) {
            throw new IllegalArgumentException("Ngay muon khong hop le!");
        }

        // Kiem tra tai lieu co san sang khong
        if (!isAvailable(publicationId)) {
            throw new IllegalArgumentException("Tu choi: Tai lieu nay dang co nguoi muon!");
        }

        // Kiem tra gioi han so luong phieu dang muon cua doc gia (Toi da 3 phieu)
        int activeLoansCount = countActiveLoansOf(readerId);
        if (activeLoansCount >= 3) {
            throw new IllegalArgumentException("Tu choi: Doc gia da dat gioi han muon toi da (3 tai lieu dang muon)!");
        }

        // Tao ma phieu duy nhat (Vi du: L000001, L000002...)
        String loanId = String.format("L%06d", nextLoanNumber++);

        // Tao doi tuong Loan moi (Han muon tu dong +7 ngay duoc tinh ben trong Constructor cua Loan)
        Loan newLoan = new Loan(loanId, readerId.trim(), publicationId.trim(), borrowDate);
        
        loans.add(newLoan);
        return newLoan;
    }

    /**
     * Nhan tra tai lieu.
     * Quy tac:
     * 1. Phieu muon phai ton tai va dang trong trang thai BORROWING.
     * 2. Ngay tra phai tu ngay muon tro di.
     * 3. Tinh phi tre han qua tinh nang da hinh `getFinePerDay()` cua Publication:
     *    - Sach: 3.000d/ngay
     *    - Tap chi: 1.000d/ngay
     *    - Cong thuc: max(0, so ngay tre) x don gia
     */
    public long returnLoan(String loanId, LocalDate returnDate) {
        Loan loan = findLoanById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Khong tim thay ma phieu muon: " + loanId);
        }
        if (!loan.isActive()) {
            throw new IllegalArgumentException("Tu choi: Phieu muon nay da duoc tra truoc do roi (khong duoc tra lan hai)!");
        }
        if (returnDate == null || returnDate.isBefore(loan.getBorrowDate())) {
            throw new IllegalArgumentException("Tu choi: Ngay tra khong hop le (phai tu ngay muon tro di)!");
        }

        // Tinh so ngay tre (neu ngay tra sau ngay hen tra - dueDate)
        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        if (daysLate < 0) {
            daysLate = 0; // Tra dung han hoac truoc han thi khong tinh tre
        }

        // Lay thong tin tai lieu de goi da hinh phuong thuc tinh phi tre han (`getFinePerDay()`)
        Publication pub = findPublicationById(loan.getPublicationId());
        long finePerDay = (pub != null) ? pub.getFinePerDay() : 0;
        long finalFee = daysLate * finePerDay;

        // Cap nhat trang thai phieu muon (danh dau da tra, gan ngay tra va tien phat)
        loan.markReturned(returnDate, finalFee);

        return finalFee;
    }

    /**
     * Tim phieu muon theo Ma phieu.
     */
    public Loan findLoanById(String loanId) {
        if (loanId == null) return null;
        for (Loan l : loans) {
            if (l.getLoanId().equalsIgnoreCase(loanId.trim())) {
                return l;
            }
        }
        return null;
    }

    // ==================== TRA CUU VA THONG KE ====================

    /**
     * Kiem tra tai lieu co dang ranh (san co de cho muon) khong.
     * Quy tac: Tai lieu san co khi KHONG nam trong bat ky phieu muon nao co trang thai BORROWING.
     */
    public boolean isAvailable(String publicationId) {
        for (Loan l : loans) {
            if (l.getPublicationId().equals(publicationId) && l.isActive()) {
                return false; // Dang co nguoi giu
            }
        }
        return true;
    }

    /**
     * Dem so luong phieu dang muon cua mot doc gia.
     */
    public int countActiveLoansOf(String readerId) {
        int count = 0;
        for (Loan l : loans) {
            if (l.getReaderId().equals(readerId) && l.isActive()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Lay danh sach cac phieu dang muon cua mot doc gia cu the.
     */
    public List<Loan> getActiveLoansOfReader(String readerId) {
        List<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getReaderId().equals(readerId) && l.isActive()) {
                result.add(l);
            }
        }
        return result;
    }

    /**
     * Lay danh sach toan bo cac tai lieu hien dang san co trong thu vien.
     */
    public List<Publication> getAvailablePublications() {
        List<Publication> result = new ArrayList<>();
        for (Publication p : publications.values()) {
            if (isAvailable(p.getId())) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Lay danh sach phieu qua han tai mot moc ngay do nguoi dung nhap.
     * (Chi loc du lieu, tuyet doi khong lam thay doi trang thai phieu).
     */
    public List<Loan> getOverdueLoans(LocalDate date) {
        List<Loan> result = new ArrayList<>();
        if (date == null) return result;
        for (Loan l : loans) {
            if (l.isOverdueAt(date)) {
                result.add(l);
            }
        }
        return result;
    }

    /**
     * Tinh tong so tien phi phat da thu tu tat ca cac phieu da tra (RETURNED).
     */
    public long getTotalFee() {
        long total = 0;
        for (Loan l : loans) {
            if (!l.isActive()) { // Da tra
                total += l.getFinalFee();
            }
        }
        return total;
    }
}