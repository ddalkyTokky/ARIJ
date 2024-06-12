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
            2. member가 존재하는지 확인 --> 없다면 에러 <== 인증 인가가 되면 없어도 됨
            3-1. comment 생성
            3-2. 내용이 비어있는지 유효성 검사 --> 제약이 생긴다고 하면
            4. comment DB에 저장
                issue에서 comment에 연관관계 맺을 시 이슈에 먼저 추가하고 저장
            5. response dto 반환
         */

        // TODO : issue 존재하는지 확인

        val comment: Comment = Comment(
            content = request.content,
            issueId = issueId,
            memberId = 1 // 나중에 인가 구현되면 바뀜
        )
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