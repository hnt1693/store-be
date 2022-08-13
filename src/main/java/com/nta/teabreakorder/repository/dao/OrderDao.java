package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.enums.OrderType;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao implements CommonDao<Order> {
    @Autowired
    private EntityManager entityManager;

    final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        String fromDate = null;
        String toDate = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select o from Order o ");
        sql.append("LEFT JOIN o.customer c ");
        sql.append(" WHERE o.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) ");
            }
            type = searchMap.get("type");
            if (type != null) {
                sql.append(" AND o.orderType = :type ");
            }
            fromDate = searchMap.get("fromDate");
            if (fromDate != null) {
                sql.append(" AND o.createdAt >= :fromDate ");
            }
            toDate = searchMap.get("toDate");
            if (toDate != null) {
                sql.append(" AND o.createdAt <= :toDate ");
            }
        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(), "u");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "o.createdAt desc "));

        TypedQuery<Order> query = entityManager.createQuery(sql.toString(), Order.class);

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
                query.setParameter("type", OrderType.valueOf(type));
            }
            if (fromDate != null) {
                LocalDateTime frDateObject = LocalDateTime.parse(fromDate + " 00:00:00", DATE_FORMAT);
                query.setParameter("fromDate", frDateObject);
            }
            if (toDate != null) {
                LocalDateTime todayObject = LocalDateTime.parse(toDate + " 23:59:59", DATE_FORMAT);
                query.setParameter("toDate", todayObject);
            }

        }

        long count = count(pageable);
        pageable.setTotal(count);
        pageable.setSearch(searchMap);
        pageable.setSort(sortMap);
        resMap.put("pagination", pageable);

        query.setFirstResult(pageable.getPage() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        List<Order> objects = query.getResultList();

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
        String type = null;
        String fromDate = null;
        String toDate = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(o) from Order o ");
        sql.append(" LEFT JOIN o.customer c ");
        sql.append(" WHERE o.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id ");
            }
            name = searchMap.get("name");
            if (name != null) {
                sql.append(" AND LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) ");
            }
            type = searchMap.get("type");
            if (type != null) {
                sql.append(" AND o.orderType = :type ");
            }
            fromDate = searchMap.get("fromDate");
            if (fromDate != null) {
                sql.append(" AND o.createdAt >= :fromDate ");
            }
            toDate = searchMap.get("toDate");
            if (toDate != null) {
                sql.append(" AND o.createdAt <= :toDate ");
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
                query.setParameter("type", OrderType.valueOf(type));
            }
            if (fromDate != null) {
                LocalDateTime frDateObject = LocalDateTime.parse(fromDate + " 00:00:00", DATE_FORMAT);
                query.setParameter("fromDate", frDateObject);
            }
            if (toDate != null) {
                LocalDateTime todayObject = LocalDateTime.parse(toDate + " 23:59:59", DATE_FORMAT);
                query.setParameter("toDate", todayObject);
            }
        }

        return query.getSingleResult();
    }
}
