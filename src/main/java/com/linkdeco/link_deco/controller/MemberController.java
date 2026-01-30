package com.linkdeco.link_deco.controller;

import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.validator.SignupValidator;
import com.linkdeco.link_deco.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SignupValidator signupValidator;

    /**
     * 회원가입
     */
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/signupForm";
    }

    @InitBinder("memberRequestDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signupValidator);
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute MemberRequestDto requestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "member/signupForm";
        }
        memberService.signup(requestDto);
        return "redirect:/members/login"; // 가입 성공하면 로그인 창으로
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String loginForm() {
        return "member/loginForm";
    }

    /**
     * 회원가입, 로그인/로그아웃 띄우기 용
     */
    @GetMapping("")
    public String membersPage() {
        return "member/index";
    }
}
