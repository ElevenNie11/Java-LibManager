package service;
import exception.BusinessException;
import model.Reader;
import repository.*;
import java.util.List;
// 读者业务：注册账户、修改、注销账户
import java.util.Optional;

public class ReaderService {
    private final ReaderRepository readerRepo;
    private final BorrowRecordRepository recordRepo;

    public ReaderService(ReaderRepository readerRepo, BorrowRecordRepository recordRepo){
        this.readerRepo = readerRepo;
        this.recordRepo = recordRepo;
    }

    public Reader getById(String id){
        Optional<Reader> optReader = readerRepo.findById(id);
        if(optReader.isEmpty()){
            throw new BusinessException("该借书证号不存在" + id);
        }
        return optReader.get();
    }

    public Reader addReader(String id, String name, String phone){
        if(id == null || id.isBlank()) throw new BusinessException("借书证号不能为空！");
        if(name == null || name.isBlank()) throw new BusinessException("姓名不能为空！");
        if(phone == null || phone.isBlank()) throw new BusinessException("电话号码不能为空号！");
        if(readerRepo.existsById(id)) throw new BusinessException("借书证号已存在！" + id);

        Reader reader = new Reader(id, name, phone);
        readerRepo.save(reader);
        return reader;
    }

    public void updateReader(String id, String newName, String newPhone){
        Reader reader = getById(id);
        reader.setName(newName.trim());
        reader.setPhone(newPhone.trim());
    }

    public void removeReader(String id){
        getById(id);
        if(recordRepo.hasActiveByReader(id)) throw new BusinessException("该读者还有未归还的图书，不能注销");
        readerRepo.deleteById(id);
    }

    public List<Reader> listAll(){
        return readerRepo.findAll();
    }

    public List<Reader> search(String keyword){
        return readerRepo.search(keyword);
    }
}
