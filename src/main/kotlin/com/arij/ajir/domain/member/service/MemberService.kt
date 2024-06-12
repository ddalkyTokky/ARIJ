package com.arij.ajir.domain.member.service

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.member.dto.MemberCreateRequest
import com.arij.ajir.domain.member.dto.MemberNicknameUpdateRequest
import com.arij.ajir.domain.member.dto.MemberPasswordUpdateRequest
import com.arij.ajir.domain.member.dto.MemberResponse
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService (
    private val memberRepository: MemberRepository,
    private val teamService: TeamService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
){
    fun getMemberById(id: Long): Member {
        return memberRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Member", id.toString())
    }

    fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email) ?: throw ModelNotFoundException("Member", email)
    }

    @Transactional
    fun emailSignup(memberCreateRequest: MemberCreateRequest): MemberResponse {
        val member: Member = Member()
        val team = teamService.getTeamById(1L)
        member.let {
            it.team = team
            it.role = Role.USER
            it.email = memberCreateRequest.email
            it.password = bCryptPasswordEncoder.encode(memberCreateRequest.password)
            it.nickname = memberCreateRequest.nickname
        }
        return memberRepository.save(member).toResponse()
    }

    @Transactional
    fun updateNickname(
        memberNicknameUpdateRequest: MemberNicknameUpdateRequest,
        memberEmail: String
        ): MemberResponse {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        member.nickname = memberNicknameUpdateRequest.nickname
        return member.toResponse()
    }

    @Transactional
    fun updatePassword(
        memberPasswordUpdateRequest: MemberPasswordUpdateRequest,
        memberEmail: String
    ): MemberResponse {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        if (bCryptPasswordEncoder.matches(
                memberPasswordUpdateRequest.oldPw,
                member.password
            )
        ) {
            member.password =
                bCryptPasswordEncoder.encode(
                    memberPasswordUpdateRequest.newPw
                )
        }
        return member.toResponse()
    }

    @Transactional
    fun deleteMember(memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)

        memberRepository.delete(member)
    }
}