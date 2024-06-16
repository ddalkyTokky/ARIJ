package com.arij.ajir.domain.issue.controller

import com.arij.ajir.common.exception.InvalidCredentialException
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.issue.service.IssueService
import com.arij.ajir.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/issues")
class IssueController(
    private val issueService: IssueService,
) {
    // TODO : 목록 전체 조회는 로그인 없이도 가능
    @GetMapping
    fun getAllIssues(
        @RequestParam(required = false, defaultValue = "priority") topic : String,
        @RequestParam(required = false) keyword : String,
        @RequestParam(required = false, defaultValue = "priority") orderBy : String,
        @RequestParam(required = false, defaultValue = "true" ) ascend : Boolean
    ) : ResponseEntity<List<IssueResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.getAllIssues(topic, keyword, orderBy, ascend))
    }

    @GetMapping("/{issueId}")
    fun getIssueById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable("issueId") id: Long
    ): ResponseEntity<IssueResponseWithCommentResponse> {

        if (userPrincipal == null) {
           throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.OK).body(issueService.getIssueById(id, userPrincipal.email))
    }

    @PostMapping
    fun createIssue(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestBody issueCreateRequest: IssueCreateRequest
    ): ResponseEntity<IssueIdResponse> {

        if (userPrincipal == null) {
            throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(issueService.createIssue(issueCreateRequest, userPrincipal.email))
    }

    @PutMapping("/{issueId}")
    fun updateIssue(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable("issueId") id: Long, request: IssueUpdateRequest
    ): ResponseEntity<Unit> {

        if (userPrincipal == null) {
            throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.OK).body(issueService.updateIssue(id, request, userPrincipal.email))
    }

    @PatchMapping("/{issueId}/priority")
    fun updateIssuePriority(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable("issueId") id: Long,
        @RequestBody priorityUpdateRequest: PriorityUpdateRequest,
    ): ResponseEntity<Unit> {

        if (userPrincipal == null) {
            throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(issueService.updatePriority(id, priorityUpdateRequest, userPrincipal.email))
    }

    @PatchMapping("/{issueId}/work")
    fun updateIssueWorkingStatus(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable("issueId") id: Long,
        @RequestBody workingStatusUpdateRequest: WorkingStatusUpdateRequest,
    ): ResponseEntity<Unit> {

        if (userPrincipal == null) {
            throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(issueService.updateWorkingStatus(id, workingStatusUpdateRequest, userPrincipal.email))
    }

    @DeleteMapping("/{issueId}")
    fun deleteIssue(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable("issueId") id: Long
    ): ResponseEntity<Unit> {

        if (userPrincipal == null) {
            throw InvalidCredentialException()
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(issueService.deleteIssue(id, userPrincipal.email))
    }
}