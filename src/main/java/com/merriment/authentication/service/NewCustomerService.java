package com.merriment.authentication.service;

import com.merriment.authentication.dao.NewCustomerDao;
import com.merriment.authentication.model.MerrimentError;
import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.ResponseMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.merriment.authentication.dao.NewCustomerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewCustomerService {


    private final NewCustomerInterface newCustomerDao;

    @Autowired
    public NewCustomerService(NewCustomerDao newCustomerDao) {
        this.newCustomerDao = newCustomerDao;
    }

    public Boolean validatePassword(String password) {
        if(password == null){
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Boolean validateEmail(String email) {
        if(email == null){
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public ResponseMetaData registerNewCustomer(NewCustomerRequest newCustomerRequest) {
        MerrimentError error = new MerrimentError();
        Integer value =0;
        ResponseMetaData response = new ResponseMetaData();
        List<MerrimentError> errorList = new ArrayList<MerrimentError>();
        Boolean isValidPassword;
        Boolean isValidUser;
        isValidPassword = this.validatePassword(newCustomerRequest.getPassword());
        isValidUser = this.validateEmail(newCustomerRequest.getEmail());
        if(!isValidPassword){
            error.setErrorCode("MER100");
            error.setErrorMessage("Password validation failed");
            errorList.add(error);
            this.mapToError(errorList,response);
        }
        if(!isValidUser){
            error.setErrorCode("MER101");
            error.setErrorMessage("Email validation failed");
            errorList.add(error);
            this.mapToError(errorList,response);
        }
        if(isValidUser && isValidPassword) {
            value = newCustomerDao.registerNewCustomer(newCustomerRequest);
        }
        if(value>0){
            response.setStatusCode("Success");
            response.setMessage(value.toString());
        }else{
            response.setMessage("Some Error Occurred");
            response.setStatusCode("Error");
        }
        return response;
    }

    private void mapToError(List<MerrimentError> error, ResponseMetaData response) {

        response.setErrorDesInfo(error);
        response.setMessage("An Error Occurred");
        response.setStatusCode("Error");
    }

}
