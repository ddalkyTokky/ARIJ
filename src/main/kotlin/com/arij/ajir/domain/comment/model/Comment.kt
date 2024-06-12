package com.arij.ajir.domain.comment.model

import com.arij.ajir.domain.comment.dto.CommentResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // 나중에 지워질수도? --> issue에서 연관관계를 단방향으로 맺었다면
    @Column(name = "issue_id", nullable = false)
    var issueId: Long,

    @Column(name = "member_id", nullable = false)
    var memberId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateContent(content: String) {
        this.content = content
    }

}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        commentId = id!!,
        author = "${memberId}의 닉네임",
        content = content,
        createdAt = createdAt
    )
}