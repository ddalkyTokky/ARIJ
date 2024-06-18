package com.arij.ajir.domain.team.dto

import com.arij.ajir.domain.issue.dto.IssueResponse
import com.arij.ajir.domain.member.dto.MemberResponse
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
        fun from(team: Team, isIssue: Boolean):TeamResponse {
            return TeamResponse(
                teamId = team.id!!,
                teamName = team.name,
                issueCounts = team.issues.size.toLong(),
                memberCounts = team.members.size.toLong(),
                issues = if(isIssue) team.issues.map { it.toResponse() } else listOf(),
                members = team.members.map { it.toResponse() }
            )
        }

    }
}