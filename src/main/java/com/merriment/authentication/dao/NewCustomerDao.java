package com.merriment.authentication.dao;

import com.merriment.authentication.model.NewCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NewCustomerDao implements NewCustomerInterface{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int registerNewCustomer(NewCustomerRequest newCustomerRequest) {
        return jdbcTemplate.update("INSERT INTO USER (Username,Password,Email,ServiceCode) VALUES (?,?,?,?)",
                new Object[]{newCustomerRequest.getUserName(),newCustomerRequest.getPassword(),
                newCustomerRequest.getEmail(), newCustomerRequest.getServiceCode()});
    }
}
