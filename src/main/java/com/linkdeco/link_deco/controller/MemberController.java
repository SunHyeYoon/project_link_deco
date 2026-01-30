package com.linkdeco.link_deco.controller;

import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/signupForm";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute MemberRequestDto requestDto) {
        memberService.signUp(requestDto);
        return "redirect:/members/login"; // 가입 성공하면 로그인 창으로
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String loginForm() {
        return "member/loginForm";
    }
}
