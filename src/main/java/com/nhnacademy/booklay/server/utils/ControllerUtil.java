package com.nhnacademy.booklay.server.utils;

import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerUtil {

    public static String buildString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static Map<String, String> getMemberInfoMap(MemberInfo memberInfo){
        Map<String, String> map = new HashMap<>();
        if (memberInfo.getMemberNo() == null) {
            String nullChangeText = "\u0000";
            map.put("member_info_memberNo", nullChangeText);
            map.put("member_info_gender", nullChangeText);
            map.put("member_info_memberId", nullChangeText);
            map.put("member_info_nickname", nullChangeText);
            map.put("member_info_name", nullChangeText);
            map.put("member_info_birthday", nullChangeText);
            map.put("member_info_phoneNo", nullChangeText);
            map.put("member_info_email", nullChangeText);
        }else {
            map.put("member_info_memberNo", memberInfo.getMemberNo().toString());
            map.put("member_info_gender", memberInfo.getGender());
            map.put("member_info_memberId", memberInfo.getMemberId());
            map.put("member_info_nickname", memberInfo.getNickname());
            map.put("member_info_name", memberInfo.getName());
            map.put("member_info_birthday", memberInfo.getBirthday().toString());
            map.put("member_info_phoneNo", memberInfo.getPhoneNo());
            map.put("member_info_email", memberInfo.getEmail());
        }
        return map;
    }

    public static MultiValueMap<String, String> getMemberInfoMultiValueMap(MemberInfo memberInfo){
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(getMemberInfoMap(memberInfo));
        return multiValueMap;
    }

}
