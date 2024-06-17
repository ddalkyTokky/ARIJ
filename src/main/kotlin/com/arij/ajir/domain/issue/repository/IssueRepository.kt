package com.arij.ajir.domain.issue.repository

import com.arij.ajir.domain.issue.model.Issue
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IssueRepository : JpaRepository<Issue, Long>, CustomIssueRepository {
    fun findIssueByIdAndDeletedIsFalse(id: Long): Optional<Issue>
}