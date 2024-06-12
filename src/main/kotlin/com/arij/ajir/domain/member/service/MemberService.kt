package com.arij.ajir.domain.member.service

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService (
    private val memberRepository: MemberRepository
){
    fun getMemberById(id: Long): Member {
        return memberRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Member", id.toString())
    }

    fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
    }

    @Transactional
    fun emailSignupMember(memberCreateRequest: MemberCreateRequest, image: String?): MemberResponse {
        val authNumber = redisService.getAuthNumber(memberCreateRequest.email)
            ?: throw IllegalArgumentException("Authentication timeout or no authentication request") // 인증 DB에 메일(key)이 없는 경우(인증 시간 초과 또는 인증 요청을 하지 않음 등)

        if (authNumber != memberCreateRequest.authNumber) { // 인증 번호가 일치하지 않는 경우
            throw IllegalArgumentException("The authentication number does not match.")
        }

        return memberRepository.save(
            Member.createMember(
                memberCreateRequest,
                bCryptPasswordEncoder.encode(memberCreateRequest.password),
                image,
                SignUpType.EMAIL
            )
        ).toResponse()
    }

    @Transactional
    fun updateMember(memberUpdateRequest: MemberUpdateRequest, memberId: Long, image: String?): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)

        return member.update(
            memberUpdateRequest,
            bCryptPasswordEncoder.encode(memberUpdateRequest.password),
            image
        ).toResponse()
    }

    @Transactional
    fun deleteMember(memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)

        memberRepository.delete(member)
    }
}