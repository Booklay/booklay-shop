package com.nhnacademy.booklay.server.controller.delivery;

import com.nhnacademy.booklay.server.dto.ErrorResponse;
import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationLimitExceededException;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationNotFoundException;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDestinationService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 양승아
 */
@Slf4j
@RestController
@RequestMapping("/delivery/destination")
@RequiredArgsConstructor
public class DeliveryDestinationController {
    private static final String DELIVERY_DESTINATION_NOT_FOUND_ERROR_CODE =
        "DeliveryDestinationNotFound";
    private static final String DELIVERY_DESTINATION_LIMIT_EXCEEDED_ERROR_CODE =
        "DeliveryDestinationLimitExceeded";

    private final DeliveryDestinationService deliveryDestinationService;

    /**
     * 배송지 추가 메소드
     *
     * @param requestDto 추가할 배송지 정보
     * @param memberNo   배송지 추가할 회원
     * @return void
     */
    @PostMapping("/{memberNo}")
    public ResponseEntity<Void> createDeliveryDestination(@Valid @RequestBody
                                                          DeliveryDestinationCURequest requestDto,
                                                          @PathVariable Long memberNo) {
        deliveryDestinationService.createDeliveryDestination(memberNo,
            requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    /**
     * 배송지 리스트 조회 메소드
     *
     * @param memberNo 조회할 배송지를 가진 회원
     * @return List<DeliveryDestinationRetrieveResponse> 배송지 리스트
     */
    @GetMapping("/list/{memberNo}")
    public ResponseEntity<List<DeliveryDestinationRetrieveResponse>> retrieveDeliveryDestinations(
        @PathVariable Long memberNo) {

        List<DeliveryDestinationRetrieveResponse> response =
            deliveryDestinationService.retrieveDeliveryDestinations(memberNo);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    /**
     * 배송지 단건 조회 메소드
     *
     * @param addressNo 조회할 배송지 번호
     * @return DeliveryDestinationRetrieveResponse
     */
    @GetMapping("/{addressNo}")
    public ResponseEntity<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestination(
        @PathVariable Long addressNo) {
        DeliveryDestinationRetrieveResponse response =
            deliveryDestinationService.retrieveDeliveryDestination(addressNo);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    /**
     * 배송지 수정 메소드
     *
     * @param requestDto 수정할 정보가 있는 dto
     * @param memberNo   수정할 배송지를 가진 회원
     * @param addressNo  수정할 배송지 번호
     * @return HttpStatus.OK
     */
    @PostMapping("/update/{memberNo}/{addressNo}")
    public ResponseEntity<Void> updateDeliveryDestination(
        @Valid @RequestBody DeliveryDestinationCURequest requestDto,
        @PathVariable Long memberNo,
        @PathVariable Long addressNo) {
        deliveryDestinationService.updateDeliveryDestination(memberNo, addressNo, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    /**
     * 배송지 삭제 메소드
     *
     * @param memberNo  삭제할 배송지를 가진 회원 번호
     * @param addressNo 삭제할 배송지 번호
     * @return HttpStatus.OK/void
     */
    @DeleteMapping("/{memberNo}/{addressNo}")
    public ResponseEntity<Void> deleteDeliveryDestination(@PathVariable Long memberNo,
                                                          @PathVariable Long addressNo) {
        deliveryDestinationService.deleteDeliveryDestination(memberNo, addressNo);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    /**
     * 배송지 찾을 수 없음 exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DeliveryDestinationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryDestinationNotFoundException(
        DeliveryDestinationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .code(DELIVERY_DESTINATION_NOT_FOUND_ERROR_CODE)
                .message(ex.getMessage()).build());
    }

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(DeliveryDestinationLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryDestinationLimitExceededException(
        DeliveryDestinationLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().code(
                    DELIVERY_DESTINATION_LIMIT_EXCEEDED_ERROR_CODE)
                .message(ex.getMessage()).build());
    }


}
