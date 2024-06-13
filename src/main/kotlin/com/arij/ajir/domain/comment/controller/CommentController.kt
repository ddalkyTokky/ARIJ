package com.arij.ajir.domain.comment.controller

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.service.CommentService
import com.arij.ajir.infra.security.UserPrincipal
import com.arij.ajir.infra.security.jwt.JwtPlugin
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
    private val jwtPlugin: JwtPlugin
) {

    @PostMapping("/{issueId}")
    fun createComment(
        @AuthenticationPrincipal test: UserPrincipal?,
        @PathVariable issueId: Long,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {


        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createComment(issueId, request))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @AuthenticationPrincipal test: UserPrincipal?,
        @PathVariable commentId: Long,
        @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentResponse> {


        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(commentId, request))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @AuthenticationPrincipal test: UserPrincipal?,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {


        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(commentId))
    }


}