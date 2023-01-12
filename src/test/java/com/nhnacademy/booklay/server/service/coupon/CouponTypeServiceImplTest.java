package com.nhnacademy.booklay.server.service.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author 김승혜
 */

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class CouponTypeServiceImplTest {

    @InjectMocks
    CouponTypeServiceImpl couponTypeService;

    @Mock
    CouponTypeRepository couponTypeRepository;

    @Test
    @DisplayName("createCouponType() - 쿠폰 생성 서비스 테스트")
    void testCreateCouponType() {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();

        // when
        couponTypeService.createCouponType(couponTypeRequest);

        // then
        BDDMockito.then(couponTypeRepository).should().save(any());
    }

    @Test
    @DisplayName("retrieveAllCouponTypes() - 모든 쿠폰 종류 조회 서비스 테스트")
    void retrieveAllCouponTypes() {

        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(couponTypeRepository.findAllBy(pageRequest)).willReturn(Page.empty());


        // when
        couponTypeService.retrieveAllCouponTypes(pageRequest);

        // then
        BDDMockito.then(couponTypeRepository).should().findAllBy(pageRequest);
    }

    @Test
    @DisplayName("updateCouponType() - 쿠폰 정보 수정 서비스 테스트")
    void updateCouponType() {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();
        given(couponTypeRepository.existsById(couponTypeRequest.getId())).willReturn(true);

        // when
        couponTypeService.updateCouponType(couponTypeRequest.getId(), couponTypeRequest);

        // then
        BDDMockito.then(couponTypeRepository).should().existsById(couponTypeRequest.getId());
        BDDMockito.then(couponTypeRepository).should().save(any());
    }

    @Test
    @DisplayName("updateCouponType() - 쿠폰 정보 수정 시, 유효하지 않은 couponTypeId 테스트")
    void updateCouponTypeInvalidCouponTypeId() {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();
        given(couponTypeRepository.existsById(couponTypeRequest.getId())).willReturn(false);

        // when

        // then
        assertThatThrownBy(() -> couponTypeService.updateCouponType(couponTypeRequest.getId(), couponTypeRequest))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(String.valueOf(couponTypeRequest.getId()));

        BDDMockito.then(couponTypeRepository).should().existsById(couponTypeRequest.getId());
    }

    @Test
    @DisplayName("deleteCouponType() - 쿠폰 삭제 서비스 테스트.")
    void deleteCouponType() {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();
        given(couponTypeRepository.existsById(couponTypeRequest.getId())).willReturn(true);

        // when
        couponTypeService.deleteCouponType(couponTypeRequest.getId());

        // then
        BDDMockito.then(couponTypeRepository).should().deleteById(couponTypeRequest.getId());
    }

    @Test
    @DisplayName("deleteCouponType() - 쿠폰 삭제 시, 유효하지 않은 couponTypeId 테스트")
    void deleteCouponTypeInvalidCouponTypeId() {

        // given
        CouponTypeCURequest couponTypeRequest = Dummy.getDummyCouponTypeCURequest();
        given(couponTypeRepository.existsById(couponTypeRequest.getId())).willReturn(false);

        // when

        // then
        assertThatThrownBy(() -> couponTypeService.deleteCouponType(couponTypeRequest.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(String.valueOf(couponTypeRequest.getId()));

        BDDMockito.then(couponTypeRepository).should().existsById(couponTypeRequest.getId());
    }
}