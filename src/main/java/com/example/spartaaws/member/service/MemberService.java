package com.example.spartaaws.member.service;

import com.example.spartaaws.member.dto.request.MemberCreateRequestDto;
import com.example.spartaaws.member.dto.response.MemberCreateResponseDto;
import com.example.spartaaws.member.dto.response.MemberResponseDto;
import com.example.spartaaws.member.entity.Member;
import com.example.spartaaws.member.repository.MemberRepository;
import com.example.spartaaws.s3.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public MemberService(MemberRepository memberRepository, S3Service s3Service) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
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

    @Transactional
    public String uploadProfileImage(Long id, MultipartFile file) {
        Member foundMember = this.memberRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Member not found"
                ));
        String imageUrl = this.s3Service.uploadImage(file);
        foundMember.updateProfileImage(imageUrl);
        return imageUrl;
    }

    @Transactional(readOnly = true)
    public String getProfileImageUrl(Long id) {
        Member foundMember = this.memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        String imageUrl = foundMember.getProfileImageUrl();
        if (imageUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile image not found");
        }
        return s3Service.getPresignedUrl(imageUrl);
    }
}
