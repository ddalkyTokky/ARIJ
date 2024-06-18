package com.arij.ajir.domain.comment.service

import com.arij.ajir.common.exception.DummyRelatedException
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.common.exception.NotAuthorityException
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

        val issue: Issue =
            issueRepository.findByIdOrNull(issueId) ?: throw ModelNotFoundException("이슈", issueId.toString())

        if (issue.team.name == "DUMMY") throw DummyRelatedException() //"더미팀 댓글 생성 금지 / 애초에 이슈가 존재하면 안됨"

        val member: Member =
            memberRepository.findByIdOrNull(person.id) ?: throw ModelNotFoundException("맴버", person.id.toString())

        if (member.role.name != Role.ADMIN.name) {

            if (member.team!!.name == "DUMMY") throw DummyRelatedException() // "사용자가 더미팀일 때 댓글 생성 금지"

            if (issue.team.name != member.team!!.name) throw NotAuthorityException("사용자가 다른 팀 이슈에 댓글을 남길 수 없습니다", member.role.name)
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

        val comment: Comment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("댓글", commentId.toString())

        val member: Member =
            memberRepository.findByIdOrNull(person.id) ?: throw ModelNotFoundException("맴버", person.id.toString())


        if (((comment.member.id != member.id) || (comment.issue.team != member.team)) && (member.role.name != Role.ADMIN.name)) {
            throw NotAuthorityException("사용자가 다른 팀 이슈에 댓글을 남길 수 없습니다", member.role.name) // TODO : 수정 필요
        }

        comment.updateContent(request.content)

        commentRepository.save(comment)

    }

    fun deleteComment(commentId: Long, person: UserPrincipal): Unit {

        val comment: Comment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("댓글", commentId.toString())

        val member: Member =
            memberRepository.findByIdOrNull(person.id) ?: throw ModelNotFoundException("맴버", person.id.toString())

        if (member.role.name != Role.ADMIN.name) {

            if (((comment.member.id != member.id) || (comment.issue.team != member.team)) &&
                ((member.role.name != Role.LEADER.name) || (member.team!!.name != comment.issue.team.name))
            ) {
                throw NotAuthorityException("사용자가 다른 팀 이슈에 댓글을 남길 수 없습니다", member.role.name) // TODO : 수정 필요
            }
        }


        commentRepository.delete(comment)
    }


}