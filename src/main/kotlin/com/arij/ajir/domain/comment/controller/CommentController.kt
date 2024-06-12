package com.arij.ajir.domain.comment.controller

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/{issueId}")
    fun createComment(
        @PathVariable issueId: Long,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createComment(issueId, request))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(commentId, request))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(commentId))
    }
}