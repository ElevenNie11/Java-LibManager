package repository;
import model.Book;
import java.util.*;
// 图书仓库：专门负责图书数据的增删改查（图书的数据访问层），不含业务规则
// 用Map存在内存里，之后换数据库只用改动这个类，上层业务代码不用动
// [注意]：内存存储，程序一旦关闭，所有数据全部丢失！只是模拟数据库

public class BookRepository {
    // 键：图书ISBN编号
    // 值：Book对象实体
    private final Map<String, Book> storage = new LinkedHashMap<>();  // LinkedHashMap是有序Map，保留图书插入的先后顺序
    
    //保存更新图书
    public void save(Book book){
        // 语法：put(key,value)
        // 如果这个ISBN不存在：新增图书
        // 如果ISBN已经存在：覆盖更新这本图书
        // 相当于数据库的 insert /update
        storage.put(book.getIsbn(), book);
    }

    // 根据ISBN查询图书
    // 根据ISBN查询图书，有可能查到，也有可能查不到
    // Optional<Book> 就是一个容器盒子，盒子里面要么装着一本Book，要么就是空的（不会是null）
    // 所以用Optional就可以避免空指针异常（NullPointerException）
    // storage.get(isbn) 从Map里面按照isbn查找图书
    // 找到返回Book对象，找不到就返回null
    // Optional.ofNullable(变量) 表示可以接收null
    // 如果括号里是Book对象：把Book装进Optional盒子
    // 如果括号里是null：创建一个空的Optional盒子（而不是返回null）
    public Optional<Book> findByIsbn(String isbn){
        return Optional.ofNullable(storage.get(isbn));
    }

    // 判断该ISBN是否存在
    public boolean existsByIsbn(String isbn){
        if(storage.containsKey(isbn)){
            return true;
        }
        return false;
    }

    // 根据ISBN删除图书
    public boolean deleteByIsbn(String isbn){
        if(storage.remove(isbn) != null){
            return true;
        }
        return false;
    }

    // 查询全部图书
    public List<Book> findAll(){
        // storage.values()：取出Map里面所有Book对象
        // new ArrayList<>(...)：包装成List返回
        // List<Book> 代表图书集合
        List<Book> all = new ArrayList<>(storage.values());
        return all;
    }

    // search模糊搜索（按书名/作者，忽略大小写）
    public List<Book> search(String keyword){
        String kw = keyword.toLowerCase();
        List<Book> result = new ArrayList<>();
        for(Book book: storage.values()){
            if(book.getTitle().toLowerCase().contains(kw) || book.getAuthor().toLowerCase().contains(kw)){
                result.add(book);
            }
        }
        return result;
    }
}


// 调用示例

// 在业务层Service的调用示例：

/*
BookRepository repo = new BookRepository();
//新增
repo.save(new Book("978001","Java编程","张三",5));
//查询
Optional<Book> b = repo.findByIsbn("978001");
//搜索书名含Java的书
List<Book> list = repo.search("Java");
*/

// 返回类型 Optional<Book> 不是直接返回Book
// Book代表一本书，可能是null，一旦调用 book.getTitle() 就有空指针风险
// Optional<Book>：装Book的盒子，拿到盒子之后必须先判断盒子里面有没有东西，才能取出Book
// .get()取出里面的对象
// .isPresent()盒子里面有返回值true
// isEmpty()盒子里面是空返回true
// orElse(默认值)有值就拿值，如果是空的，就返回我们自己给的默认对象
// orElseThrow(异常)有值拿值，空则抛出异常
/*
Optional<Book> optBook = bookRepo.findByIsbn("978123");
// 判断盒子里面是否存在图书
if(optBook.isPresent()){
    // 存在，取出Book
    Book book = optBook.get();
    System.out.println(book.getTitle());
}else{
    // 不存在，抛出业务异常
    throw new BusinessException("该ISBN图书不存在");
}

*/