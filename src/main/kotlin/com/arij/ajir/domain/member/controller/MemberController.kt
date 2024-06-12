package com.arij.ajir.domain.member.controller

import com.arij.ajir.common.exception.TokenException
import com.arij.ajir.domain.member.dto.*
import com.arij.ajir.domain.member.service.MemberService
import com.arij.ajir.infra.security.jwt.JwtPlugin
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val jwtPlugin: JwtPlugin
) {
    @PostMapping("/signup")
    fun emailSignupMember(@RequestBody @Valid memberCreateRequest: MemberCreateRequest): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.emailSignup(memberCreateRequest))
    }

    @PostMapping("login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(loginRequest))
    }

    @PatchMapping("/nickname")
    fun updateMemberNickname(
        @RequestHeader httpsHeaders: HttpHeaders,
        @RequestBody @Valid memberNicknameUpdateRequest: MemberNicknameUpdateRequest
    ): ResponseEntity<MemberResponse> {
        val token: String = httpsHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateNickname(memberNicknameUpdateRequest, email))
    }

    @PatchMapping("/password")
    fun updateMemberPassword(
        @RequestHeader httpsHeaders: HttpHeaders,
        @RequestBody @Valid
        memberPasswordUpdateRequest: MemberPasswordUpdateRequest
    ): ResponseEntity<MemberResponse> {
        val token: String = httpsHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updatePassword(memberPasswordUpdateRequest, email))
    }

    @DeleteMapping
    fun deleteMember(
        @RequestHeader httpsHeaders: HttpHeaders,
    ): ResponseEntity<Unit> {
        val token: String = httpsHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(memberService.deleteMember(email))
    }
}