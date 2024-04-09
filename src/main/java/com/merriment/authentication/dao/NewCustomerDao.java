package com.merriment.authentication.dao;

import com.merriment.authentication.model.NewCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class NewCustomerDao implements NewCustomerInterface{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer registerNewCustomer(NewCustomerRequest newCustomerRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO USER (Username,Password,Email,ServiceCode) VALUES (?,?,?,?)";
        jdbcTemplate.update(connection ->{
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newCustomerRequest.getUserName());
            ps.setString(2,newCustomerRequest.getPassword());
            ps.setString(3,newCustomerRequest.getEmail());
            ps.setString(4,newCustomerRequest.getServiceCode());
            return ps;
        }, keyHolder);
        return (Integer) keyHolder.getKey();
    }
}
