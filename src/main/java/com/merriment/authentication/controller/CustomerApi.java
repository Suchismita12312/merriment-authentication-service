package com.merriment.authentication.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.merriment.authentication.repository.NewCustomerRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.merriment.authentication.model.MerrimentError;
import com.merriment.authentication.model.NewCustomerRequest;
import com.merriment.authentication.model.ResponseMetaData;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2024-03-19T13:48:13.393+01:00")
@RestController
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
    @Autowired
    NewCustomerRepository newCustomerRepository;
    @PostMapping(value = "/newCustomer")
    public ResponseMetaData newCustomer(@RequestBody NewCustomerRequest newCustomerRequest) throws RestClientException {
        Object postBody = newCustomerRequest;
        
        // verify the required parameter 'newCustomerRequest' is set
        if (newCustomerRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'newCustomerRequest' when calling newCustomer");
        }else{
            ResponseMetaData response = newCustomerRepository.newCustomer(newCustomerRequest);
        }
        
        return null;
    }
}
