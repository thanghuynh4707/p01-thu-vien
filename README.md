# p01-thu-vien
Phân tích đề tài P01: Quản lý thư viện (PRO192)
1. Đề yêu cầu gì

Đây là ứng dụng Java console (JDK 8, NetBeans 17) cho thư viện nhỏ. Thư viện quản lý sách, tạp chí, độc giả và phiếu mượn–trả. Điểm chấm chính là OOP có ý nghĩa: sách và tạp chí tính phí trễ hạn khác nhau nhưng đi qua cùng một luồng xử lý (đa hình). Đề nói rõ không chấm theo số lớp hay số dòng code.

2. Nhóm cần làm gì

Chức năng bắt buộc

Tài liệu: thêm, liệt kê, tìm theo mã hoặc một phần tên, sửa, xóa có điều kiện. Có hai loại: Sách (có tác giả) và Tạp chí (có số phát hành).
Độc giả: thêm, liệt kê, tìm theo mã, sửa, xóa có điều kiện.
Cho mượn: nhập mã độc giả, mã tài liệu, ngày mượn. Kiểm tra điều kiện rồi tạo phiếu có mã duy nhất, ngày hẹn trả và trạng thái đang mượn.
Nhận trả: nhập ngày trả, tính số ngày trễ và phí, cập nhật phiếu, giải phóng tài liệu.
Tra cứu và thống kê:
danh sách đang mượn của một độc giả;
tài liệu sẵn có;
phiếu quá hạn tại một ngày do người dùng nhập;
tổng phí trên các phiếu đã trả.
Lưu và đọc file: khôi phục đúng quan hệ và trạng thái sau khi khởi động lại.

Quy tắc nghiệp vụ cần nhớ

Mã là chuỗi không rỗng, tối đa 20 ký tự, không có khoảng trắng, không trùng và không đổi sau khi tạo.
Tên, tác giả và liên hệ không được rỗng sau khi trim().
Ngày nhập theo dạng yyyy-MM-dd (dùng LocalDate).
Một tài liệu chỉ có tối đa 1 phiếu đang mượn. Một độc giả có tối đa 3 phiếu đang mượn. Hạn mượn là 7 ngày.
Phí trễ: max(0, số ngày trễ) × đơn giá. Sách 3.000đ/ngày, tạp chí 1.000đ/ngày. Tiền dùng long.
Ngày trả phải từ ngày mượn trở đi. Chỉ phiếu đang mượn mới được trả, và trả lần hai bị từ chối.
Báo cáo quá hạn chỉ lọc dữ liệu, không sửa phiếu.
Chỉ xóa tài liệu hoặc độc giả chưa từng có phiếu. Phiếu đã trả vẫn giữ.
Mã phiếu không được trùng, kể cả sau khi khởi động lại, và không dùng lại mã cũ.
Thao tác bị từ chối không được làm đổi dữ liệu: kiểm tra hết điều kiện trước, rồi mới cập nhật.

Sản phẩm phải nộp

Repo GitHub tên FPTU-FA26-PRO192-SE21B-GroupXX, mời khoand96. Mỗi người commit bằng tài khoản cá nhân.
Project NetBeans build được bằng JDK 8, kèm file JAR và hướng dẫn chạy.
README.
Báo cáo PDF ngắn, gồm sơ đồ lớp khớp code, chỗ gọi đa hình, bảng phân công theo MSSV và bảng kiểm thử.
Ít nhất 12 ca kiểm thử có kết quả thực tế. Mười ca TV01–TV10 trong đề chỉ là kết quả mong đợi, nhóm phải tự chạy và ghi lại.
File /docs/ai_logs/AI_INTERACTION_LOG.md ghi lại các lần dùng AI.
Dữ liệu mẫu: ít nhất 4 tài liệu (2 sách, 2 tạp chí), 3 độc giả, phiếu đang mượn và đã trả. Chương trình cũng phải có cách khởi động với danh sách rỗng.

Bảo vệ: demo một luồng thành công, một trường hợp bị từ chối, một tình huống đa hình và việc mở lại dữ liệu. Mỗi người giải thích phần mình và sửa nhanh một thay đổi nhỏ theo yêu cầu của giảng viên.

3. Cần bao nhiêu lớp và gồm những gì
Bạn cần vẽ 7 lớp trong sơ đồ lớp (class diagram). Nếu nhóm làm cả hai lớp tùy chọn thì thêm 2 lớp nữa, thành 9.

- 7 lớp cần vẽ:

Publication (abstract)
Book
Journal
Reader
Loan
LibraryService
FileStorage

3 lớp có thể bỏ khỏi sơ đồ chính

Menu, InputHelper và Main chỉ là lớp giao diện và tiện ích. Chúng không chứa quy tắc nghiệp vụ, nên thường chỉ ghi chú hoặc bỏ qua

- Các quan hệ cần thể hiện

Book và Journal kế thừa Publication.
Loan liên kết với đúng 1 Reader và đúng 1 Publication. Một Reader có nhiều Loan.
LibraryService chứa danh sách Publication, Reader và Loan.
FileStorage đọc và ghi dữ liệu của LibraryService.

