package com.nhnacademy.booklay.server.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class MemberInfo {

    private Long memberNo;
    private String gender;
    private String memberId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;

    public MemberInfo(Map<String, String[]> paramMap) {

        String[] memberInfoFieldName = new String[]{"member_info_memberNo", "member_info_gender", "member_info_memberId", "member_info_nickname", "member_info_name", "member_info_birthday", "member_info_phoneNo", "member_info_email"};
        int arrayCount = 0;
        String memberNoString = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.memberNo = memberNoString==null?null:Long.parseLong(memberNoString);
        this.gender = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.memberId = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.nickname = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.name = readMap(memberInfoFieldName[arrayCount++], paramMap);
        String birthdayString = readMap(memberInfoFieldName[arrayCount++], paramMap);
        if (birthdayString != null) {
            String[] bStringArray = birthdayString.replace("[", "").replace("]", "")
                    .split(", ");
            List<String> strings = Arrays.stream(bStringArray).map(s -> s.length() < 2 ? "0" + s : s).collect(Collectors.toList());
            this.birthday = LocalDate.parse(strings.get(0)+"-"+strings.get(1)+"-"+strings.get(2));
        }else {
            this.birthday = null;
        }
        this.phoneNo = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.email = readMap(memberInfoFieldName[arrayCount], paramMap);
    }
    private String readMap(String key, Map<String, String[]> map){
        if(map.containsKey(key)&&map.get(key).length>0&&!map.get(key)[0].equals("\u0000")){
            return map.get(key)[0];
        }else {
            return null;
        }
    }
}
