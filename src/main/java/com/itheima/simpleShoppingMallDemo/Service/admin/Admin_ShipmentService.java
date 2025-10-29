package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import com.itheima.simpleShoppingMallDemo.ModelVO.Admin_ShipmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台发货管理服务
 */
@Service
public class Admin_ShipmentService extends ServiceImpl<ShipmentMapper, Shipment> {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    /**
     * 分页查询发货记录(包含商品、订单、支付信息)
     */
    public IPage<Admin_ShipmentVO> getShipmentPage(Page<Shipment> page, Long userId, Long orderId,
                                                   Integer shipmentStatus, Integer paymentStatus) {
        LambdaQueryWrapper<Shipment> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(Shipment::getUserId, userId);
        }
        if (orderId != null) {
            wrapper.eq(Shipment::getOrderId, orderId);
        }
        if (shipmentStatus != null) {
            wrapper.eq(Shipment::getShipmentStatus, shipmentStatus);
        }

        // 如果需要按支付状态筛选,应该关联Order表而不是Payment表
        if (paymentStatus != null) {
            // 先查询符合支付状态的订单ID列表
            LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(Order::getStatus, paymentStatus);  // 假设Order表有paymentStatus字段
            orderWrapper.select(Order::getOrderId);
            List<Order> orders = orderMapper.selectList(orderWrapper);

            if (!orders.isEmpty()) {
                List<Long> orderIds = orders.stream()
                        .map(Order::getOrderId)
                        .collect(Collectors.toList());
                wrapper.in(Shipment::getOrderId, orderIds);
            } else {
                // 如果没有符合条件的订单,返回空结果
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
        }

        wrapper.orderByDesc(Shipment::getCreatedAt);

        IPage<Shipment> shipmentPage = this.page(page, wrapper);

        // 转换为VO并填充关联信息
        IPage<Admin_ShipmentVO> voPage = shipmentPage.convert(shipment -> {
            Admin_ShipmentVO vo = new Admin_ShipmentVO();
            BeanUtils.copyProperties(shipment, vo);
            fillShipmentDetails(vo);
            return vo;
        });

        return voPage;
    }

    /**
     * 根据ID查询发货记录详情(关联用户ID)
     */
    public Admin_ShipmentVO getShipmentById(Long id) {
        Shipment shipment = this.getById(id);
        if (shipment == null) {
            return null;
        }

        Admin_ShipmentVO vo = new Admin_ShipmentVO();
        BeanUtils.copyProperties(shipment, vo);
        fillShipmentDetails(vo);
        return vo;
    }

    /**
     * 填充发货详细信息
     */
    private void fillShipmentDetails(Admin_ShipmentVO vo) {
        // 填充商品信息
        if (vo.getProductId() != null) {
            Product product = productMapper.selectById(vo.getProductId());
            vo.setProduct(product);
        }

        // 填充订单信息
        if (vo.getOrderId() != null) {
            Order order = orderMapper.selectById(vo.getOrderId());
            vo.setOrder(order);
        }

        // 填充支付信息
        if (vo.getPaymentId() != null) {
            Payment payment = paymentMapper.selectById(vo.getPaymentId());
            vo.setPayment(payment);

            // 设置支付状态文本
            if (payment != null) {
                vo.setPaymentStatusText(getPaymentStatusText(payment.getStatus().intValue()));
            }
        } else {
            vo.setPaymentStatusText("未支付");
        }

        // 填充用户信息
        if (vo.getUserId() != null) {
            User user = userMapper.selectById(vo.getUserId());
            vo.setUser(user);
        }

        // 填充地址信息
        if (vo.getAddressId() != null) {
            Address address = addressMapper.selectById(vo.getAddressId());
            vo.setAddress(address);
        }

        // 设置发货状态文本
        vo.setShipmentStatusText(getShipmentStatusText(vo.getShipmentStatus()));
    }

    /**
     * 获取支付状态文本
     */
    private String getPaymentStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "未支付";
            case 1: return "已支付";
            case 2: return "已退款";
            default: return "未知";
        }
    }

    /**
     * 获取发货状态文本
     */
    private String getShipmentStatusText(Long status) {
        if (status == null) return "未知";
        switch (status.intValue()) {
            case 0: return "待发货";
            case 1: return "已发货";
            case 2: return "已签收";
            default: return "未知";
        }
    }

    /**
     * 更新发货记录
     */
    public Shipment updateShipment(Shipment shipment) {
        this.updateById(shipment);
        return shipment;
    }

    /**
     * 删除发货记录
     */
    public void deleteShipment(Long id) {
        this.removeById(id);
    }

    /**
     * 发货操作(传入物流单号，状态改为已发货)
     */
    @Transactional(rollbackFor = Exception.class)
    public Shipment shipOrder(Long shipmentId, String trackingNumber) {
        Shipment shipment = this.getById(shipmentId);
        if (shipment == null) {
            throw new RuntimeException("发货记录不存在");
        }

        // 检查是否已支付
        if (shipment.getPaymentId() != null) {
            Payment payment = paymentMapper.selectById(shipment.getPaymentId());
            if (payment == null || payment.getStatus() != 1) {
                throw new RuntimeException("订单未支付，无法发货");
            }
        }

        // 检查当前状态
        if (shipment.getShipmentStatus() != null && shipment.getShipmentStatus() != 0) {
            throw new RuntimeException("当前状态不允许发货操作");
        }

        // 更新发货信息
        shipment.setShipmentStatus(1L); // 已发货
        shipment.setTrackingNumber(trackingNumber);
        shipment.setShippedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        this.updateById(shipment);
        return shipment;
    }

    /**
     * 签收操作(状态改为已签收)
     */
    @Transactional(rollbackFor = Exception.class)
    public Shipment deliverOrder(Long shipmentId) {
        Shipment shipment = this.getById(shipmentId);
        if (shipment == null) {
            throw new RuntimeException("发货记录不存在");
        }

        // 检查当前状态
        if (shipment.getShipmentStatus() == null || shipment.getShipmentStatus() != 1) {
            throw new RuntimeException("订单未发货，无法签收");
        }

        // 更新签收信息
        shipment.setShipmentStatus(2L); // 已签收
        shipment.setDeliveredAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        this.updateById(shipment);

        // 可以考虑同步更新订单状态为已完成
        if (shipment.getOrderId() != null) {
            Order order = orderMapper.selectById(shipment.getOrderId());
            if (order != null && order.getStatus() != 3) {
                order.setStatus(3L); // 已完成
                orderMapper.updateById(order);
            }
        }

        return shipment;
    }
}