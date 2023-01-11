package com.nhnacademy.booklay.server.controller.admin.coupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    CouponCreateRequest couponCreateRequest;
    CouponDetailRetrieveResponse couponDetailRetrieveResponse;
    CouponUpdateRequest couponUpdateRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        couponCreateRequest = Dummy.getDummyCouponCreateRequest();

        couponDetailRetrieveResponse = CouponDetailRetrieveResponse.fromEntity(Dummy.getDummyCoupon());
        couponUpdateRequest = Dummy.getDummyCouponUpdateRequest();
    }

    @Test
    @DisplayName("retrieveAllCoupons() - 모든 쿠폰 조회 컨트롤러 테스트")
    void testRetrieveAllCoupons() throws Exception {

        // mocking
        PageRequest pageRequest = PageRequest.of(0,10);
        PageImpl<CouponRetrieveResponse> couponRetrieveResponses = new PageImpl<>(List.of(), pageRequest, 1);

        when(couponAdminService.retrieveAllCoupons(any())).thenReturn(couponRetrieveResponses);

        // then
        mockMvc.perform(get(URI_PREFIX + "/pages")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("createCoupon() - 쿠폰 생성 컨트롤러 테스트")
    void testCreateCoupon() throws Exception {

        // then
        mockMvc.perform(post(URI_PREFIX)
            .content(objectMapper.writeValueAsString(couponCreateRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("retrieveCouponDetail() - 쿠폰 단건 조회 컨트롤러 테스트")
    void testRetrieveCouponDetail() throws Exception {

        // mocking
        when(couponAdminService.retrieveCoupon(couponDetailRetrieveResponse.getId())).thenReturn(couponDetailRetrieveResponse);

        // then
        mockMvc.perform(get(URI_PREFIX + "/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

//    @Test
    @DisplayName("updateCoupon() - 쿠폰 수정 컨트롤러 테스트")
    void testUpdateCoupon() throws Exception {

        // then
        mockMvc.perform(put(URI_PREFIX + "/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    @Test
    @DisplayName("deleteCoupon() - 쿠폰 삭제 컨트롤러 테스트")
    void deleteCoupon() throws Exception {

        // then
        mockMvc.perform(delete(URI_PREFIX + "/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        BDDMockito.then(couponAdminService).should().deleteCoupon(1L);

    }
}