package com.arij.ajir.domain.team.service

import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import org.springframework.stereotype.Service

@Service
class TeamService {

    fun createTeams(teamRequest: TeamRequest): TeamResponse {
        TODO()
    }

    fun getTeamList(name: String): List<TeamResponse> {
        TODO()
    }

    fun getTeamById(teamId: Long): TeamResponse {
        TODO()
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest): TeamResponse {
        TODO()
    }

    fun deleteTeamById(teamId: Long) {
        TODO()
    }

    fun inviteMember(memberId: Long): TeamResponse {
        TODO()
    }

    fun firedMember(memberId: Long) {
        TODO()
    }


}
