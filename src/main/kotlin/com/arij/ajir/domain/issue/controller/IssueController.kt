package com.arij.ajir.domain.issue.controller

import com.arij.ajir.domain.issue.dto.IssueCreateRequest
import com.arij.ajir.domain.issue.dto.IssueResponse
import com.arij.ajir.domain.issue.dto.PriorityUpdateRequest
import com.arij.ajir.domain.issue.dto.IssueUpdateRequest
import com.arij.ajir.domain.issue.service.IssueService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/issues")
class IssueController(private val issueService: IssueService) {

//    @GetMapping
//    fun getAllIssues() : ResponseEntity<List<IssueResponse>> {}

    @GetMapping("/{issueId}")
    fun getIssueById(@PathVariable("issueId") id: Long): ResponseEntity<IssueResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.getIssueById(id))
    }

    @PostMapping
    fun createIssue(@RequestBody issueCreateRequest: IssueCreateRequest): ResponseEntity<IssueResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.createIssue(issueCreateRequest))
    }

    @PutMapping("/{issueId}")
    fun updateIssue(@PathVariable("issueId") id: Long, request: IssueUpdateRequest): ResponseEntity<IssueResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.updateIssue(id, request))
    }

    @PatchMapping("/{issueId}")
    fun updateIssuePriority(
        @PathVariable("issueId") id: Long,
        @RequestBody priorityUpdateRequest: PriorityUpdateRequest,
    ): ResponseEntity<IssueResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(issueService.updatePriority(id, priorityUpdateRequest.priority))
    }

    @DeleteMapping("/{issueId}")
    fun deleteIssue(@PathVariable("issueId") id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(issueService.deleteIssue(id))
    }
}