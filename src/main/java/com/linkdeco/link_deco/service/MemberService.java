package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(MemberRequestDto requestDto) {

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 객체 생성 및 저장
        Member member = requestDto.toEntity(encodedPassword);

        return memberRepository.save(member).getId();
    }
}
