package com.linkdeco.link_deco.controller;

import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.validator.SignupValidator;
import com.linkdeco.link_deco.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SignupValidator signupValidator;
    @Value("${spring.cloud.aws.s3.default-profile-image}")
    private String defaultProfileImage;
    /**
     * 회원가입
     */
    @InitBinder("memberRequestDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signupValidator);
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("defaultProfileImage", defaultProfileImage);
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/signupForm";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("memberRequestDto") MemberRequestDto requestDto,
                         BindingResult result,
                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("defaultProfileImage", defaultProfileImage);
            return "member/signupForm";
        }
        memberService.signup(requestDto, profileImage);
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
