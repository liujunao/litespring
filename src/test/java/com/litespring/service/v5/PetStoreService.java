package com.litespring.service.v5;

import com.litespring.beans.factory.annotation.Autowired;
import com.litespring.dao.v5.AccountDao;
import com.litespring.dao.v5.ItemDao;
import com.litespring.stereotype.Component;
import com.litespring.util.MessageTracker;

@Component(value = "petStore")
public class PetStoreService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.addMsg("place order"); //用于测试，实际使用时，会入侵代码
    }

    public void placeOrderWithException() {
        throw new NullPointerException();
    }
}
