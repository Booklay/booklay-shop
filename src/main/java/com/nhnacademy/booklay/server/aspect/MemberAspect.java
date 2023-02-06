package com.nhnacademy.booklay.server.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

/**
 * 로그인한 멤버의 정보를 받아오기 위한 AOP.
 * Controller 클래스에서 메소드 파라미터에 MemberInfo를 넣으면 주입됩니다.
 *
 * @author 조현진
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberAspect {
    private final ObjectMapper objectMapper;

    /**
     * GetMapping 에 MemberInfo 추가
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && execution(* *.*(.., com.nhnacademy.booklay.server.dto.common.MemberInfo, ..))")
    public Object injectMemberToGetMapping(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        MemberInfo memberInfo = new MemberInfo(paramMap);
        Object[] args = Arrays.stream(pjp.getArgs())
                              .map(arg -> {
                                  if (arg instanceof MemberInfo) {
                                      arg = memberInfo;
                                  }
                                  return arg;
                              }).toArray();
        return pjp.proceed(args);
    }

    /**
     * PostMapping 에 MemberInfo 추가
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && execution(* *.*(.., com.nhnacademy.booklay.server.dto.common.MemberInfo, ..))")
    public Object injectMemberToPostMapping(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = getRequestWrapper(request);
        String requestBody = getRequestBody(requestWrapper);
        MemberInfo memberInfo = objectMapper.readValue(requestBody, MemberInfo.class);
        Object[] args = Arrays.stream(pjp.getArgs())
                .map(arg -> {
                    if (arg instanceof MemberInfo) {
                        arg = memberInfo;
                    }
                    return arg;
                }).toArray();
        return pjp.proceed(args);
    }
    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return "{}";
                }
            }
        }
        return "{}";
    }

    private ContentCachingRequestWrapper getRequestWrapper(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper)request;
        }
        return new ContentCachingRequestWrapper(request);
    }
}
