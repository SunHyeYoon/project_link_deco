package com.linkdeco.link_deco.controller;

import com.linkdeco.link_deco.dto.LoginRequestDto;
import com.linkdeco.link_deco.dto.LoginResponseDto;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody MemberRequestDto requestDto) {
        Long memberId = memberService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = memberService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 테스트용
     * SecurityContext에 저장된 memberId를 어떻게 꺼내 쓰는지 확인
     */
    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(@AuthenticationPrincipal Long memberId) {
        // JwtFilter에서 SecurityContextHolder에 저장한 memberId를 바로 주입받음
        return ResponseEntity.ok("현재 로그인한 사용자의 ID와 닉네임은 " + memberId + " 입니다.");
    }
}
