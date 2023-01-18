package com.nhnacademy.booklay.server.batch.coupon;

import com.nhnacademy.booklay.server.dto.member.BirthdayMemberDto;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import com.nhnacademy.booklay.server.service.coupon.CouponBirthdaySettingService;
import com.nhnacademy.booklay.server.service.coupon.CouponComplexService;
import com.nhnacademy.booklay.server.service.coupon.CouponTemplateService;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
@Slf4j
public class BirthdayCouponTasklet implements Tasklet, StepExecutionListener {

    private final CouponTemplateService couponTemplateService;
    private final CouponComplexService couponService;
    private final CouponBirthdaySettingService couponBirthdaySettingService;
    private final MemberService memberService;

    private Map<Long, Long> birthdaySettings = new HashMap<>();


    @Override
    public void beforeStep(StepExecution stepExecution) {
        couponBirthdaySettingService.retrieveAllBirthdaySetting().stream().
            forEach(couponBirthdaySetting ->
                birthdaySettings.put(couponBirthdaySetting.getMemberGrade(), couponBirthdaySetting.getCouponTemplateNo()));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
        throws Exception {
        //todo memberService 에 생일인 멤버찾기 기능추가
//        List<BirthdayMemberDto> birthdayMemberDtoList = memberService.retrieveBirthdayMembers();
        List<BirthdayMemberDto> birthdayMemberDtoList = new ArrayList<>();
        for (BirthdayMemberDto birthdayMemberDto : birthdayMemberDtoList) {

            CouponTemplate couponTemplate =
                couponTemplateService.retrieveCouponTemplate(birthdaySettings.get(birthdayMemberDto.getGradeNo()));
            couponService.createAndIssueCouponByTemplate(couponTemplate, birthdayMemberDto.getMemberNo());

        }
        return RepeatStatus.FINISHED;
    }


}
