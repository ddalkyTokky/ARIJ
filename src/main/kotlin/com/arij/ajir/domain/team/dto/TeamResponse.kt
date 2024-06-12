package com.arij.ajir.domain.team.dto

import com.arij.ajir.domain.team.entity.Team

data class TeamResponse(
    val teamId: Long,
    val issueCounts: Long,
    val memberCounts: Long,
    val teamName : String,
    val members : MutableList<Unit>
){
    companion object {
        fun from(team: Team, issueCounts: Long, memberCounts: Long, members: MutableList<Unit>?):TeamResponse {
            return TeamResponse(
                teamId = team.id!!,
                teamName = team.name,
                issueCounts = issueCounts,
                memberCounts = memberCounts,
                members = members ?: mutableListOf()
            )
        }

    }
}