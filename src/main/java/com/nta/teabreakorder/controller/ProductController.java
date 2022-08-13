package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return productService.get(pageable);
    }

    @GetMapping("/all")
    public ResponseEntity getAllNoPagination() throws Exception {
        return productService.getAll();
    }
    @GetMapping("/all-not-in-warhouse")
    public ResponseEntity getAllNotUsedInWarHouse() throws Exception {
        return productService.getAllNotUsedInWarHouse();
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Product product) throws Exception {
        return productService.create(product);
    }
    @PutMapping("")
    public ResponseEntity update(@RequestBody Product product) throws Exception {
        return productService.update(product);
    }
    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return productService.deletes(ids);
    }
}
