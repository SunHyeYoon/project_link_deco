package com.linkdeco.link_deco.global.config;

import com.linkdeco.link_deco.controller.MemberController;
import com.linkdeco.link_deco.global.security.CustomUserDetailsService;
import com.linkdeco.link_deco.global.validator.SignupValidator;
import com.linkdeco.link_deco.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private SignupValidator signupValidator;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("누구나 접근 가능한 페이지(/)는 200 OK를 반환한다")
    void home_IsAccessible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증이 필요한 페이지(/members/my-page) 접근 시 로그인 페이지로 리다이렉트된다")
    void loginRequiredPage_RedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/members/my-page"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/login"));
    }

    @Test
    @DisplayName("로그인 처리는 설정한 URL(/members/login)에서 POST 방식으로 일어난다")
    void loginProcessing_UrlCheck() throws Exception {
        mockMvc.perform(post("/members/login")
                        .param("username", "test@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection()); // 인증 결과에 따라 리다이렉트됨
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공 시 세션이 무효화되고 홈으로 리다이렉트된다")
    void logout_Success() throws Exception {
        mockMvc.perform(post("/members/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                // 세션이 무효화되었는지, JSESSIONID 쿠키를 지우라고 응답하는지 확인
                .andExpect(header().exists("Set-Cookie"));
    }
}