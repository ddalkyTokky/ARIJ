package com.arij.ajir.domain.comment.service

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.model.Comment
import com.arij.ajir.domain.comment.model.toResponse
import com.arij.ajir.domain.comment.repository.CommentRepository
import com.arij.ajir.domain.issue.repository.IssueRepository
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val issueRepository: IssueRepository
) {
    fun createComment(issueId: Long, request: CommentCreateRequest, person: UserPrincipal): CommentResponse {
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
            issue = issueRepository.findByIdOrNull(1L)!!, //TODO 임시 코드. 수정필요.
            member = memberRepository.findByIdOrNull(1L)!!
        )
        commentRepository.save(comment)

        return comment.toResponse()
    }

    fun updateComment(commentId: Long, request: CommentUpdateRequest, person: UserPrincipal): CommentResponse {
        /* TODO
            1. DB에서 Comment 가져오기 --> 없으면 에러
            2-1. comment의 내용 수정
            2-2. 댓글 내용 수정시 유효성 검사 --> 제약이 생길 시
            3. DB에 변경된 Comment 저장
            4. response dto로 반환
         */

        val comment: Comment =
            commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("Comment not found")

        comment.updateContent(request.content)

        commentRepository.save(comment)

        return comment.toResponse()
    }

    fun deleteComment(commentId: Long, person: UserPrincipal): Unit {
        /* TODO
            1. DB에서 Comment 가져오기 --> 없으면 에러
            2. comment 삭제
         */

        val comment: Comment =
            commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("Comment not found")

        commentRepository.delete(comment)
    }


}