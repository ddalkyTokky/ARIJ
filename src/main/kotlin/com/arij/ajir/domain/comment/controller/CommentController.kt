package com.arij.ajir.domain.comment.controller

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.service.CommentService
import com.arij.ajir.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @PostMapping("/{issueId}")
    fun createComment(
        @AuthenticationPrincipal person: UserPrincipal?,
        @PathVariable issueId: Long,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {

        if (person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createComment(issueId, request, person))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @AuthenticationPrincipal person: UserPrincipal?,
        @PathVariable commentId: Long,
        @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentResponse> {

        if (person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(commentId, request, person))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @AuthenticationPrincipal person: UserPrincipal?,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {

        if (person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(commentId, person))
    }


}