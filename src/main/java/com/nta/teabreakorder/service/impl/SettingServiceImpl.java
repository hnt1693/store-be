package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Settings;
import com.nta.teabreakorder.repository.common.SettingRepository;
import com.nta.teabreakorder.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return CommonUtil.createResponseEntityOK(settingRepository.findById(id));
    }

    @Override
    public ResponseEntity create(Settings settings) throws Exception {
        return CommonUtil.createResponseEntityOK(settingRepository.save(settings));
    }

    @Override
    public ResponseEntity update(Settings settings) throws Exception {
        return CommonUtil.createResponseEntityOK(settingRepository.save(settings));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        settingRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity getAll() throws Exception {
        return CommonUtil.createResponseEntityOK(settingRepository.findAll());
    }

    @Override
    public ResponseEntity getByKey(String key) throws Exception {
        return CommonUtil.createResponseEntityOK(settingRepository.findByCode(key));
    }
}
