package com.arij.ajir.domain.team.controller

import com.arij.ajir.common.dto.UserProfileDto
import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.service.TeamService
import com.arij.ajir.infra.security.UserPrincipal
import com.arij.ajir.infra.security.jwt.JwtPlugin
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamService: TeamService,
    private val jwtPlugin: JwtPlugin
){

    @PostMapping
    fun createTeams(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody teamRequest: TeamRequest,
    ): ResponseEntity<TeamResponse> {


        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeams(teamRequest, UserProfileDto.from(userPrincipal) ))
    }

    @GetMapping
    fun getTeamList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam name: String
    ): ResponseEntity<List<TeamResponse>>{


        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamList(name, userPrincipal))
    }

    @GetMapping("/{teamId}")
    fun getTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable teamId: Long
    ): ResponseEntity<TeamResponse>{


        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamById(teamId, UserProfileDto.from(userPrincipal)))
    }

    @PutMapping("/{teamId}")
    fun updateTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable teamId: Long,
        @Valid @RequestBody teamRequest: TeamRequest
    ): ResponseEntity<TeamResponse>{


        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeamById(teamId, teamRequest, UserProfileDto.from(userPrincipal)))
    }

    @DeleteMapping("/{teamId}")
    fun deleteTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable teamId: Long,
    ): ResponseEntity<Unit>{


        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            teamService.deleteTeamById(teamId, UserProfileDto.from(userPrincipal)))
    }

    @PatchMapping("/mates/{memberId}")
    fun inviteMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        return ResponseEntity.status(HttpStatus.OK).body(teamService.inviteMember(memberId, UserProfileDto.from(userPrincipal)))
    }

    @DeleteMapping("/mates/{memberId}")
    fun firedMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(teamService.firedMember(memberId, UserProfileDto.from(userPrincipal)))
    }


}