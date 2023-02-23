package com.nhnacademy.booklay.server.dto.common;

import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
        this.birthday = birthdayString==null?null:LocalDate.parse(birthdayString);
        this.phoneNo = readMap(memberInfoFieldName[arrayCount++], paramMap);
        this.email = readMap(memberInfoFieldName[arrayCount], paramMap);
    }

    public MemberInfo(MemberInfoPostGetter memberInfoPostGetter) {
        this.memberNo = memberInfoPostGetter.getMember_info_memberNo();
        this.gender = memberInfoPostGetter.getMember_info_gender();
        this.memberId = memberInfoPostGetter.getMember_info_memberId();
        this.nickname = memberInfoPostGetter.getMember_info_nickname();
        this.name = memberInfoPostGetter.getMember_info_name();
        this.birthday = memberInfoPostGetter.getMember_info_birthday();
        this.phoneNo = memberInfoPostGetter.getMember_info_phoneNo();
        this.email = memberInfoPostGetter.getMember_info_email();
    }

    public MemberInfo(MemberRetrieveResponse memberRetrieveResponse) {
        this.memberNo = memberRetrieveResponse.getMemberNo();
        this.gender = memberRetrieveResponse.getGender();
        this.memberId = memberRetrieveResponse.getMemberId();
        this.nickname = memberRetrieveResponse.getNickname();
        this.name = memberRetrieveResponse.getName();
        this.birthday = memberRetrieveResponse.getBirthday();
        this.phoneNo = memberRetrieveResponse.getPhoneNo();
        this.email = memberRetrieveResponse.getEmail();
    }

    private String readMap(String key, Map<String, String[]> map) {
        if (map.containsKey(key) && map.get(key).length > 0 && !map.get(key)[0].equals("\u0000")) {
            return map.get(key)[0];
        } else {
            return null;
        }
    }
}
