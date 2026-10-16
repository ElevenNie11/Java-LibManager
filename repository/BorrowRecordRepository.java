package repository;
import model.BorrowRecord;
import java.util.*;
// 借阅记录仓库：记录只追加、不删除，读者还书更改returnDate

public class BorrowRecordRepository {
    // 图书仓库和读者仓库都是用 Map 来存储的
    // 借阅记录仓库用 List 来存储
    private final List<BorrowRecord> records = new ArrayList<>();

    public void save(BorrowRecord record){
        records.add(record);
    }

    // 查询该读者和图书尚未归还的记录
    // 借书时放重复
    // 还书时定位记录
    public Optional<BorrowRecord> findActive(String readerId, String isbn){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getReaderId().equals(readerId) && record.getIsbn().equals(isbn)){
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    public List<BorrowRecord> findAll(){
        List<BorrowRecord> all = new ArrayList<>(records);
        return all;
    }

    public List<BorrowRecord> findActiveAll(){
        List<BorrowRecord> activeAll = new ArrayList<>();
        for(BorrowRecord record: records){
            if(!record.isReturned()){
                activeAll.add(record);
            }
        }
        return activeAll;
    }

    public List<BorrowRecord> findByReader(String readerId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (record.getReaderId().equals(readerId)) {
                result.add(record);
            }
        }
        return result;
    }

    // 删除图书前的检查：这本图书还有没有“没归还”的
    public boolean hasActiveByIsbn(String isbn){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getIsbn().equals(isbn)){
                return true;
            }
        }
        return false;
    }

    // 注销读者前的检查：这位读者还有没有“未归还”的历史记录
    public boolean hasActiveByReader(String readerId){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getReaderId().equals(readerId)){
                return true;
            }
        }
        return false;
    }
}
