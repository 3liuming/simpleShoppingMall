package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartBuyDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndCartDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndProductDto;
import com.itheima.simpleShoppingMallDemo.Service.AddressService;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buy")
public class BuyController {
    @Autowired
    BuyService buyService;
    @Autowired
    AddressService addressService;

    @GetMapping("/list")
    public Result<UserAddressAndProductDto> selUserProductByUidAndPid(@RequestParam("productId") Long productId,
                                                                      HttpServletRequest request){
        return buyService.selUserProductByUidAndPid((Long)request.getAttribute("userId"),productId);
    }

    /**
     * 获取购物车结算页面数据（地址列表 + 购物车商品列表）
     */
    @GetMapping("/cart-list")
    public Result<UserAddressAndCartDto> getCartListWithAddress(@RequestParam("cartItemIds") String cartItemIds,
                                                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        return buyService.getCartListWithAddress(userId, cartItemIds);
    }

    @PostMapping("/create")
    public Result<Boolean> createOrderByUsernameAndQuantityAndPid(@RequestBody CartBuyDto cartBuyDto,
                                                                  HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        if(buyService.createOrderByUsernameAndQuantityAndPid(userId, cartBuyDto) != null){
            return Result.success(true);
        }else return Result.fail("创建订单失败") ;
    }

    @PostMapping("/nowbuy")
    public Result<Boolean> CreatePaymentByUserNameAndQuantityAndPid(@RequestBody CartBuyDto cartBuyDto,
                                                                    HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        return buyService.createPaymentByUsernameAndQuantityAndPid(userId, cartBuyDto);
    }

    /**
     * 添加地址
     */
    @PostMapping("/add")
    public Result addAddress(@Validated @RequestBody Address dto,
                             HttpServletRequest request) {
        // 从 session 获取当前用户ID
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        Address address = new Address();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);

        boolean success = addressService.addAddress(address);
        if (success) {
            return Result.success( address);
        }
        return Result.fail("地址添加失败");
    }

    /**
     * 修改地址
     */
    @PostMapping("/update")
    public Result updateAddress(@Validated @RequestBody Address dto,
                                HttpServletRequest request) {
        // 从 session 获取当前用户ID
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        Address address = new Address();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);

        boolean success = addressService.updateAddress(address);
        if (success) {
            return Result.success("地址修改成功");
        }
        return Result.fail("地址修改失败或不存在");
    }

    /**
     * 删除地址（逻辑删除）
     */
    @PostMapping("/delete")
    public Result deleteAddress(@Validated @RequestBody Address dto,
                                HttpServletRequest request) {
        // 从 session 获取当前用户ID
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail("请先登录");
        }

        boolean success = addressService.deleteAddress(dto.getAddressId(), userId);
        if (success) {
            return Result.success("地址删除成功");
        }
        return Result.fail("地址删除失败或不存在");
    }

}
