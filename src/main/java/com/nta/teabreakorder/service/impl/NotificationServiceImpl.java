package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.repository.NotificationRepository;
import com.nta.teabreakorder.repository.dao.NotificationDao;
import com.nta.teabreakorder.service.CommonService;
import com.nta.teabreakorder.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(notificationDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return CommonUtil.createResponseEntityOK(notificationRepository.findById(id));
    }

    @Override
    public ResponseEntity create(Notification notification) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity update(Notification notification) throws Exception {
        return CommonUtil.createResponseEntityOK(notificationRepository.save(notification));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        notificationRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }

    @Override
    public ResponseEntity countUnread() {
        return CommonUtil.createResponseEntityOK(notificationRepository.countAllByReadIsFalse());
    }

    @Override
    public ResponseEntity markReadByIds(List<Long> ids) {
        notificationRepository.markReadByIds(ids);
        return CommonUtil.createResponseEntityOK(1);
    }
}
