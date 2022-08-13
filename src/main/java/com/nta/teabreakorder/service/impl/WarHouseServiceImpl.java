package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.WarHouseItem;
import com.nta.teabreakorder.repository.WarHouseItemRepository;
import com.nta.teabreakorder.repository.dao.WarHouseDao;
import com.nta.teabreakorder.service.WarHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarHouseServiceImpl implements WarHouseService {

    @Autowired
    private WarHouseItemRepository warHouseItemRepository;
    @Autowired
    private WarHouseDao warHouseDao;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(warHouseDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity create(WarHouseItem warHouseItem) throws Exception {
        return CommonUtil.createResponseEntityOK(warHouseItemRepository.save(warHouseItem));
    }

    @Override
    public ResponseEntity update(WarHouseItem warHouseItem) throws Exception {
        return CommonUtil.createResponseEntityOK(warHouseItemRepository.save(warHouseItem));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        warHouseItemRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }
}
