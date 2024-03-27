package com.merriment.authentication.repository;

import com.merriment.authentication.model.MerrimentError;
import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.ResponseMetaData;
import com.merriment.authentication.service.NewCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class NewCustomerRepository {

    @Autowired
    NewCustomerService newCustomerService;

    @Autowired
    ResponseMetaData responseMetaData;

    public ResponseMetaData newCustomer(NewCustomerRequest newCustomerRequest) {
        MerrimentError error = new MerrimentError();
        List<MerrimentError> errorList = new ArrayList<MerrimentError>();
        Boolean isValidPassword;
        Boolean isValidUser;
        isValidPassword = newCustomerService.validatePassword(newCustomerRequest.getPassword());
        isValidUser = newCustomerService.validateEmail(newCustomerRequest.getEmail());
        if(!isValidPassword){
            error.setErrorCode("MER100");
            error.setErrorMessage("Password validation failed");
            errorList.add(error);
            this.mapToError(errorList);
        }
        if(!isValidUser){
            error.setErrorCode("MER101");
            error.setErrorMessage("Email validation failed");
            errorList.add(error);
            this.mapToError(errorList);
        }
        if(isValidUser && isValidPassword){
            responseMetaData = newCustomerService.registerNewCustomer(newCustomerRequest);
        }

        return responseMetaData;
    }

    private void mapToError(List<MerrimentError> error) {
        if(responseMetaData == null){
            this.getResponseMetadata();
        }
        responseMetaData.setErrorDesInfo(error);
        responseMetaData.setMessage("An Error Occurred");
        responseMetaData.setStatusCode("Error");
    }

    private void getResponseMetadata() {
        responseMetaData = new ResponseMetaData();
    }
}


