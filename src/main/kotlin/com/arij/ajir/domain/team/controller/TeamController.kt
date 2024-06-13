package com.arij.ajir.domain.team.controller

import com.arij.ajir.common.dto.UserProfileDto
import com.arij.ajir.common.exception.InvalidCredentialException
import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.service.TeamService
import com.arij.ajir.infra.security.UserPrincipal
import com.arij.ajir.infra.security.jwt.JwtPlugin
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @Valid @RequestBody teamRequest: TeamRequest,
    ): ResponseEntity<TeamResponse> {

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")

        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeams(teamRequest, UserProfileDto.from(userPrincipal) ))
    }

    @GetMapping
    fun getTeamList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestParam name: String?,
    ): ResponseEntity<List<TeamResponse>>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")

        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamList(name, UserProfileDto.from(userPrincipal)))
    }

    @GetMapping("/{teamId}")
    fun getTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable teamId: Long
    ): ResponseEntity<TeamResponse>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")


        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamById(teamId, UserProfileDto.from(userPrincipal)))
    }

    @PutMapping("/{teamId}")
    fun updateTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable teamId: Long,
        @Valid @RequestBody teamRequest: TeamRequest
    ): ResponseEntity<TeamResponse>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")

        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeamById(teamId, teamRequest, UserProfileDto.from(userPrincipal)))
    }

    @DeleteMapping("/{teamId}")
    fun deleteTeamById(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable teamId: Long,
    ): ResponseEntity<Unit>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")


        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            teamService.deleteTeamById(teamId, UserProfileDto.from(userPrincipal)))
    }

    @PatchMapping("/mates/{memberId}")
    fun inviteMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")

        return ResponseEntity.status(HttpStatus.OK).body(teamService.inviteMember(memberId, UserProfileDto.from(userPrincipal)))
    }

    @DeleteMapping("/mates/{memberId}")
    fun firedMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{

        if (userPrincipal == null) throw InvalidCredentialException("로그인을 해 주세요")

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(teamService.firedMember(memberId, UserProfileDto.from(userPrincipal)))
    }


}