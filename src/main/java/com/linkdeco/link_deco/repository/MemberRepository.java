package com.linkdeco.link_deco.repository;

import com.linkdeco.link_deco.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인을 위한 조회 메서드
    Optional<Member> findByEmail(String email);

    // 이메일 중복 체크를 위한 메서드
    boolean existsByEmail(String email);
}
