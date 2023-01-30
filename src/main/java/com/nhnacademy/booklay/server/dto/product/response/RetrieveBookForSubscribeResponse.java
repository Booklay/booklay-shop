package com.nhnacademy.booklay.server.dto.product.response;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RetrieveBookForSubscribeResponse {

    @NotNull
    Long productId;
    @NotNull
    String title;
    @Setter
    List<String> authors;
    @NotNull
    String publisher;
    @Setter
    Boolean isRegistered;

    public RetrieveBookForSubscribeResponse(Long productId, String title, String publisher) {
        this.productId = productId;
        this.title = title;
        this.publisher = publisher;
    }
}
