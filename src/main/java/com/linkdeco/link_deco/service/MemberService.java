package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    @Value("${spring.cloud.aws.s3.default-profile-image}")
    private String defaultProfileImage;

    @Transactional
    public Long signup(MemberRequestDto requestDto, MultipartFile profileImage) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 객체 생성 및 저장
        Member member = requestDto.toEntity(encodedPassword);

        // 프로필 이미지 설정
        if (profileImage != null && !profileImage.isEmpty()) {
            // 사용자가 파일을 업로드한 경우 -> S3에 업로드 후 그 URL 저장
            String uploadedUrl = s3Service.upload(profileImage);
            member.updateProfileImage(uploadedUrl);
        } else {
            // 파일이 없는 경우 -> 미리 설정한 기본 이미지 URL 저장
            member.updateProfileImage(defaultProfileImage);
        }
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void updateProfileImage(Long memberId, String imageUrl) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        member.updateProfileImage(imageUrl);
    }

    @Transactional
    public void deleteProfileImage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        s3Service.delete(member.getProfileImageKey());
        // 프로필 이미지 삭제 시 기본 이미지로 변경
        member.updateProfileImage(defaultProfileImage);
    }
}
