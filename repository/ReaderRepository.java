package repository;
import model.Reader;
import java.util.*;
// 读者仓库（数据访问层）
// 接口风格与图书仓库保持一致

public class ReaderRepository {
    private final Map<String, Reader> storage = new LinkedHashMap<>();
    
    public void save(Reader reader){
        storage.put(reader.getId(), reader);
    }

    public Optional<Reader> findById(String id){
        return Optional.ofNullable(storage.get(id));
    }

    public boolean existsById(String id){
        return storage.containsKey(id);
    }

    public boolean deleteById(String id){
        return storage.remove(id) != null;
    }

    public List<Reader> findAll(){
        List<Reader> all = new ArrayList<>(storage.values());
        return all;
    }

    public List<Reader> search(String keyword){
        String kw = keyword.toLowerCase();
        List<Reader> result = new ArrayList<>();
        for(Reader reader: storage.values()){
            if(reader.getName().toLowerCase().contains(kw) || reader.getPhone().toLowerCase().contains(kw)){
                result.add(reader);
            }
        }
        return result;
    }
}
