package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.enums.EntityType;
import com.nta.teabreakorder.enums.NotificationType;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.WarHouseItem;
import com.nta.teabreakorder.repository.NotificationRepository;
import com.nta.teabreakorder.repository.WarHouseItemRepository;
import com.nta.teabreakorder.repository.dao.WarningJobDao;
import com.nta.teabreakorder.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private WarningJobDao warningJobDao;

    @Autowired
    private WarHouseItemRepository warHouseItemRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void warhouseJob() throws Exception {
        List<WarHouseItem> allWarHouse = warHouseItemRepository.findAll();
        Notification notification;
        if (!allWarHouse.isEmpty()) {
            for (WarHouseItem warHouseItem : allWarHouse) {
                boolean lowQuantity = warHouseItem.getQuantity() <= 10;
                if (lowQuantity) {
                    if(!warningJobDao.isExitWarning(EntityType.WARHOUSE,warHouseItem.getId())){
                        notification = new Notification();
                        notification.setContent(warHouseItem.getProduct().getName() + " ( SL: " + warHouseItem.getQuantity() + " ) " + "sắp hết hàng. Vui lòng kiểm tra");
                        notification.setTimestamp(LocalDateTime.now());
                        notification.setType(NotificationType.warning);
                        notificationRepository.save(notification);
                        warningJobDao.createWarningStatus(EntityType.WARHOUSE, warHouseItem.getId());
                    }
                } else {
                    warningJobDao.deleteWarningStatus(EntityType.WARHOUSE, warHouseItem.getId());
                }
            }
        }
    }
}
