package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户管理服务
 */
@Service
public class Admin_UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private BalanceUsageRecordMapper balanceUsageRecordMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private UserFavoriteProductMapper userFavoriteProductMapper;

    @Autowired
    private ShipmentMapper shipmentMapper;

    /**
     * 分页查询用户(支持用户名或昵称搜索)
     */
    public IPage<User> getUserPage(Page<User> page, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or()
                    .like(User::getNickname, keyword));
        }
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询用户
     */
    public User getUserById(Long id) {
        return this.getById(id);
    }

    /**
     * 新增用户
     */
    public User addUser(User user) {
        this.save(user);
        return user;
    }

    /**
     * 更新用户
     */
    public User updateUser(User user) {
        this.updateById(user);
        return user;
    }

    /**
     * 删除用户(级联删除关联数据)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 1. 查询用户的所有订单
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId);
        List<Order> orders = orderMapper.selectList(orderWrapper);

        if (!orders.isEmpty()) {
            List<Long> orderIds = orders.stream().map(Order::getOrderId).collect(Collectors.toList());

            // 2. 删除订单项
            LambdaQueryWrapper<OrderItem> orderItemWrapper = new LambdaQueryWrapper<>();
            orderItemWrapper.in(OrderItem::getOrderId, orderIds);
            orderItemMapper.delete(orderItemWrapper);

            // 3. 删除订单
            orderMapper.deleteBatchIds(orderIds);
        }

        // 4. 删除地址
        LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper.eq(Address::getUserId, userId);
        addressMapper.delete(addressWrapper);

        // 5. 删除余额记录
        LambdaQueryWrapper<BalanceUsageRecord> balanceWrapper = new LambdaQueryWrapper<>();
        balanceWrapper.eq(BalanceUsageRecord::getUserId, userId);
        balanceUsageRecordMapper.delete(balanceWrapper);

        // 6. 删除购物车
        LambdaQueryWrapper<CartItem> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(CartItem::getUserId, userId);
        cartItemMapper.delete(cartWrapper);

        // 7. 删除评论
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, userId);
        commentMapper.delete(commentWrapper);

        // 8. 删除支付记录
        LambdaQueryWrapper<Payment> paymentWrapper = new LambdaQueryWrapper<>();
        paymentWrapper.eq(Payment::getUserId, userId);
        paymentMapper.delete(paymentWrapper);

        // 9. 删除收藏
        LambdaQueryWrapper<UserFavoriteProduct> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(UserFavoriteProduct::getUserId, userId);
        userFavoriteProductMapper.delete(favoriteWrapper);

        // 10. 删除发货记录
        LambdaQueryWrapper<Shipment> shipmentWrapper = new LambdaQueryWrapper<>();
        shipmentWrapper.eq(Shipment::getUserId, userId);
        shipmentMapper.delete(shipmentWrapper);

        // 11. 最后删除用户
        this.removeById(userId);
    }
}