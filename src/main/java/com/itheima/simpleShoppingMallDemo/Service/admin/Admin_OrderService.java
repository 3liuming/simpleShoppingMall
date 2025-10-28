package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderItemMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.ModelDto.Admin_OrderItemVO;
import com.itheima.simpleShoppingMallDemo.ModelDto.Admin_OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台订单管理服务
 */
@Service
public class Admin_OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 分页查询订单(包含订单项和商品信息)
     */
    public IPage<Admin_OrderVO> getOrderPage(Page<Order> page, Long userId, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> orderPage = this.page(page, wrapper);

        // 转换为VO并填充订单项和商品信息
        IPage<Admin_OrderVO> voPage = orderPage.convert(order -> {
            Admin_OrderVO vo = new Admin_OrderVO();
            BeanUtils.copyProperties(order, vo);
            vo.setOrderItems(getOrderItemsWithProduct(order.getOrderId()));
            return vo;
        });

        return voPage;
    }

    /**
     * 根据ID查询订单详情
     */
    public Admin_OrderVO getOrderById(Long id) {
        Order order = this.getById(id);
        if (order == null) {
            return null;
        }

        Admin_OrderVO vo = new Admin_OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderItems(getOrderItemsWithProduct(id));
        return vo;
    }

    /**
     * 获取订单项列表(包含商品信息)
     */
    private List<Admin_OrderItemVO> getOrderItemsWithProduct(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);

        return orderItems.stream().map(item -> {
            Admin_OrderItemVO vo = new Admin_OrderItemVO();
            BeanUtils.copyProperties(item, vo);
            Product product = productMapper.selectById(item.getProductId());
            vo.setProduct(product);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增订单
     */
    public Order addOrder(Order order) {
        this.save(order);
        return order;
    }

    /**
     * 更新订单
     */
    public Order updateOrder(Order order) {
        this.updateById(order);
        return order;
    }

    /**
     * 删除订单(级联删除订单项)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long orderId) {
        // 删除订单项
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        orderItemMapper.delete(wrapper);

        // 删除订单
        this.removeById(orderId);
    }
}