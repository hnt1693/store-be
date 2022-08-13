package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.model.WarHouseItem;
import com.nta.teabreakorder.repository.ProductRepository;
import com.nta.teabreakorder.repository.WarHouseItemRepository;
import com.nta.teabreakorder.repository.dao.ProductDao;
import com.nta.teabreakorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WarHouseItemRepository warHouseItemRepository;


    @Autowired
    private ProductDao productDao;


    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(productDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity create(Product product) throws Exception {
        product = productRepository.save(product);
        WarHouseItem warHouseItem = new WarHouseItem(0L, product, 0, null, 0);
        warHouseItemRepository.save(warHouseItem);
        return CommonUtil.createResponseEntityOK(productRepository.save(product));
    }

    @Override
    public ResponseEntity update(Product product) throws Exception {
        return CommonUtil.createResponseEntityOK(productRepository.save(product));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        productRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity getAllNotUsedInWarHouse() throws Exception {
        return CommonUtil.createResponseEntityOK(productRepository.getAllNotUsedInWarHouse());
    }

    @Override
    public ResponseEntity getAll() throws Exception {
        return CommonUtil.createResponseEntityOK(productRepository.getAll());
    }
}
