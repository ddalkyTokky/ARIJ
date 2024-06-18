package com.arij.ajir.domain.issue.model

import com.arij.ajir.common.exception.DuplicateArgumentException
import com.arij.ajir.domain.comment.model.Comment
import com.arij.ajir.domain.comment.model.toResponse
import com.arij.ajir.domain.issue.dto.*
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.team.model.Team
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.LocalDateTime

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
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var priority: Priority,

    @Enumerated(EnumType.STRING)
    @Column(name = "working_status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var workingStatus: WorkingStatus,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.REMOVE])
    val comments: MutableList<Comment> = mutableListOf()


    fun updateTitleAndContent(
        issueUpdateRequest: IssueUpdateRequest,
    ) {
        this.title = issueUpdateRequest.title
        this.content = issueUpdateRequest.content

    }

    fun updatePriority(priority: Priority) {
        if (this.priority == priority) throw DuplicateArgumentException("issue", priority.toString())
        this.priority = priority
    }

    fun updateWorkingStatus(workingStatus: WorkingStatus) {
        if (this.workingStatus == workingStatus) throw DuplicateArgumentException("issue", workingStatus.toString())
        this.workingStatus = workingStatus
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
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
            workingStatus = workingStatus,
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
            workingStatus = workingStatus,
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