package com.litespring.tx;

import com.litespring.util.MessageTracker;

//用于模拟事务
public class TransactionManager {

    public void start() {
        System.out.println("start tx");
        MessageTracker.addMsg("start tx"); //用于测试，实际使用时，会入侵代码
    }

    public void commit() {
        System.out.println("commit tx");
        MessageTracker.addMsg("commit tx"); //用于测试，实际使用时，会入侵代码
    }

    public void rollback() {
        System.out.println("rollback tx");
        MessageTracker.addMsg("rollback tx"); //用于测试，实际使用时，会入侵代码
    }
}
