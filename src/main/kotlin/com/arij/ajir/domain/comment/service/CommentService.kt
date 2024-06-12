package com.arij.ajir.domain.comment.service

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.model.Comment
import com.arij.ajir.domain.comment.model.toResponse
import com.arij.ajir.domain.comment.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository
) {
    fun createComment(issueId: Long, request: CommentCreateRequest): CommentResponse {
        /* TODO
            1. issue가 존재하는지 확인 --> 없다면 에러
            2. member가 존재하는지 확인 --> 없다면 에러
            3. ㄷ
         */
        val comment : Comment = Comment.createComment(request.content,issueId,1)
        comment.issueId = 1
        commentRepository.save(comment)

        return comment.toResponse()
    }

    fun updateComment(commentId: Long, request: CommentUpdateRequest): CommentResponse {
        TODO("Not yet implemented")
    }

    fun deleteComment(commentId: Long): Unit {
        TODO("Not yet implemented")
    }


}