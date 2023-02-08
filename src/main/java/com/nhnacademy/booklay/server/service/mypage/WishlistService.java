package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 최규태
 */

public interface WishlistService {

    void createWishlist(CreateDeleteWishlistAndAlarmRequest request);

    void deleteWishlist(CreateDeleteWishlistAndAlarmRequest request);

    WishlistAndAlarmBooleanResponse retrieveExists(Long memberNo);

    Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException;
}
