package com.arij.ajir.domain.member.service

import com.arij.ajir.common.exception.DuplicateArgumentException
import com.arij.ajir.common.exception.InvalidCredentialException
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.common.exception.PasswordRecordException
import com.arij.ajir.domain.member.dto.*
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.domain.team.repository.TeamRepository
import com.arij.ajir.domain.team.service.TeamService
import com.arij.ajir.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val teamRepository: TeamRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtPlugin: JwtPlugin
){
    fun emailSignup(memberCreateRequest: MemberCreateRequest){
        if(memberRepository.existsByEmail(memberCreateRequest.email)){
            throw DuplicateArgumentException("Member", memberCreateRequest.email)
        }
        if(memberRepository.existsByNickname(memberCreateRequest.nickname)){
            throw DuplicateArgumentException("Member", memberCreateRequest.nickname)
        }

        val member: Member = Member()
        val team = teamRepository.findByIdOrNull(1L)
        member.let {
            it.team = team
            it.role = Role.USER
            it.email = memberCreateRequest.email
            it.password = bCryptPasswordEncoder.encode(memberCreateRequest.password)
            it.password2 = "password2"
            it.password2 = "password3"
            it.nickname = memberCreateRequest.nickname
        }
        memberRepository.save(member)
    }

    @Transactional(readOnly = true)
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

    fun updateNickname(
        memberNicknameUpdateRequest: MemberNicknameUpdateRequest,
        memberEmail: String
        ) {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        member.nickname = memberNicknameUpdateRequest.nickname
    }

    fun updatePassword(
        request: MemberPasswordUpdateRequest,
        memberEmail: String
    ) {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        if (!bCryptPasswordEncoder.matches(request.oldPw, member.password)
        ) {
            throw InvalidCredentialException()
        }

        if(request.oldPw == request.newPw){
            throw PasswordRecordException()
        }
        if(bCryptPasswordEncoder.matches(request.newPw, member.password2)){
            throw PasswordRecordException()
            }
        if(bCryptPasswordEncoder.matches(request.newPw, member.password3)){
            throw PasswordRecordException()
        }

        member.password3= member.password2
        member.password2 = member.password
        member.password =
            bCryptPasswordEncoder.encode(
                request.newPw
            )
    }

    fun deleteMember(memberEmail: String) {
        val member = memberRepository.findByEmail(memberEmail) ?: throw ModelNotFoundException("Member", memberEmail)

        memberRepository.delete(member)
    }
}