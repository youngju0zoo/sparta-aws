package com.example.spartaaws.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {
    private String name;
    private int age;
    private String mbti;
}
