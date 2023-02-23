package com.nhnacademy.booklay.server.aspect.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final JwtParser parser;
    private final String gatewayIp;
    private static final String MEMBER_INFO_PREFIX = "/shop/v1/members/memberinfo/";

    public Claims getClaims(JwtInfo jwtInfo) {
        return parser.parseClaimsJws(jwtInfo.getAccessToken()).getBody();
    }

    public String getRole(JwtInfo jwtInfo) {
        Claims claims = getClaims(jwtInfo);

        var role =
            (ArrayList<LinkedHashMap<String, String>>) claims.get("role");

        return role.get(0).get("authority");
    }

    public Claims getClaims(String jwt) {
        return parser.parseClaimsJws(jwt).getBody();
    }

    public String getRole(String jwt) {
        Claims claims = getClaims(jwt);

        var role =
            (ArrayList<LinkedHashMap<String, String>>) claims.get("role");

        return role.get(0).get("authority");
    }

    public String getUuid(String jwt) {
        Claims claims = getClaims(jwt);

        return (String) claims.get("uuid");
    }

    public String getEmail(String jwt) {
        Claims claims = getClaims(jwt);

        return (String) claims.get("email");
    }

    public String getShopUrl() {
        return gatewayIp + MEMBER_INFO_PREFIX;
    }

}
