package com.merriment.authentication.controller;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.merriment.authentication.model.User;
import com.merriment.authentication.service.NewCustomerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import com.merriment.authentication.model.MerrimentError;
import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.ResponseMetaData;


@RestController
@RequestMapping("/merriment/authentication")
public class CustomerApi {

    /**
     * New Customer
     * New Customer to Merriment
     * <p><b>200</b> - Successful Response
     * <p><b>400</b> - Y803 : eventName required&lt;br&gt;Y803 : callbackUrl required&lt;br&gt;Y800 : Invalid value for callbackUrl
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Not Found
     * @param newCustomerRequest newCustomerRequest
     * @return ResponseMetaData
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */

    private final NewCustomerService newCustomerService;
    @Autowired
    public CustomerApi(NewCustomerService newCustomerService){
        this.newCustomerService = newCustomerService;
    }
    @PostMapping(value = "/newcustomer")
    public ResponseEntity<ResponseMetaData> newCustomer(@RequestBody NewCustomerRequest newCustomerRequest) throws RestClientException {

            ResponseMetaData response;
            // verify the required parameter 'newCustomerRequest' is set
            if (newCustomerRequest == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'newCustomerRequest' when calling newCustomer");
            } else {
                try {
                    response = newCustomerService.registerNewCustomer(newCustomerRequest);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            }

        
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping(value = "/login")
    public ResponseEntity<ResponseMetaData> login(@RequestBody User user){
        ResponseMetaData response;
        if(user == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'newCustomerRequest' when calling newCustomer");
        }
        response = newCustomerService.login(user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
