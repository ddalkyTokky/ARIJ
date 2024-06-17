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
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.domain.Pageable

@Repository
class IssueRepositoryImpl : QueryDslSupport(), CustomIssueRepository {

    private val issue = QIssue.issue


    override fun searchIssues(topic: String?, keyword: String?, orderBy: String, ascend: Boolean): List<Issue> {

        val keywordBuilder = BooleanBuilder().and(issue.deleted.eq(false))

        if (topic != null) {

            if (keyword == null)
                throw IllegalArgumentException("검색어를 입력해 주세요")

            val word = keyword.toUpperCase(Locale.getDefault())

            when (topic) {
                "title" -> keywordBuilder.and(issue.title.containsIgnoreCase(word))
                "content" -> keywordBuilder.and(issue.content.containsIgnoreCase(word))
                "author" -> keywordBuilder.and(issue.member.nickname.containsIgnoreCase(word))
                "priority" -> {
                    if (Priority.NORMAL.name == word)
                        keywordBuilder.and(issue.priority.eq(Priority.NORMAL))

                    else if (Priority.URGENT.name == word)
                        keywordBuilder.and(issue.priority.eq(Priority.URGENT))

                    else if (Priority.EMERGENCY.name == word)
                        keywordBuilder.and(issue.priority.eq(Priority.EMERGENCY))

                    else throw IllegalArgumentException("Unknown Priority $word")
                }

                "working" -> {
                    if (WorkingStatus.TODO.name == word)
                        keywordBuilder.and(issue.workingStatus.eq(WorkingStatus.TODO))
                    else if (WorkingStatus.INPROGRESS.name == word)
                        keywordBuilder.and(issue.workingStatus.eq(WorkingStatus.INPROGRESS))
                    else if (WorkingStatus.DONE.name == word)
                        keywordBuilder.and(issue.workingStatus.eq(WorkingStatus.DONE))
                    else throw IllegalArgumentException("Unknown Working Status $word")
                }

                "team" -> keywordBuilder.and(issue.team.name.containsIgnoreCase(word))
            }
        }

        if(orderBy == topic) throw IllegalArgumentException("정렬기준이랑 검색 기준이랑 동일할 수 없다.")

        val query = queryFactory.select(issue).from(issue).where(keywordBuilder)

//        when (orderBy) {
//            "author" -> {
//                if (ascend)
//                    query.orderBy(issue.member.nickname.asc())
//                else
//                    query.orderBy(issue.member.nickname.desc())
//            }
//
//            "priority" -> {
//                if (ascend)
//                    query.orderBy(issue.priority.asc())
//                else
//                    query.orderBy(issue.priority.desc())
//            }
//
//            "working" -> {
//                if (ascend)
//                    query.orderBy(issue.workingStatus.asc())
//                else
//                    query.orderBy(issue.workingStatus.desc())
//            }
//
//            else -> {
//                if (ascend)
//                    query.orderBy(issue.team.name.asc())
//                else
//                    query.orderBy(issue.createdAt.desc())
//            }
//        }
        /*
        * orderby에 와도 되는 거 issue에 property들
        * id, member, team, createAt, ....
        * */
        query.orderBy(test(ascend, issue,orderBy ))


        return query.fetch()
    }

    private fun test(ascend: Boolean, path: EntityPathBase<*>, str: String): OrderSpecifier<*> {
        val pathBuilder = PathBuilder(path.type, path.metadata)


        return OrderSpecifier(
            if(ascend) Order.ASC else Order.DESC,
            pathBuilder.get(str) as Expression<Comparable<*>>,
        )
    }

    override fun searchIssueListByTitle(title: String): List<Issue> {
        return queryFactory.select(issue).from(issue).where(issue.title.containsIgnoreCase(title)).fetch()
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