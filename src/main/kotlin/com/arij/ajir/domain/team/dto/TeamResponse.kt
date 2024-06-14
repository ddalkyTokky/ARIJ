package com.arij.ajir.domain.team.dto

import com.arij.ajir.domain.member.dto.MemberResponse
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.team.model.Team

data class TeamResponse(
    val teamId: Long,
    val issueCounts: Long,
    val memberCounts: Long,
    val teamName : String,
    val members : List<MemberResponse>
){
    companion object {
        fun from(team: Team, members: List<Member>):TeamResponse {
            return TeamResponse(
                teamId = team.id!!,
                teamName = team.name,
                issueCounts = team.issues.size.toLong(),
                memberCounts = team.members.size.toLong(),
                members = members.map { it.toResponse() }
            )
        }

    }
}