4. Thành phần các lớp:
Lưu ý: đề cấm đổi mã, ngày mượn, ngày hẹn trả và loại tài liệu sau khi tạo. Vì vậy các thuộc tính đó chỉ có getter, không có setter. Đây là yêu cầu của đề, không phải thiếu sót.

1) Publication (abstract)
- id : String
- title : String

+ Publication(id : String, title : String)       // kiểm tra id, title hợp lệ
+ getId() : String
+ getTitle() : String
+ setTitle(title : String) : void                // kiểm tra không rỗng
+ abstract getFinePerDay() : long                // đơn giá phạt, lớp con override
+ abstract getType() : String                    // "BOOK" / "JOURNAL"
+ toString() : String

Không có setId, vì mã không đổi.

2) Book extends Publication
- author : String

+ Book(id : String, title : String, author : String)   // gọi super(id, title)
+ getAuthor() : String
+ setAuthor(author : String) : void                    // kiểm tra không rỗng
+ getFinePerDay() : long       // return 3000
+ getType() : String           // return "BOOK"
+ toString() : String
3) Journal extends Publication
- issue : String       // số phát hành (có thể dùng int nếu nhóm thống nhất)

+ Journal(id : String, title : String, issue : String) // gọi super(id, title)
+ getIssue() : String
+ setIssue(issue : String) : void
+ getFinePerDay() : long       // return 1000
+ getType() : String           // return "JOURNAL"
+ toString() : String
4) Reader
- id : String
- fullName : String
- contact : String

+ Reader(id : String, fullName : String, contact : String)
+ getId() : String
+ getFullName() : String
+ setFullName(fullName : String) : void     // kiểm tra không rỗng
+ getContact() : String
+ setContact(contact : String) : void       // kiểm tra không rỗng
+ toString() : String
5) Loan
- loanId : String
- readerId : String
- publicationId : String
- borrowDate : LocalDate
- dueDate : LocalDate               // = borrowDate + 7 ngày, tính trong constructor
- returnDate : LocalDate            // null khi chưa trả
- finalFee : long                   // 0 khi chưa trả
- status : LoanStatus               // BORROWING / RETURNED

+ Loan(loanId, readerId, publicationId, borrowDate : LocalDate)
+ Loan(loanId, readerId, publicationId, borrowDate, dueDate, returnDate, finalFee, status)
                                    // constructor đầy đủ, dùng khi đọc từ file
+ getLoanId() : String
+ getReaderId() : String
+ getPublicationId() : String
+ getBorrowDate() : LocalDate
+ getDueDate() : LocalDate
+ getReturnDate() : LocalDate
+ getFinalFee() : long
+ getStatus() : LoanStatus
+ isActive() : boolean                           // status == BORROWING
+ isOverdueAt(date : LocalDate) : boolean        // active && dueDate.isBefore(date)
+ markReturned(returnDate : LocalDate, fee : long) : void
                                    // chỉ chạy một lần, đây là "setter" có kiểm tra
+ toString() : String

returnDate, finalFee và status không có setter riêng. Chúng được đổi cùng lúc qua markReturned() để tránh trạng thái nửa vời.

6) LibraryService
- publications : Map<String, Publication>   // HashMap theo id
- readers : Map<String, Reader>             // HashMap theo id
- loans : List<Loan>                        // ArrayList
- nextLoanNumber : long                     // bộ đếm mã phiếu, phải lưu file

+ LibraryService()                          // khởi tạo rỗng
+ getPublications() : Collection<Publication>
+ getReaders() : Collection<Reader>
+ getLoans() : List<Loan>
+ getNextLoanNumber() : long
+ setNextLoanNumber(n : long) : void        // dùng khi nạp từ file

// Tài liệu
+ addPublication(p : Publication) : void
+ findPublicationById(id : String) : Publication
+ searchPublicationsByTitle(keyword : String) : List<Publication>
+ updatePublication(id, newTitle, newAuthorOrIssue) : void
+ deletePublication(id : String) : void     // từ chối nếu từng có phiếu

// Độc giả
+ addReader(r : Reader) : void
+ findReaderById(id : String) : Reader
+ updateReader(id, newName, newContact) : void
+ deleteReader(id : String) : void          // từ chối nếu từng có phiếu

// Mượn / trả
+ borrow(readerId, publicationId, borrowDate : LocalDate) : Loan
+ returnLoan(loanId : String, returnDate : LocalDate) : long   // trả về phí
+ findLoanById(loanId : String) : Loan

// Tra cứu / thống kê
+ isAvailable(publicationId : String) : boolean
+ countActiveLoansOf(readerId : String) : int
+ getActiveLoansOfReader(readerId : String) : List<Loan>
+ getAvailablePublications() : List<Publication>
+ getOverdueLoans(date : LocalDate) : List<Loan>
+ getTotalFee() : long                      // cộng finalFee các phiếu đã trả
7) FileStorage
- filePath : String            // đường dẫn tương đối, ví dụ "data/library.dat"

+ FileStorage(filePath : String)
+ getFilePath() : String
+ setFilePath(filePath : String) : void
+ save(service : LibraryService) : void     // throws IOException
+ load() : LibraryService                   // throws IOException; file hỏng → báo lỗi
+ exists() : boolean
