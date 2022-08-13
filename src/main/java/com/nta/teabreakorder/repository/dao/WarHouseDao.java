package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.enums.ProductType;
import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.model.WarHouseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WarHouseDao implements CommonDao<WarHouseItem> {

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
        String type = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select w from WarHouseItem w ");
        sql.append("JOIN  w.product p ");
        sql.append(" WHERE w.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND w.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(p.name) like LOWER(CONCAT('%',:name,'%')) ");
            }
            type = searchMap.get("type");
            if (type != null) {
                sql.append(" AND p.type = :type ");
            }

        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(), "u");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "p.name asc "));

        TypedQuery<WarHouseItem> query = entityManager.createQuery(sql.toString(), WarHouseItem.class);

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
            if (type != null) {
                query.setParameter("type", ProductType.valueOf(type));
            }
        }

        long count = count(pageable);
        pageable.setTotal(count);
        pageable.setSearch(searchMap);
        pageable.setSort(sortMap);
        resMap.put("pagination", pageable);

        query.setFirstResult(pageable.getPage()* pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        List<WarHouseItem> objects = query.getResultList();

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
        String type = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(*) from WarHouseItem w ");
        sql.append("JOIN  w.product p ");
        sql.append(" WHERE w.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND w.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(p.name) like LOWER(CONCAT('%',:name,'%')) ");
            }
            type = searchMap.get("type");
            if (type != null) {
                sql.append(" AND p.type = :type ");
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
            if (type != null) {
                query.setParameter("type", ProductType.valueOf(type));
            }
        }


        return query.getSingleResult();
    }
}
