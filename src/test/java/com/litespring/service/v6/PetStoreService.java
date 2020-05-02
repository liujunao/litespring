package com.litespring.service.v6;

import com.litespring.stereotype.Component;
import com.litespring.util.MessageTracker;

@Component(value = "petStore")
public class PetStoreService implements IPetStoreService {
    @Override
    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.addMsg("place order");
    }
}
