package com.arij.ajir.domain.issue.controller

import com.arij.ajir.common.exception.TokenException
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.issue.service.IssueService
import com.arij.ajir.infra.security.jwt.JwtPlugin
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/issues")
class IssueController(
    private val issueService: IssueService,
    private val jwtPlugin: JwtPlugin
) {

//    @GetMapping
//    fun getAllIssues() : ResponseEntity<List<IssueResponse>> {}

    @GetMapping("/{issueId}")
    fun getIssueById(@PathVariable("issueId") id: Long): ResponseEntity<IssueResponseWithCommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.getIssueById(id))
    }

    @PostMapping
    fun createIssue(
        @RequestHeader httpsHeaders: HttpHeaders,
        @RequestBody issueCreateRequest: IssueCreateRequest
    ): ResponseEntity<IssueIdResponse> {
        val token: String = httpsHeaders["Authorization"]?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.createIssue(issueCreateRequest, email))
    }

    @PutMapping("/{issueId}")
    fun updateIssue(
        @RequestHeader httpsHeaders: HttpHeaders,
        @PathVariable("issueId") id: Long, request: IssueUpdateRequest
    ): ResponseEntity<Unit> {
        val token: String = httpsHeaders["Authorization"]?.get(0) ?: throw TokenException("No Token Found")

        return ResponseEntity.status(HttpStatus.OK).body(issueService.updateIssue(id, request))
    }

    @PatchMapping("/{issueId}")
    fun updateIssuePriority(
        @PathVariable("issueId") id: Long,
        @RequestBody priorityUpdateRequest: PriorityUpdateRequest,
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(issueService.updatePriority(id, priorityUpdateRequest.priority))
    }

    @DeleteMapping("/{issueId}")
    fun deleteIssue(@PathVariable("issueId") id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(issueService.deleteIssue(id))
    }
}