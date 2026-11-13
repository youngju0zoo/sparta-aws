package com.example.spartaaws.member.service;

import com.example.spartaaws.member.dto.request.MemberCreateRequestDto;
import com.example.spartaaws.member.dto.response.MemberCreateResponseDto;
import com.example.spartaaws.member.dto.response.MemberResponseDto;
import com.example.spartaaws.member.entity.Member;
import com.example.spartaaws.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberCreateResponseDto savedMember(MemberCreateRequestDto createRequestDto) {
        Member newMember = new Member(
                createRequestDto.getName(), createRequestDto.getAge(), createRequestDto.getMbti()
                );
        Member savedMember = this.memberRepository.save(newMember);
        MemberCreateResponseDto responseDto = new MemberCreateResponseDto(
                savedMember.getMemberId(), savedMember.getMemberName(),
                savedMember.getMemberAge(), savedMember.getMemberMbti()
        );
        return responseDto;
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long id) {
        Member foundMember = this.memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Member not found"
                ));
        MemberResponseDto responseDto = new MemberResponseDto(
                foundMember.getMemberId(), foundMember.getMemberName(),
                foundMember.getMemberAge(), foundMember.getMemberMbti());
        return responseDto;
    }
}
