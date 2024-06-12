package com.arij.ajir.domain.member.service

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.member.dto.MemberRequest
import com.arij.ajir.domain.member.dto.MemberResponse
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
    fun emailSignup(memberRequest: MemberRequest, image: String?): MemberResponse {
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