package com.nhnacademy.booklay.server.dto.product.request;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class DisAndConnectBookWithSubscribeRequest {

    @NotNull
    Long productId;

    @NotNull
    Long subscribeId;

    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;

    public DisAndConnectBookWithSubscribeRequest(Long productId, Long subscribeId) {
        this.productId = productId;
        this.subscribeId = subscribeId;
    }
}
