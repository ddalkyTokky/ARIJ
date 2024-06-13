package com.arij.ajir.domain.member.controller

import com.arij.ajir.common.exception.TokenException
import com.arij.ajir.domain.member.dto.*
import com.arij.ajir.domain.member.service.MemberService
import com.arij.ajir.infra.security.UserPrincipal
import com.arij.ajir.infra.security.jwt.JwtPlugin
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val jwtPlugin: JwtPlugin
) {
    @PostMapping("/signup")
    fun emailSignupMember(@RequestBody @Valid memberCreateRequest: MemberCreateRequest): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.emailSignup(memberCreateRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(loginRequest))
    }

    @PatchMapping("/nickname")
    fun updateMemberNickname(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestBody @Valid memberNicknameUpdateRequest: MemberNicknameUpdateRequest
    ): ResponseEntity<Unit> {
        if(userPrincipal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateNickname(memberNicknameUpdateRequest, userPrincipal.email))
    }

    @PatchMapping("/password")
    fun updateMemberPassword(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestBody @Valid
        memberPasswordUpdateRequest: MemberPasswordUpdateRequest
    ): ResponseEntity<Unit> {
        if(userPrincipal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updatePassword(memberPasswordUpdateRequest, userPrincipal.email))
    }

    @DeleteMapping
    fun deleteMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
    ): ResponseEntity<Unit> {
        if(userPrincipal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(memberService.deleteMember(userPrincipal.email))
    }
}