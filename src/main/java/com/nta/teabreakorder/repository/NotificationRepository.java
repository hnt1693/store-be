package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Notification  n where n.id in ?1")
    void deletes(List<Long> ids);

    @Query(value = "select count(n) from Notification  n where n.isRead = false")
    long countAllByReadIsFalse();


    @Transactional
    @Modifying
    @Query(value = "update Notification  n set n.isRead = true where n.id in ?1")
    void markReadByIds(List<Long> ids);

}
