package com.arij.ajir.domain.member.controller

import com.arij.ajir.domain.member.dto.MemberCreateRequest
import com.arij.ajir.domain.member.dto.MemberNicknameUpdateRequest
import com.arij.ajir.domain.member.dto.MemberPasswordUpdateRequest
import com.arij.ajir.domain.member.dto.MemberResponse
import com.arij.ajir.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController (
    private val memberService: MemberService
){
    @PostMapping("/signup")
    fun emailSignupMember(@RequestBody @Valid memberCreateRequest: MemberCreateRequest): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.emailSignup(memberCreateRequest))
    }

    @PostMapping("login")
    fun login(@RequestBody )

    @PatchMapping("/nickname")
    fun updateMemberNickname(@RequestBody @Valid memberNicknameUpdateRequest: MemberNicknameUpdateRequest): ResponseEntity<MemberResponse> {
        //TODO memberEmail 토큰에서 받아오기.
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateNickname(memberNicknameUpdateRequest, memberNicknameUpdateRequest.email))
    }

    @PatchMapping("/password")
    fun updateMemberPassword(@RequestBody @Valid
    memberPasswordUpdateRequest: MemberPasswordUpdateRequest): ResponseEntity<MemberResponse> {
        //TODO memberEmail 토큰에서 받아오기.
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updatePassword(memberPasswordUpdateRequest, memberPasswordUpdateRequest.email))
    }

    @DeleteMapping
    fun deleteMember(): ResponseEntity<Unit> {
        //TODO memberEmail 토큰에서 받아오기.
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(memberService.deleteMember("email"))
    }
}