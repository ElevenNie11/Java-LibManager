package exception;
// 自定义异常（业务异常）
public class BusinessException extends RuntimeException {
    public BusinessException(String message){
        super(message);
    }
}
// 作用如下：
// 创建一个自己的业务运行时异常，专门用来抛出业务上的错误（比如库存不足、借书逾期、余额不够）
// 不用像受检异常那样强制写 try-catch，上层统一捕获给用户提示
// super(message)调用父类 RuntimeException 的构造，把错误信息传给父类保存，后续可以用 e.getMessage() 获取提示文字

// 关键是继承 RuntimeException（属于“非受检异常”）
// 抛出它的时候，方法上不需要写 throws 声明，调用方也不用必须强制try-catch
// 对比：如果继承Exception，就是受检异常，所有调用代码都要强制捕获或声明抛出，很麻烦

// 使用途径如下：
/*
业务逻辑————借书
public void borrowBook(){
    if(book.getTotalCount() <= 0){
        // 库存不足，直接抛出业务异常
        throw new BusinessException("图书库存不足，无法借阅");
    }
}
*/