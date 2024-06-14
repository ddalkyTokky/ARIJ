package com.arij.ajir.domain.comment.service

import com.arij.ajir.domain.comment.dto.CommentCreateRequest
import com.arij.ajir.domain.comment.dto.CommentCreateResponse
import com.arij.ajir.domain.comment.dto.CommentUpdateRequest
import com.arij.ajir.domain.comment.model.Comment
import com.arij.ajir.domain.comment.model.toCreateResponse
import com.arij.ajir.domain.comment.repository.CommentRepository
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.repository.IssueRepository
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val issueRepository: IssueRepository
) {
    fun createComment(issueId: Long, request: CommentCreateRequest, person: UserPrincipal): CommentCreateResponse {
        /* TODO
            1. issue가 존재하는지 확인 --> 없다면 에러
            2. member가 존재하는지 확인 --> 없다면 에러 <== 인증 인가가 되면 없어도 됨
            3-1. comment 생성
            3-2. 내용이 비어있는지 유효성 검사 --> 제약이 생긴다고 하면
            4. comment DB에 저장
                issue에서 comment에 연관관계 맺을 시 이슈에 먼저 추가하고 저장
            5. response dto 반환
         */

        val issue: Issue =
            issueRepository.findByIdOrNull(issueId) ?: throw IllegalArgumentException("issue not found")

        if (issue.team.name == "DUMMY") throw IllegalArgumentException("더미팀 댓글 생성 금지 / 애초에 이슈가 존재하면 안됨")

        val member: Member =
            memberRepository.findByIdOrNull(person.id) ?: throw IllegalArgumentException("member not found")

        if (member.role.name != Role.ADMIN.name) {

            if (member.team!!.name == "DUMMY") throw IllegalArgumentException("사용자가 더미팀일 때 댓글 생성 금지")

            if (issue.team.name != member.team!!.name) throw IllegalArgumentException("사용자가 다른 팀 이슈에 댓글을 남길 수 없음")
        }

        val comment: Comment = Comment(
            content = request.content,
            issue = issue,
            member = member,
        )

        commentRepository.save(comment)

        return comment.toCreateResponse()
    }

    fun updateComment(commentId: Long, request: CommentUpdateRequest, person: UserPrincipal): Unit {
        /* TODO
            1. DB에서 Comment 가져오기 --> 없으면 에러
            2-1. comment의 내용 수정
            2-2. 댓글 내용 수정시 유효성 검사 --> 제약이 생길 시
            3. DB에 변경된 Comment 저장
            4. response dto로 반환
         */

        val comment = checkAuthorization(commentId, person.id)

        comment.updateContent(request.content)

        commentRepository.save(comment)

    }

    fun deleteComment(commentId: Long, person: UserPrincipal): Unit {
        /* TODO
            1. DB에서 Comment 가져오기 --> 없으면 에러
            2. comment 삭제
         */

        val comment = checkAuthorization(commentId, person.id)

        commentRepository.delete(comment)
    }

    private fun checkAuthorization(commentId: Long, memberId: Long): Comment {

        val comment: Comment =
            commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("Comment not found")

        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("member not found")

        if (member.role.name != Role.ADMIN.name) return comment

        if (((comment.member.id != member.id) || (comment.issue.team != member.team)) &&
            ((member.role.name != Role.LEADER.name) || (member.team!!.name != comment.issue.team.name))
        ) {
            throw IllegalArgumentException("타인의 댓글 이거나 내 댓글이어도 다른 팀일 때 댓글임 ")
        }

        return comment
    }


}