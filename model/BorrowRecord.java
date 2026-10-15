package model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// [借阅记录]模块
// 一条记录表示“某读者借了某本书”
// returnDate为null则表示“未归还”
public class BorrowRecord {
    public static final int BORROW_DAYS = 30; // 借期30天
    private final String id;
    private final String isbn;
    private final String readerId;
    private final LocalDate borrowDate;
    private LocalDate returnDate;
    
    public BorrowRecord(String id, String isbn, String readerId, LocalDate borrowDate) {
        this.id = id;
        this.isbn = isbn;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
    }

    public boolean isReturned(){
        return returnDate != null;
    }

    // 应还日期 = 借出日期 + 借期
    public LocalDate getDueDate(){
        return borrowDate.plusDays(BORROW_DAYS);
    }

    // 逾期天数：没到还书日期就返回0；如果超期了则返回一共逾期多少天
    // 已归还：按归还日算
    // 未归还：按今天算
    /* ChronoUnit.DAYS.between(A,B)：计算从日期A → 日期B之间相差了多少天数（B-A）
    - B在A之后：得到正数（逾期了）
    - B在A之前：得到负数（还没到期）*/
    public long getOverdueDays(){
        LocalDate end = isReturned() ? returnDate : LocalDate.now();
        long days = ChronoUnit.DAYS.between(getDueDate(), end);
        return Math.max(days, 0);                                   // 未逾期就返回0
    }

    public void markReturned(LocalDate date){
        this.returnDate = date;
    }

    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getReaderId() { return readerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        return id + "/读者 " + readerId + "/图书 " + isbn + "/借出 " + borrowDate + "/" + (isReturned() ? "已还 " + returnDate : "应还 " + getDueDate());
    }
}