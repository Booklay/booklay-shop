package com.nhnacademy.booklay.server.controller.delivery;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDestinationService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final DeliveryDestinationService deliveryDestinationService;

    @PostMapping("/create/{memberNo}")
    public ResponseEntity<Void> createDeliveryDestination(@Valid @RequestBody
                                                          DeliveryDestinationCURequest requestDto,
                                                          @PathVariable Long memberNo) {
        deliveryDestinationService.createDeliveryDestination(memberNo,
            requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @GetMapping("/list/{memberNo}")
    public ResponseEntity<List<DeliveryDestinationRetrieveResponse>> retrieveDeliveryDestinations(
        @PathVariable Long memberNo) {

        List<DeliveryDestinationRetrieveResponse> response =
            deliveryDestinationService.retrieveDeliveryDestinations(memberNo);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/{addressNo}")
    public ResponseEntity<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestination(
        @PathVariable Long addressNo) {
        DeliveryDestinationRetrieveResponse response =
            deliveryDestinationService.retrieveDeliveryDestination(addressNo);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @PostMapping("/update/{memberNo}/{addressNo}")
    public ResponseEntity<Void> updateDeliveryDestination(
        @Valid @RequestBody DeliveryDestinationCURequest requestDto,
        @PathVariable Long memberNo,
        @PathVariable Long addressNo) {
        deliveryDestinationService.updateDeliveryDestination(memberNo, addressNo, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @DeleteMapping("/delete/{memberNo}/{addressNo}")
    public ResponseEntity<Void> deleteDeliveryDestination(@PathVariable Long memberNo,
                                                          @PathVariable Long addressNo) {
        deliveryDestinationService.deleteDeliveryDestination(memberNo, addressNo);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }


}
