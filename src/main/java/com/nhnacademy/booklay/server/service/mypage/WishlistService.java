package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

public interface WishlistService {

    void createWishlist(WishlistAndAlarmRequest request);

    void deleteWishlist(WishlistAndAlarmRequest request);

    WishlistAndAlarmBooleanResponse retrieveExists(WishlistAndAlarmRequest memberNo);

    Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException;

    @Transactional(readOnly = true)
    List<RetrieveProductResponse> retrieveWishlist(Long memberNo) throws IOException;
}
