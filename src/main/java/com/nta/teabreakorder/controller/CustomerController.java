package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Customer;
import com.nta.teabreakorder.model.auth.Group;
import com.nta.teabreakorder.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return customerService.get(pageable);
    }
    @GetMapping("/all")
    public ResponseEntity getAll() throws Exception {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) throws Exception {
        return customerService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Customer customer) throws Exception {
        return customerService.create(customer);
    }


    @PutMapping("")
    public ResponseEntity update(@RequestBody Customer customer) throws Exception {
        return customerService.update(customer);
    }

    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return customerService.deletes(ids);
    }
}
