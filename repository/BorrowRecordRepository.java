package repository;
import model.BorrowRecord;
import java.util.*;
// 借阅记录仓库的业务特点：
// 1. 借阅记录只追加、不删除
// 2. 借书就新增一条记录，还书不删掉这条记录
// 3. 还书只是修改记录里的 returnDate 归还日期，保留借阅历史

public class BorrowRecordRepository {
    // 图书仓库和读者仓库都是用 Map 来存储的
    // 借阅记录仓库用 List 来存储
    private final List<BorrowRecord> records = new ArrayList<>();

    public void save(BorrowRecord record){
        records.add(record);              // 功能：新增一条借阅记录，追加到List末尾
    }

    // 查询该读者和图书尚未归还的记录
    // 使用场景1：防止同一读者，同一本书重复借（如果查到active记录，说明这本书他还没有归还，不能再次借阅）
    // 使用场景2：还书时找到这条未归还记录，修改 returnDate 标记为“已归还”
    public Optional<BorrowRecord> findActive(String readerId, String isbn){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getReaderId().equals(readerId) && record.getIsbn().equals(isbn)){
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    // 功能：查询全部借阅历史记录（已归还 + 未归还全部）
    public List<BorrowRecord> findAll(){
        List<BorrowRecord> all = new ArrayList<>(records);
        return all;
    }

    // 功能：查询所有“未归还”的借阅记录，筛选所有正在借的书
    public List<BorrowRecord> findActiveAll(){
        List<BorrowRecord> activeAll = new ArrayList<>();
        for(BorrowRecord record: records){
            if(!record.isReturned()){
                activeAll.add(record);
            }
        }
        return activeAll;
    }

    // 功能：查询某个读者的全部借阅历史（包含已经归还 + 还未归还）
    public List<BorrowRecord> findByReader(String readerId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (record.getReaderId().equals(readerId)) {
                result.add(record);
            }
        }
        return result;
    }

    // 删除图书前的安全检查：这本图书还有没有“没归还”的借阅记录
    // return true： 有人还没有归还这本书，这本书就不能删除
    // return false：全部归还，可以删除该图书
    public boolean hasActiveByIsbn(String isbn){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getIsbn().equals(isbn)){
                return true;
            }
        }
        return false;
    }

    // 注销读者前的检查：这位读者还有没有“未归还”的历史记录
    // return true： 该读者还有书没还，不能注销该读者
    // return false：该读者全部该读者没有“未归还”的图书，可以注销
    public boolean hasActiveByReader(String readerId){
        for(BorrowRecord record: records){
            if(!record.isReturned() && record.getReaderId().equals(readerId)){
                return true;
            }
        }
        return false;
    }
}

// 本脚本的重点：为什么借阅记录用List存储？
// 注释：图书仓库和读者仓库都是用 Map 来存储的，借阅记录仓库用 List 来存储
// 图书有唯一主键ISBN，读者有唯一主键id
// 根据ISBN单条查询、新增、删除
// Map 特点：按键快速查找，get(isbn)一步找到，适合“按唯一标识精准查询单条”
// 借阅记录没有单一唯一key，查询条件是多字段组合：
// 1. 读者id + ISBN找未还记录
// 2. 根据读者id，查他所有历史（多条记录）
// 3. 根据ISBN查有没有未归还记录

// 总结：
// 1. Map   需要一个固定的key（比如单一字符串），但借阅记录我们经常按多个字段、批量筛选，用 List 循环遍历筛选更方便
// 2. Map： 适合通过唯一编号，快速查【单条】数据（图书、读者）
// 3. List：适合流水记录、批量筛选、多条件组合查询，经常返回【多条】结果（借阅记录）