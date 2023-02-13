package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 최규태
 */

public interface WishlistService {

    void createWishlist(WishlistAndAlarmRequest request);

    void deleteWishlist(WishlistAndAlarmRequest request);

    WishlistAndAlarmBooleanResponse retrieveExists(WishlistAndAlarmRequest memberNo);

    Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException;
}
