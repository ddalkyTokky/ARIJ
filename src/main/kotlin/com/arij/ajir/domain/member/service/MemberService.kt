package com.arij.ajir.domain.member.service

import com.arij.ajir.common.exception.DuplicateArgumentException
import com.arij.ajir.common.exception.InvalidCredentialException
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.member.dto.*
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.infra.security.jwt.JwtPlugin
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val teamService: TeamService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtPlugin: JwtPlugin
){
    @Transactional
    fun emailSignup(memberCreateRequest: MemberCreateRequest): MemberResponse {
        if(memberRepository.existsByEmail(memberCreateRequest.email)){
            throw DuplicateArgumentException("Member", memberCreateRequest.email)
        }
        if(memberRepository.existsByNickname(memberCreateRequest.nickname)){
            throw DuplicateArgumentException("Member", memberCreateRequest.nickname)
        }

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

    fun login(loginRequest: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(loginRequest.email) ?: throw ModelNotFoundException(
            "Member",
            loginRequest.email
        )

        if (!bCryptPasswordEncoder.matches(loginRequest.password, member.password)) {
            throw InvalidCredentialException()
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email!!,
                role = member.role
            )
        )
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

        if (bCryptPasswordEncoder.matches(memberPasswordUpdateRequest.oldPw, member.password)
        ) {
            member.password =
                bCryptPasswordEncoder.encode(
                    memberPasswordUpdateRequest.newPw
                )
        }
        return member.toResponse()
    }

    @Transactional
    fun deleteMember(memberEmail: String) {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        memberRepository.delete(member)
    }
}