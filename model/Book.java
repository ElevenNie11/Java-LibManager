package model;
// 图书实体：只负责装数据，不负责业务判断
public class Book {
    private final String isbn;        // 唯一编号 主键 创建后不可修改
    private String title;
    private String author;
    private int totalCount;    // 馆藏总数 
    private int borrowedCount; // 当前借出数量 
    
    public Book(String isbn, String title, String author, int totalCount){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCount = totalCount;
    }

    // 可借数量 = 馆藏总数 - 已借出数量
    public int getAvailableCount(){
        return totalCount - borrowedCount;
    }

    public boolean isAvailable(){
        if(getAvailableCount() > 0){
            return true;
        }
        return false;
    }

    // 借出一本书：由 BorrowService 通过业务校验之后调用
    public void borrowOne(){
        if(!isAvailable()){
            throw new IllegalStateException("《" + title +"》已被全部借出，现在已无空余！");
        }
        borrowedCount++;
    }

    // 归还一本书
    public void returnOne(){
        if(borrowedCount <= 0){
            throw new IllegalStateException("《" + title + "》没有可归还的借出记录。");
        }
        borrowedCount--;
    }

    // 由于都是private类型的变量，所以需要setter和getter去修改和访问它们
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public int getBorrowedCount() { return borrowedCount; }

    @Override
    public String toString(){
        return isbn + "/" + title + "/" + author + " :" + "馆藏" + totalCount + "本" + ";" + "可借阅" + getAvailableCount() +"本" ;
    }
}
