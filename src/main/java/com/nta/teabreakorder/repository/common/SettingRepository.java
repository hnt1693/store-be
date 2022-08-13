package com.nta.teabreakorder.repository.common;

import com.nta.teabreakorder.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Settings, Long> {
    @Query(value = "DELETE FROM Settings s where s.id in ?1")
    @Modifying
    @Transactional
    void deletes(List<Long> ids);

    Settings findByCode(String key);

    @Query(value = "update Settings s set s.value=?2 where s.code=?1")
    @Transactional
    @Modifying
    void updateValueByKey(String key, String value);
}
