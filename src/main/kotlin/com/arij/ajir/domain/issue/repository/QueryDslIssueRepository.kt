package com.arij.ajir.domain.issue.repository

import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.model.QIssue
import com.arij.ajir.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.stereotype.Repository


@Repository
class QueryDslIssueRepository: QueryDslSupport() {

    private val issue = QIssue.issue

    fun searchTopicAndKeyword(topic: String, keyword: String):List<Issue> {

        val builder = BooleanBuilder()

        val columnMap = mapOf(
            "title" to QIssue.issue.title,
            "content" to QIssue.issue.content,
            "author" to QIssue.issue.member.nickname,
            "priority" to QIssue.issue.priority,
            "working_status" to QIssue.issue.workingStatus.stringValue(),
        )

        val column = columnMap[topic]

       when(columnMap[topic]) {
           QIssue.issue.title -> column.let{ builder.and(issue.title.contains(keyword)) }
           QIssue.issue.content -> column.let{ builder.and(issue.content.contains(keyword)) }
           QIssue.issue.member.nickname -> column.let{ builder.and(issue.member.nickname.contains(keyword)) }
           QIssue.issue.priority -> column.let{ builder.and(issue.priority.stringValue().contains(keyword)) }
           QIssue.issue.workingStatus -> column.let{ builder.and(issue.workingStatus.stringValue().contains(keyword)) }
       }


        return queryFactory
            .selectFrom(issue)
            .where(builder)
            .fetch()
    }
}