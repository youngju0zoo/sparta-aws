package com.example.spartaaws.member.repository;

import com.example.spartaaws.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
