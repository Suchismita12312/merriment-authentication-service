package com.merriment.authentication.service;

import com.merriment.authentication.dao.NewCustomerDao;
import com.merriment.authentication.model.MerrimentError;
import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.ResponseMetaData;
import com.merriment.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.merriment.authentication.dao.NewCustomerInterface;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewCustomerService {


    private final NewCustomerInterface newCustomerDao;

    Logger logger = Logger.getLogger(NewCustomerService.class.getName());

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


    public ResponseMetaData registerNewCustomer(NewCustomerRequest newCustomerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        MerrimentError error = null;
        Integer value =0;
        ResponseMetaData response = new ResponseMetaData();
        List<MerrimentError> errorList = new ArrayList<MerrimentError>();
        Boolean isValidPassword;
        Boolean isValidUser;
        isValidPassword = this.validatePassword(newCustomerRequest.getPassword());
        isValidUser = this.validateEmail(newCustomerRequest.getEmail());
        if(!isValidPassword){
            error = new MerrimentError();
            error.setErrorCode("MER100");
            error.setErrorMessage("Password validation failed");
            errorList.add(error);
            this.mapToError(errorList,response);
        }
        if(!isValidUser){
            error = new MerrimentError();
            error.setErrorCode("MER101");
            error.setErrorMessage("Email validation failed");
            errorList.add(error);
            this.mapToError(errorList,response);
        }
        if(isValidUser && isValidPassword) {
            Integer checkEmailExist = newCustomerDao.getNumberOfUserByEmail(newCustomerRequest.getEmail());
            Integer checkUserNameExist = newCustomerDao.getNumberOfUserByUserName(newCustomerRequest.getUserName());
            if(checkEmailExist>0){
                error = new MerrimentError();
                error.setErrorCode("MER105");
                error.setErrorMessage("Email already exist");
                errorList.add(error);
                this.mapToError(errorList,response);
            } else if (checkUserNameExist > 0) {
                error = new MerrimentError();
                error.setErrorCode("MER106");
                error.setErrorMessage("UserName already exist");
                errorList.add(error);
                this.mapToError(errorList,response);
            } else {
                String password = createHash(newCustomerRequest.getPassword());
                newCustomerRequest.setPassword(password);
                logger.info(password);
                value = newCustomerDao.registerNewCustomer(newCustomerRequest);
            }
        }
        if(response.getErrorDesInfo() == null || response.getErrorDesInfo().isEmpty() ){
            response.setStatusCode("Success");
            response.setMessage(value.toString());
        }else{
            response.setStatusCode("Error");
            response.setMessage("An Error Occurred");
        }
        return response;
    }

    private String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        String salt = BCrypt.gensalt(16);
        return BCrypt.hashpw(password,salt);
    }

    private void mapToError(List<MerrimentError> error, ResponseMetaData response) {

        response.setErrorDesInfo(error);
        response.setMessage("An Error Occurred");
        response.setStatusCode("Error");
    }

    public ResponseMetaData login(User user) {
        ResponseMetaData response = new ResponseMetaData();
        MerrimentError error = new MerrimentError();
        List<MerrimentError> errorList =new ArrayList<MerrimentError>();
        List<User> userInDB = null;
        Boolean result = false;
        if(user.getEmail() != null){
            userInDB=newCustomerDao.getUserByEmail(user.getEmail());
        }else{
            userInDB=newCustomerDao.getUserByUserName(user.getUserName());
        }
        if(userInDB == null || userInDB.isEmpty()){
            error.setErrorCode("MER108");
            error.setErrorMessage("User does not exist");
            errorList.add(error);
            this.mapToError(errorList,response);
            return response;
        } else if (userInDB.size()>1) {
            error.setErrorCode("MER109");
            error.setErrorMessage("Multiple User found");
            errorList.add(error);
            this.mapToError(errorList,response);
            return response;
        }
        try {

            result = compareHash(user,userInDB.get(0));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        if(result){
            response.setStatusCode("Success");
            response.setMessage("Login Successful");
        }else{
            error.setErrorCode("MER107");
            error.setErrorMessage("Invalid credentials");
            errorList.add(error);
            this.mapToError(errorList,response);
        }
        return response;
    }

    private Boolean compareHash(User user, User userInDB) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = user.getPassword();
        String passwordInDB = userInDB.getPassword();
        boolean password_verified = false;
        if(null == passwordInDB || !passwordInDB.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password, passwordInDB);

        return(password_verified);

    }
}
