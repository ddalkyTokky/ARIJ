package com.arij.ajir.domain.team.dto

data class TeamResponse(
    val teamId: Long,
    val issueCounts: Long,
    val memberCounts: Long,
    val teamName : String,
    val members : MutableList<Unit>
)
