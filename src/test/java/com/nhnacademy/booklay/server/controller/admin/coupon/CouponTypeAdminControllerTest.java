package com.nhnacademy.booklay.server.controller.admin.coupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.service.coupon.CouponTypeService;
import java.util.List;
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

/**
 * 쿠폰 종류 등록, 조회, 수정, 삭제 컨트롤러 테스트.
 *
 * @author 김승혜
 */

@WebMvcTest(CouponTypeAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponTypeAdminControllerTest {

    @MockBean
    CouponTypeService couponTypeService;

    @Autowired
    CouponTypeAdminController couponTypeAdminController;

    @Autowired
    MockMvc mockMvc;

    private static final String URI_PREFIX = "/admin/couponTypes";
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 쿠폰 종류 조회 성공 테스트")
    void retrieveAllCouponTypes() throws Exception {

        // given
        PageRequest pageRequest = PageRequest.of(0,10);
        PageImpl<CouponTypeRetrieveResponse>
            couponTypePage = new PageImpl<>(List.of(), pageRequest, 1);

        // when
        when(couponTypeService.retrieveAllCouponTypes(any())).thenReturn(couponTypePage);

        // then
        mockMvc.perform(get(URI_PREFIX)
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponTypeService).retrieveAllCouponTypes(any());
    }

    @Test
    @DisplayName("쿠폰 종류 생성 성공 테스트")
    void createCouponType() throws Exception {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();

        // when

        // then
        mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(couponTypeRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponTypeService).createCouponType(any());
    }

    @Test
    void updateCouponType() throws Exception {

        // given
        CouponTypeCURequest couponRequest = Dummy.getDummyCouponTypeCURequest();

        // when

        // then
        mockMvc.perform(put(URI_PREFIX + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponTypeService, times(1)).updateCouponType(eq(1L), any());
    }

    @Test
    void deleteCouponType() throws Exception {

        // given

        // when

        // then
        mockMvc.perform(delete(URI_PREFIX + "/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(couponTypeService).deleteCouponType(1L);
    }
}