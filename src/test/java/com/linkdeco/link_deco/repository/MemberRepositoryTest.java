package com.linkdeco.link_deco.repository;

import com.linkdeco.link_deco.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원을 조회할 수 있다")
    void findByEmail() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testuser")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> found = memberRepository.findByEmail("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getNickname()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회하면 Empty를 반환한다")
    void findByEmail_notFound() {
        // when
        Optional<Member> found = memberRepository.findByEmail("notexist@example.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("이메일 존재 여부를 확인할 수 있다")
    void existsByEmail() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testuser")
                .build();
        memberRepository.save(member);

        // when
        boolean exists = memberRepository.existsByEmail("test@example.com");
        boolean notExists = memberRepository.existsByEmail("notexist@example.com");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("회원을 저장할 수 있다")
    void save() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testuser")
                .imageUrl("http://example.com/image.jpg")
                .build();

        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getPassword()).isEqualTo("password123");
        assertThat(saved.getNickname()).isEqualTo("testuser");
        assertThat(saved.getImageUrl()).isEqualTo("http://example.com/image.jpg");
    }
}