package com.example.spartaaws.member.controller;

import com.example.spartaaws.member.dto.request.MemberCreateRequestDto;
import com.example.spartaaws.member.dto.response.MemberCreateResponseDto;
import com.example.spartaaws.member.dto.response.MemberResponseDto;
import com.example.spartaaws.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberCreateResponseDto> createMember(@RequestBody MemberCreateRequestDto createRequestDto) {
        MemberCreateResponseDto createResponseDto = memberService.savedMember(createRequestDto);
        ResponseEntity<MemberCreateResponseDto> response = new ResponseEntity<>(createResponseDto, HttpStatus.CREATED);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable("id") Long id) {
        MemberResponseDto responseDto = memberService.getMember(id);
        ResponseEntity<MemberResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        return response;
    }
}
