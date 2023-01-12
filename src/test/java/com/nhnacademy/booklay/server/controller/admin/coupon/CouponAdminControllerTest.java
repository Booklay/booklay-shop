package com.nhnacademy.booklay.server.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author 김승혜
 */

@WebMvcTest(CouponAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponAdminControllerTest {

    @MockBean
    CouponAdminService couponAdminService;

    @Autowired
    CouponAdminController couponAdminController;

    @Autowired
    MockMvc mockMvc;

    private static final String URI_PREFIX = "/admin/coupons";

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 쿠폰 조회 성공 테스트")
    void testRetrieveAllCoupons() throws Exception {

        // given
        PageRequest pageRequest = PageRequest.of(0,10);
        PageImpl<CouponRetrieveResponse> couponRetrieveResponses = new PageImpl<>(List.of(), pageRequest, 1);

        // when
        when(couponAdminService.retrieveAllCoupons(any())).thenReturn(couponRetrieveResponses);

        // then
        mockMvc.perform(get(URI_PREFIX + "/pages")
                .queryParam("page", "0")
                .queryParam("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService).retrieveAllCoupons(any());
    }

    @Test
    @DisplayName("쿠폰 생성 성공 테스트")
    void testCreateCoupon() throws Exception {

        // given
        CouponCreateRequest couponRequest = Dummy.getDummyCouponCreateRequest();

        // when

        // then
        mockMvc.perform(post(URI_PREFIX)
            .content(objectMapper.writeValueAsString(couponRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService).createCoupon(any());
    }

    @Test
    @DisplayName("쿠폰 단건 조회 성공 테스트")
    void testRetrieveCouponDetail() throws Exception {

        // given

        // when

        // then
        mockMvc.perform(get(URI_PREFIX + "/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService).retrieveCoupon(1L);
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰 조회 시 실패 테스트")
    void testRetrieveCouponDetailFail() throws Exception {

        // given

        // when
        when(couponAdminService.retrieveCoupon(1L)).thenThrow(NotFoundException.class);

        // then
        mockMvc.perform(get(URI_PREFIX + "/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService).retrieveCoupon(1L);
    }

    @Test
    @DisplayName("쿠폰 수정 성공 테스트")
    void testUpdateCoupon() throws Exception {

        // given
        CouponUpdateRequest couponRequest = Dummy.getDummyCouponUpdateRequest();

        // when


        // then
        mockMvc.perform(put(URI_PREFIX + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService, times(1)).updateCoupon(eq(1L), any());
    }

    @Test
    @DisplayName("deleteCoupon() - 쿠폰 삭제 컨트롤러 테스트")
    void deleteCoupon() throws Exception {

        // given

        // when

        // then
        mockMvc.perform(delete(URI_PREFIX + "/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponAdminService).deleteCoupon(1L);
    }
}