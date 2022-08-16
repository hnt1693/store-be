package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.enums.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class WarningJobDao {

    @Autowired
    private EntityManager entityManager;


    public boolean isExitWarning(EntityType entityType, Long entityId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT s.id FROM notify_status s ");
        sql.append(" WHERE s.entity_type=? ");
        sql.append(" AND s.entity_id =? ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, entityType.toString());
        query.setParameter(2, Integer.valueOf(entityId.toString()));
        return query.getResultList() != null && query.getResultList().size()>0;
    }


    public void deleteWarningStatus(EntityType entityType, Long entityId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM notify_status s ");
        sql.append(" WHERE s.entity_type=? ");
        sql.append(" AND s.entity_id = ? ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, entityType.toString());
        query.setParameter(2, entityId);
        query.executeUpdate();
    }


    public void createWarningStatus(EntityType entityType, Long entityId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO notify_status(entity_type,entity_id) VALUES (?,?) ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, entityType.toString());
        query.setParameter(2, entityId);
        query.executeUpdate();
    }
}
