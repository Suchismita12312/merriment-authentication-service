package com.merriment.authentication.dao;

import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public Integer getNumberOfUserByEmail(String email) {
        String query = "SELECT count(*) FROM USER WHERE Email = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, email);
    }

    @Override
    public Integer getNumberOfUserByUserName(String userName) {
        String query = " SELECT count(*) FROM USER WHERE UserName= ?";
        return jdbcTemplate.queryForObject(query, Integer.class,userName);
    }

    @Override
    public List<User> getUserByEmail(String email) {
        String query = "SELECT * FROM USER WHERE Email =?";
        List<User> userList = new ArrayList<User>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, email);
        for (Map row : rows) {
            User obj = new User();
            obj.setEmail((String) row.get("Email"));
            obj.setUserName((String) row.get("UserName"));
            obj.setPassword((String) row.get("Password"));
            userList.add(obj);
        }
        return userList;
    }

    @Override
    public List<User> getUserByUserName(String userName) {
        String query = "SELECT * FROM USER WHERE USERNAME =?";
        List<User> userList = new ArrayList<User>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, userName);
        for (Map row : rows) {
            User obj = new User();
            obj.setEmail((String) row.get("Email"));
            obj.setUserName((String) row.get("UserName"));
            obj.setPassword((String) row.get("Password"));
            userList.add(obj);
        }
        return userList;
    }
}
