package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.model.auth.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NotificationDao implements CommonDao<Notification> {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Map<String, Object> get(Pageable pageable) {
        Map<String, Object> resMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;
        String name = null;
        String isRead = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select n from Notification n ");
        sql.append(" WHERE n.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND n.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(gr.groupName) like LOWER(CONCAT('%',:name,'%')) ");
            }
            isRead = searchMap.get("isRead");
            if (isRead != null) {
                sql.append(" AND n.isRead =:isRead ");
            }

        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(), "n");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "n.id "));

        TypedQuery<Notification> query = entityManager.createQuery(sql.toString(), Notification.class);

        if (pageable.getFields() != null) {
            filterList = DaoUtils.getFieldsFilter(pageable.getFields());
            pageable.setFieldList(filterList);
        }


        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (isRead != null) {
                query.setParameter("isRead", Boolean.parseBoolean(isRead));
            }
            if (name != null) {
                query.setParameter("name", name);
            }

        }

        long count = count(pageable);
        pageable.setTotal(count);
        pageable.setSearch(searchMap);
        pageable.setSort(sortMap);
        resMap.put("pagination", pageable);

        query.setFirstResult(pageable.getPage() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        List<Notification> objects = query.getResultList();

        resMap.put("data", objects);
        return resMap;
    }

    @Override
    public long count(Pageable pageable) {

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;
        String name = null;
        String isRead = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(n) from Notification n ");
        sql.append(" WHERE n.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND n.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(gr.groupName) like LOWER(CONCAT('%',:name,'%')) ");
            }

            isRead = searchMap.get("isRead");
            if (isRead != null) {
                sql.append(" AND n.isRead=:isRead");
            }

        }

        TypedQuery<Long> query = entityManager.createQuery(sql.toString(), Long.class);

        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (isRead != null) {
                query.setParameter("isRead", Boolean.parseBoolean(isRead));
            }
            if (name != null) {
                query.setParameter("name", name);
            }

        }

        return query.getSingleResult();
    }
}
