package com.example.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class UserMailProcesser implements ItemProcessor<User,User>{




    @Override
    public User process(User u) throws Exception {
        System.out.println("mail send");
        u.setMailInd(new Boolean("True"));

        System.out.println(u.getMailInd());
        return u;
    }
}
