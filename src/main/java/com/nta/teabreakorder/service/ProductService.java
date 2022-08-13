package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Product;
import org.springframework.http.ResponseEntity;

public interface ProductService extends CommonService<Product> {
    ResponseEntity getAllNotUsedInWarHouse() throws Exception;
    ResponseEntity getAll() throws Exception;
}
