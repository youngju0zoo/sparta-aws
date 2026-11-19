package com.example.spartaaws.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberName;
    private int memberAge;
    private String memberMbti;

    public Member(String memberName, int memberAge, String memberMbti) {
        this.memberName = memberName;
        this.memberAge = memberAge;
        this.memberMbti = memberMbti;
    }
}
