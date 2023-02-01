package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage/product")
public class MyPageProductController {

    private final WishlistService wishlistService;

    // 위시리스트 등록 삭제
    @PostMapping("/wishlist")
    public void createWishlist(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistService.createWishlist(request);
    }

    @DeleteMapping("/wishlist")
    public void deleteWishlist(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistService.deleteWishlist(request);
    }

    //재입고 알림 등록 삭제
    @PostMapping("/alarm")
    public void createAlarm(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistService.createWishlist(request);
    }

    @DeleteMapping("/alarm")
    public void deleteAlarm(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistService.deleteWishlist(request);
    }
}
