package model;
// 读者实体：借书证号是唯一标识
public class Reader {
    private final String id;   // 借书证号
    private String name;
    private String phone;
    private int borrowedCount; // 当前在借数量，用于判断借阅上限

    public Reader(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public void borrowOne(){
        borrowedCount++;
    }
    public void returnOne(){
        if(borrowedCount > 0){
            borrowedCount--;
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getBorrowedCount() { return borrowedCount; }

    @Override
    public String toString(){
        return id + "/" + name + "/" + phone + " /在借" + borrowedCount + "本";
    }
}