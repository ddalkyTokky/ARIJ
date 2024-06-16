package com.arij.ajir.domain.team.repository

import com.arij.ajir.domain.issue.model.QIssue
import com.arij.ajir.domain.member.model.QMember
import com.arij.ajir.domain.team.model.QTeam
import com.arij.ajir.domain.team.model.Team
import com.arij.ajir.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class QueryDslTeamRepositoryImpl: QueryDslSupport() {

    private val team = QTeam.team
    private val issue = QIssue.issue
    private val members = QMember.member

    fun findAllTeam(): List<Team>{

        val teams = queryFactory
            .selectFrom(team)
            .leftJoin(team.issues, issue).fetchJoin()
            .fetch()

        fetchMembers(teams)

        return teams

    }

    private fun fetchMembers(teams: List<Team>) {
        val teamIds = teams.map { it.id }

        val teamMembers = queryFactory
            .selectFrom(members)
            .leftJoin(members.team, team)
            .where(team.id.`in`(teamIds))
            .orderBy(members.role.desc())
            .fetch()

        teams.forEach { team ->
            team.members = teamMembers.filter { it.team?.id == team.id }.toMutableList()
        }
    }
}