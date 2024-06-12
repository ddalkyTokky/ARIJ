package com.arij.ajir.domain.comment.model

import com.arij.ajir.domain.comment.dto.CommentResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "content", nullable = false)
    var content: String? = null

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "issue_id", nullable = false)
    var issueId: Long? = null

    @Column(name = "member_id", nullable = false)
    var memberId: Long? = null


    companion object {
        fun createComment(content: String, issueId: Long, memberId: Long ) : Comment {
            val comment = Comment()
            comment.content = content
            comment.issueId = issueId
            comment.memberId = memberId

            return comment
        }
    }

}

fun Comment.toResponse() : CommentResponse {
    return CommentResponse(
        commentId = id!!,
        author = "${issueId!!}의 닉네임",
        content =content!!,
        createdAt = createdAt
    )
}