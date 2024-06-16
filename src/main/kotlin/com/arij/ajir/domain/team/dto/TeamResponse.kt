package com.arij.ajir.domain.team.dto

import com.arij.ajir.domain.issue.dto.IssueResponse
import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.member.dto.MemberResponse
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.team.model.Team

data class TeamResponse(
    val teamId: Long,
    val issueCounts: Long,
    val memberCounts: Long,
    val teamName : String,
    val issues: List<IssueResponse>,
    val members : List<MemberResponse>
){
    companion object {
        fun from(team: Team, issues: List<Issue>, members: List<Member>):TeamResponse {
            return TeamResponse(
                teamId = team.id!!,
                teamName = team.name,
                issueCounts = team.issues.size.toLong(),
                memberCounts = team.members.size.toLong(),
                issues = issues.map { it.toResponse() },
                members = members.map { it.toResponse() }
            )
        }

    }
}