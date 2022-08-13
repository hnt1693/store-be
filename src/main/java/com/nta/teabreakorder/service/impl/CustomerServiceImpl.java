package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Customer;
import com.nta.teabreakorder.repository.CustomerRepository;
import com.nta.teabreakorder.repository.dao.CustomerDao;
import com.nta.teabreakorder.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(customerDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return CommonUtil.createResponseEntityOK(customerRepository.findById(id));
    }

    @Override
    public ResponseEntity create(Customer customer) throws Exception {
        return CommonUtil.createResponseEntityOK(customerRepository.save(customer));
    }

    @Override
    public ResponseEntity update(Customer customer) throws Exception {
        return CommonUtil.createResponseEntityOK(customerRepository.save(customer));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        customerRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity getAll() {
        return CommonUtil.createResponseEntityOK(customerRepository.findAll());
    }
}
