package com.arij.ajir.domain.issue.model

import com.arij.ajir.domain.comment.model.Comment
import com.arij.ajir.domain.comment.model.toResponse
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.team.model.Team
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.sql.Timestamp
import java.time.Instant

@Entity
@Table(name = "Issue")
class Issue(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: Timestamp,

    @Column(name ="deleted", nullable = false)
    var deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var priority: Priority,

    @Column(name = "category", nullable = false)
    var category: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "deleted_at")
    var deletedAt: Timestamp? = null

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
    val comments: MutableList<Comment> = mutableListOf()

    companion object {
        fun createIssue(
            issueCreateRequest: IssueCreateRequest,
            member: Member,
            team: Team,
        ): Issue {
            return Issue(
                member = member,
                team = team,
                title = issueCreateRequest.title,
                content = issueCreateRequest.content,
                createdAt = Timestamp.from(Instant.now()),
                deleted = false,
                priority = issueCreateRequest.priority,
                category = issueCreateRequest.category,
            )
        }
    }

    fun update(
        issueUpdateRequest: IssueUpdateRequest,
    ): Issue {
        this.title = issueUpdateRequest.title
        this.content = issueUpdateRequest.content
        this.category = issueUpdateRequest.category
        return this
    }

    fun delete() {
        deletedAt = Timestamp.from(Instant.now())
        deleted = true
    }

    fun toResponse(): IssueResponse {
        return IssueResponse(
            id = id!!,
            title = title,
            author = member.nickname!!,
            teamName = team.name,
            createdAt = createdAt,
            content = content,
            priority = priority,
            category = category,
            deleted = deleted,
        )
    }

    fun toResponseWithCommentResponse(): IssueResponseWithCommentResponse {
        return IssueResponseWithCommentResponse(
            id = id!!,
            title = title,
            author = member.nickname!!,
            teamName = team.name,
            createdAt = createdAt,
            content = content,
            priority = priority,
            category = category,
            deleted = deleted,
            comments = comments.map { it.toResponse() },
        )
    }

    fun toIdResponse(): IssueIdResponse {
        return IssueIdResponse(
            id = id!!,
        )
    }
}