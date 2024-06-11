package com.arij.ajir.domain.team.controller

import com.arij.ajir.domain.team.dto.TeamResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamService: TeamService
){

    @PostMapping
    fun createTeams(
        @RequestBody name : String
    ): ResponseEntity<TeamResponse> {

        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeams(name))
    }

    @GetMapping
    fun getTeamList(
        @RequestParam name: String
    ): ResponseEntity<List<TeamResponse>>{

        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamList(name))
    }

    @GetMapping("/{teamId}")
    fun getTeamById(
        @PathVariable teamId: Long
    ): ResponseEntity<TeamResponse>{

        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamById(teamId))
    }

    @PutMapping("/{teamId}")
    fun updateTeamById(
        @PathVariable teamId: Long,
        @RequestBody name: String
    ): ResponseEntity<TeamResponse>{

        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeamById(teamId, name))
    }

    @DeleteMapping("/{teamId}")
    fun deleteTeamById(
        @PathVariable teamId: Long,
    ): ResponseEntity<Unit>{

        teamService.deleteTeamById(teamId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PatchMapping("/{memberId}")
    fun inviteMember(
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        return ResponseEntity.status(HttpStatus.OK).body(teamService.inviteMember(memberId))
    }

    @DeleteMapping("/{memberId}")
    fun firedMember(
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        teamService.firedMember(memberId)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}