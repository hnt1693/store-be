package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService extends CommonService<Customer>{
    ResponseEntity getAll();
}
