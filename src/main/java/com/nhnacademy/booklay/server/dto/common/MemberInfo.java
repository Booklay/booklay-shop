package com.nhnacademy.booklay.server.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;


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

        String[] memberInfoFieldName = new String[]{"memberNo", "gender", "memberId", "nickname", "name", "birthday", "phoneNo", "email"};
        int arrayCount = 0;
        String memberNoString = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.memberNo = memberNoString==null?null:Long.parseLong(memberNoString);
        this.gender = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.memberId = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.nickname = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.name = readMap(memberInfoFieldName[arrayCount++], paramMap);
        String birthdayString = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.birthday = birthdayString==null?null:LocalDate.parse(birthdayString);
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
