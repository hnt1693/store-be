package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.ConfigHelper;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.OrderType;
import com.nta.teabreakorder.exception.ApiException;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.OrderDetail;
import com.nta.teabreakorder.model.WarHouseItem;
import com.nta.teabreakorder.repository.OrderDetailRepository;
import com.nta.teabreakorder.repository.OrderRepository;
import com.nta.teabreakorder.repository.WarHouseItemRepository;
import com.nta.teabreakorder.repository.common.SettingRepository;
import com.nta.teabreakorder.repository.dao.OrderDao;
import com.nta.teabreakorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private WarHouseItemRepository warHouseItemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ConfigHelper errorHelper;

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(orderDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return CommonUtil.createResponseEntityOK(orderRepository.getById(id));
    }

    @Override
    public ResponseEntity create(Order order) throws Exception {
        List<OrderDetail> orderDetailList = order.getOrderDetailList().stream().filter(ob -> ob.getProduct() != null).collect(Collectors.toList());
        List<WarHouseItem> warHouseItems = new ArrayList<>();
        WarHouseItem warHouseItem = null;
        int total = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            if (OrderType.XUAT.equals(order.getOrderType())) {
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), orderDetail.getQuantity());
                if (null != warHouseItem) {
                    warHouseItem.setQuantity(warHouseItem.getQuantity() - orderDetail.getQuantity());
                    warHouseItems.add(warHouseItem);
                    total += orderDetail.getQuantity() * warHouseItem.getProduct().getPrice();
                } else {
                    throw new ApiException(errorHelper.getValue("ORDER_ORDERDETAIL_C_QUANTITY"));
                }
            } else {
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), -1);

                if (null != warHouseItem) {
                    warHouseItem.setQuantity(warHouseItem.getQuantity() + orderDetail.getQuantity());
                    warHouseItem.addNewVersionTransaction();
                    warHouseItem.addTransaction();
                    warHouseItems.add(warHouseItem);
                    total += orderDetail.getQuantity() * warHouseItem.getProduct().getBasePrice();
                } else {
                    throw new ApiException(errorHelper.getValue("ORDER_ORDERDETAIL_C_QUANTITY"));
                }
            }
        }

        warHouseItemRepository.saveAll(warHouseItems);
        order.setOrderDetailList(orderDetailList);
        order.setTotal(total);
        return CommonUtil.createResponseEntityOK(orderRepository.save(order));
    }

    @Override
    public ResponseEntity update(Order order) throws Exception {

        List<OrderDetail> orderDetailList = order.getOrderDetailList().stream().filter(ob -> ob.getProduct() != null).collect(Collectors.toList());
        List<WarHouseItem> warHouseItems = new ArrayList<>();
        WarHouseItem warHouseItem = null;
        OrderDetail orderDetailDB = null;
        int total = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            if (OrderType.XUAT.equals(order.getOrderType())) {
                orderDetailDB = orderDetailRepository.findById(orderDetail.getId()).get();
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), orderDetail.getQuantity() - orderDetailDB.getQuantity());
                if (null != warHouseItem) {
                    warHouseItem.setQuantity(warHouseItem.getQuantity() - orderDetail.getQuantity() + orderDetailDB.getQuantity());
                    warHouseItems.add(warHouseItem);
                    total += orderDetail.getQuantity() * warHouseItem.getProduct().getPrice();
                } else {
                    throw new ApiException(errorHelper.getValue("ORDER_ORDERDETAIL_U_QUANTITY"));
                }
            } else {
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), -1);
                orderDetailDB = orderDetailRepository.findById(orderDetail.getId()).get();
                if (null != warHouseItem) {
                    warHouseItem.setQuantity(warHouseItem.getQuantity() + orderDetail.getQuantity() - orderDetailDB.getQuantity());
                    warHouseItem.updateTransaction(warHouseItem.getVersion(), warHouseItem);
                    warHouseItems.add(warHouseItem);
                    total += orderDetail.getQuantity() * warHouseItem.getProduct().getBasePrice();
                } else {
                    throw new ApiException(errorHelper.getValue("ORDER_ORDERDETAIL_U_QUANTITY"));
                }
            }
        }

        warHouseItemRepository.saveAll(warHouseItems);
        order.setOrderDetailList(orderDetailList);
        order.setTotal(total);
        order = orderRepository.save(order);
        setTimeSummaryJob(orderRepository.getCreatedAtBy(order.getId()));
        return CommonUtil.createResponseEntityOK(order);
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        List<Order> orderList = orderRepository.findAllById(ids);
        if(!orderList.isEmpty()){
            for (Order order : orderList) {
                rollbackQuantityWarHouseByOrder(order);
            }
            orderList.sort(Comparator.comparing(AuditingModel::getCreatedAt));
            LocalDateTime earliest = orderList.get(0).getCreatedAt();
            orderRepository.deleteAll(orderList);
            setTimeSummaryJob(earliest);
        }
        return CommonUtil.createResponseEntityOK(1);
    }

    private void rollbackQuantityWarHouseByOrder(Order order) throws Exception {
        List<WarHouseItem> warHouseItemList = new ArrayList<>();
        WarHouseItem warHouseItem = null;
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            if (OrderType.XUAT.equals(order.getOrderType())) {
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), -1);
                if(warHouseItem!=null){
                    warHouseItem.setQuantity(warHouseItem.getQuantity() + orderDetail.getQuantity());
                    warHouseItemList.add(warHouseItem);
                }
            } else {
                warHouseItem = warHouseItemRepository.getByProductAndQuantityGreaterThanEqual(orderDetail.getProduct(), -1);
                if (warHouseItem!=null && warHouseItem.getQuantity() < orderDetail.getQuantity()) {
                    throw new ApiException(errorHelper.getValue("ORDER_ORDERDETAIL_D_QUANTITY"));
                }
                warHouseItem.setQuantity(warHouseItem.getQuantity() - orderDetail.getQuantity());
                warHouseItemList.add(warHouseItem);
            }
        }
        warHouseItemRepository.saveAll(warHouseItemList);
    }


    private void setTimeSummaryJob(LocalDateTime createdDate) {
        DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        settingRepository.updateValueByKey("last_time_summary_job", createdDate.format(FORMAT_DATE));
    }
}
