package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Customer;
import com.nta.teabreakorder.model.auth.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao implements CommonDao<Customer> {
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

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select c from Customer c ");
        sql.append(" WHERE c.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND c.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) ");
            }

        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(), "c");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "c.id "));

        TypedQuery<Customer> query = entityManager.createQuery(sql.toString(), Customer.class);

        if (pageable.getFields() != null) {
            filterList = DaoUtils.getFieldsFilter(pageable.getFields());
            pageable.setFieldList(filterList);
        }


        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
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

        query.setFirstResult(pageable.getPage()* pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        List<Customer> objects = query.getResultList();

        resMap.put("data", objects);
        return resMap;
    }

    @Override
    public long count(Pageable pageable) {
        Map<String, Object> resMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;
        String name = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(*) from Customer c ");
        sql.append(" WHERE c.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND c.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) ");
            }
        }

        TypedQuery<Long> query = entityManager.createQuery(sql.toString(), Long.class);

        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (name != null) {
                query.setParameter("name", name);
            }

        }

        return query.getSingleResult();
    }
}
