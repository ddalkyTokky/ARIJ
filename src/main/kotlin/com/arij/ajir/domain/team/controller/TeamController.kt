package com.arij.ajir.domain.team.controller

import com.arij.ajir.common.exception.TokenException
import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.service.TeamService
import com.arij.ajir.infra.security.jwt.JwtPlugin
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamService: TeamService,
    private val jwtPlugin: JwtPlugin
){

    @PostMapping
    fun createTeams(
        @RequestHeader httpHeaders: HttpHeaders,
        @Valid @RequestBody teamRequest: TeamRequest
    ): ResponseEntity<TeamResponse> {
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeams(teamRequest, email))
    }

    @GetMapping
    fun getTeamList(
        @RequestHeader httpHeaders: HttpHeaders,
        @RequestParam name: String
    ): ResponseEntity<List<TeamResponse>>{

        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamList(name, email))
    }

    @GetMapping("/{teamId}")
    fun getTeamById(
        @RequestHeader httpHeaders: HttpHeaders,
        @PathVariable teamId: Long
    ): ResponseEntity<TeamResponse>{
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamById(teamId, email))
    }

    @PutMapping("/{teamId}")
    fun updateTeamById(
        @RequestHeader httpHeaders: HttpHeaders,
        @PathVariable teamId: Long,
        @Valid @RequestBody teamRequest: TeamRequest
    ): ResponseEntity<TeamResponse>{
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeamById(teamId, teamRequest, email))
    }

    @DeleteMapping("/{teamId}")
    fun deleteTeamById(
        @RequestHeader httpHeaders: HttpHeaders,
        @PathVariable teamId: Long,
    ): ResponseEntity<Unit>{
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            teamService.deleteTeamById(teamId, email))
    }

    @PatchMapping("/mates/{memberId}")
    fun inviteMember(
        @RequestHeader httpHeaders: HttpHeaders,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.OK).body(teamService.inviteMember(memberId, email))
    }

    @DeleteMapping("/mates/{memberId}")
    fun firedMember(
        @RequestHeader httpHeaders: HttpHeaders,
        @PathVariable memberId: Long,
    ): ResponseEntity<TeamResponse>{
        val token: String = httpHeaders.get("Authorization")?.get(0) ?: throw TokenException("No Token Found")
        val email: String = jwtPlugin.validateToken(token).getOrNull()?.payload?.get("email").toString()

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(teamService.firedMember(memberId, email))
    }


}