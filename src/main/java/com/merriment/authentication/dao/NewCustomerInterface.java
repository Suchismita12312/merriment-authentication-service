package com.merriment.authentication.dao;

import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.User;

import java.util.List;

public interface NewCustomerInterface {

    Integer registerNewCustomer(NewCustomerRequest newCustomerRequest);


    Integer getNumberOfUserByEmail(String email);

    Integer getNumberOfUserByUserName(String userName);

    List<User> getUserByEmail(String email);

    List<User> getUserByUserName(String userName);
}
