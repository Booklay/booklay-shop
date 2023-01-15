package com.nhnacademy.booklay.server.batch.coupon;

import com.nhnacademy.booklay.server.service.coupon.CouponBirthdaySettingService;
import com.nhnacademy.booklay.server.service.coupon.CouponService;
import com.nhnacademy.booklay.server.service.coupon.CouponTemplateService;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BirthdayCoupon {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job birthdayCouponJob(
        @Qualifier("birthdayCouponStep") Step step){
        return jobBuilderFactory.get("birthdayCouponJob")
            .start(step)
            .build();
    }

    @Bean
    public Step birthdayCouponStep(BirthdayCouponTasklet birthdayCouponTasklet){
        return stepBuilderFactory.get("birthdayCouponStep")
            .tasklet(birthdayCouponTasklet)
            .build();
    }
    @Bean
    public BirthdayCouponTasklet birthdayCouponTasklet(CouponTemplateService couponTemplateService,
                                                       CouponService couponService,
                                                       CouponBirthdaySettingService couponBirthdaySettingService,
                                                       MemberService memberService){
        return new BirthdayCouponTasklet(couponTemplateService, couponService, couponBirthdaySettingService, memberService);
    }
}
