package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.entity.CouponBirthdaySetting;
import com.nhnacademy.booklay.server.repository.coupon.CouponBirthdaySettingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponBirthdaySettingServiceImpl implements CouponBirthdaySettingService {
    private final CouponBirthdaySettingRepository couponBirthdaySettingRepository;
    @Override
    public List<CouponBirthdaySetting> retrieveAllBirthdaySetting() {
        return couponBirthdaySettingRepository.findAll();
    }
}
