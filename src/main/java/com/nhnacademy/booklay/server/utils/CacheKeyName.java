package com.nhnacademy.booklay.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeyName {
    public static final String REVIEW_PAGE_KEY_NAME = "postResponsePageCache";
    public static final String POST_RESPONSE_PAGE_CACHE = "postResponsePageCache";
    public static final String PRODUCT_RESPONSE_KEY_NAME = "productResponseCache";
    public static final String PRODUCT_ALL_IN_ONE_KEY_NAME = "productAllInOneCache";
}
