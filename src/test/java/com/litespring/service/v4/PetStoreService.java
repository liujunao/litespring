package com.litespring.service.v4;

import com.litespring.beans.factory.annotation.Autowired;
import com.litespring.dao.v4.AccountDao;
import com.litespring.dao.v4.ItemDao;
import com.litespring.stereotype.Component;

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
}
