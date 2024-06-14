package com.arij.ajir.domain.comment.model

import com.arij.ajir.domain.comment.dto.CommentCreateResponse
import com.arij.ajir.domain.comment.dto.CommentResponse
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.member.model.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    var issue: Issue,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateContent(content: String) {
        this.content = content
    }

}

// TODO : 이슈 단건 조회 시  댓글을 이걸로 반환하는데 수정 필요함
fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        commentId = id!!,
        author = member.nickname!!,
        content = content,
        createdAt = createdAt
    )
}

fun Comment.toCreateResponse(): CommentCreateResponse {
    return CommentCreateResponse(
        commentId = id!!,
    )
}