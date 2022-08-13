package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return orderService.get(pageable);
    }


    @PostMapping("")
    public ResponseEntity create(@RequestBody Order order) throws Exception {
        return orderService.create(order);
    }
    @PutMapping("")
    public ResponseEntity update(@RequestBody Order order) throws Exception {
        return orderService.update(order);
    }
    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return orderService.deletes(ids);
    }
}
