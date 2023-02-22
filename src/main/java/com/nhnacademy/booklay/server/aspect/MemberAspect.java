package com.nhnacademy.booklay.server.aspect;

import com.nhnacademy.booklay.server.aspect.jwt.TokenUtils;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 로그인한 멤버의 정보를 받아오기 위한 AOP.
 * Controller 클래스에서 메소드 파라미터에 MemberInfo를 넣으면 주입됩니다.
 *
 * @author 조현진
 */
@Slf4j
@Aspect
@Order(value = 100)
@Component
@RequiredArgsConstructor
public class MemberAspect {
    private final TokenUtils tokenUtils;
    private final MemberService memberService;
    /**
     * Member 정보를 주입합니다.
     *
     * @param pjp 메소드 원본의 정보를 갖는 객체입니다.
     * @return 메소드 정보
     * @throws Throwable 메소드 실행 시 발생할 수 있는 예외
     */

    @Around("@within(restController) && execution(* *.*(.., com.nhnacademy.booklay.server.dto.common.MemberInfo, ..))")
    public Object injectMember(ProceedingJoinPoint pjp, RestController restController) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String email = tokenUtils.getEmail(request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()));
        MemberRetrieveResponse memberRetrieveResponse =  memberService.retrieveMemberInfoByEmail(email).orElse(null);

        MemberInfo memberInfo = memberRetrieveResponse == null?new MemberInfo():new MemberInfo(memberRetrieveResponse);

        Object[] args = Arrays.stream(pjp.getArgs()).map(arg -> {
            if (arg instanceof MemberInfo) {
                arg = memberInfo;
            }
            return arg;
        }).toArray();


        return pjp.proceed(args);
    }

}
