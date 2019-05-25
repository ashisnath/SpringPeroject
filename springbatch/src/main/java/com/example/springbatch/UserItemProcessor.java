package com.example.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User,User> {


    @Override
    public User process(User user) throws Exception {

        user.setMailInd(new Boolean("False"));

      return user;
    }



}
