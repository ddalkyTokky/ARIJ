package com.arij.ajir.domain.issue.repository

import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.issue.model.Priority
import com.arij.ajir.domain.issue.model.QIssue
import com.arij.ajir.domain.issue.model.WorkingStatus
import com.arij.ajir.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class IssueRepositoryImpl : QueryDslSupport(), CustomIssueRepository {

    private val issue = QIssue.issue


    override fun searchIssues(
        topic: String?,
        keyword: String?,
        orderBy: String,
        ascend: Boolean,
        teamId: Long
    ): List<Issue> {

        val keywordBuilder = BooleanBuilder().and(issue.deleted.eq(false))

        if (teamId != -1L)
            keywordBuilder.and(issue.team.id.eq(teamId))

        if (topic != null) {

            if (keyword == null)
                throw IllegalArgumentException("검색어를 입력해 주세요")

            val word = keyword.toUpperCase(Locale.getDefault()) // TODO: 테스트 용이성을 위해서 넣어둠

            when (topic) {
                "title" -> keywordBuilder.and(issue.title.containsIgnoreCase(keyword))
                "content" -> keywordBuilder.and(issue.content.containsIgnoreCase(keyword))
                "author" -> keywordBuilder.and(issue.member.nickname.containsIgnoreCase(keyword))
                "priority" -> keywordBuilder.and(issue.priority.eq(Priority.valueOf(keyword)))
                "working" -> keywordBuilder.and(issue.workingStatus.eq(WorkingStatus.valueOf(keyword)))
                "team" -> keywordBuilder.and(issue.team.name.containsIgnoreCase(word))
            }
        }

        if (orderBy == topic) throw IllegalArgumentException("정렬기준이랑 검색 기준이랑 동일할 수 없다.")

        return queryFactory.select(issue).from(issue).where(keywordBuilder).orderBy(test(ascend, issue, orderBy))
            .fetch()
    }

    private fun test(ascend: Boolean, path: EntityPathBase<*>, str: String): OrderSpecifier<*> {
        val pathBuilder = PathBuilder(path.type, path.metadata)


        return OrderSpecifier(
            if (ascend) Order.ASC else Order.DESC,
            pathBuilder.get(str) as Expression<Comparable<*>>,
        )
    }

    // TODO: 지워도 됨
    private fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->
            OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }

    // TODO: 지워도 됨
    override fun findIssue(pageable: Pageable): Page<Issue> {
        val keywordBuilder = BooleanBuilder()

        val query = queryFactory.select(issue).from(issue).where(keywordBuilder)
        // 최종적으로 쿼리 수행
        val contents = queryFactory.selectFrom(issue)
            .where(keywordBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, issue))
            .fetch()

        // Page 구현체 반환
        return PageImpl(contents, pageable, 1)

    }

}