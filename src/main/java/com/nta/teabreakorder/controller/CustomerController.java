package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Customer;
import com.nta.teabreakorder.model.auth.Group;
import com.nta.teabreakorder.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@SecurityRequirement(name="securityAPI")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customer by pagination")
    @GetMapping("")
    public ResponseEntity getAll(@Parameter(description = "Page request param")  @RequestParam(required = false) Integer page,
                                 @Parameter(description = "PageSize request param") @RequestParam(required = false) Integer pageSize,
                                 @Parameter(description = "Search requestParam") @RequestParam(required = false) String searchData,
                                 @Parameter(description = "Sort requestParam : c.key desc...") @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return customerService.get(pageable);
    }

    @Operation(summary = "Get all customer not pagination")
    @GetMapping("/all")
    public ResponseEntity getAll() throws Exception {
        return customerService.getAll();
    }
    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) throws Exception {
        return customerService.getById(id);
    }

    @Operation(summary = "Create new customer")
    @PostMapping("")
    public ResponseEntity create(@RequestBody Customer customer) throws Exception {
        return customerService.create(customer);
    }

    @Operation(summary = "Update customer")
    @PutMapping("")
    public ResponseEntity update(@RequestBody Customer customer) throws Exception {
        return customerService.update(customer);
    }
    @Operation(summary = "Delete customer")
    @DeleteMapping("")
    public ResponseEntity deletes(@Parameter(description = "List id") @RequestBody List<Long> ids) throws Exception {
        return customerService.deletes(ids);
    }
}
