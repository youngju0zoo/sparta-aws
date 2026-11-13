package com.example.spartaaws.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateResponseDto {
    private Long memberId;
    private String name;
    private int age;
    private String mbti;
}
