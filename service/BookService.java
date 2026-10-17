package service;
import model.Book;                  // 图书实体
import repository.*;                // 负责图书的增删改查
import exception.BusinessException;
import java.util.List;
// 图书业务层service：借还规则
import java.util.Optional;

public class BookService {
    private final BookRepository bookRepo;
    private final BorrowRecordRepository recoredRepo;

    public BookService(BookRepository bookRepo, BorrowRecordRepository recoredRepo){
        this.bookRepo = bookRepo;
        this.recoredRepo = recoredRepo;
    }

    // 找
    public Book getByIsbn(String isbn){
        // 根据ISBN查询图书，得到Optional容器
        Optional<Book> optBook = bookRepo.findByIsbn(isbn);
        // 如果没有找到图书，则抛出业务异常
        if(optBook.isEmpty()){
            throw new BusinessException("该图书不存在" + isbn);
        }
        // 找到了，取出图书对象返回
        return optBook.get();
    }

    // 增
    public Book addBook(String isbn, String title, String author, int totalCount){
        if(isbn == null || isbn.isBlank()) throw new BusinessException("ISBN不能为空");
        if(title == null || title.isBlank()) throw new BusinessException("书名不能为空");
        if(author == null || author.isBlank()) throw new BusinessException("作者不能为空");
        if(totalCount <= 0) throw new BusinessException("无馆藏图书");
        if(bookRepo.existsByIsbn(isbn)) throw new BusinessException("ISBN已存在" + isbn);

        Book book = new Book(isbn, title, author, totalCount);
        bookRepo.save(book);
        return book;
    }

    // 改（馆藏总数不能小于已借出数量，否则库存会变成负数）
    public void updateBook(String isbn, String newTitle, String newAuthor, int newTotalCount){
        Book book = getByIsbn(isbn);
        if(newTotalCount > 0 && newTotalCount < book.getBorrowedCount()){
            throw new BusinessException("馆藏总数不能小于已借出的 " + book.getBorrowedCount() + " 本");
        }
        if(newTitle != null && !newTitle.isBlank()) book.setTitle(newTitle);
        if(newAuthor != null && !newAuthor.isBlank()) book.setAuthor(newAuthor);
        if(newTotalCount > 0) book.setTotalCount(newTotalCount);
    }

    // 删（先确认没有“未归还”的记录，防止记录悬空）
    public void removeBook(String isbn){
        getByIsbn(isbn);                // 不可以删除这条语句，否则逻辑不完整（见下面）
        if(recoredRepo.hasActiveByIsbn(isbn)) throw new BusinessException("该书还有未归还的记录，不能删除！");
        bookRepo.deleteByIsbn(isbn);
    }

    public List<Book> listAll(){
        return bookRepo.findAll();
    }

    public List<Book> search(String keyword){
        return bookRepo.search(keyword);
    }
}


// getByIsbn(isbn);                // 不可以删除这条语句，否则逻辑不完整（见下面）
// 如果去掉这个语句，假设用户输入了一个根本不存在的ISBN
// hasActiveByIsbn() 就会 return false
// if跳过：不会抛出异常，直接走到bookRepo.deleteByIsbn(isbn); 实际上什么也没删掉，没有任何提示
// 没有任何提示，逻辑就有漏洞：到底是该书被删完了？还是压根就不存在这本书？
// 保留 getByIsbn(isbn); 使得业务逻辑更加严谨