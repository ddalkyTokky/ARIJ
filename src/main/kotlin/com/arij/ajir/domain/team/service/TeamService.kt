package com.arij.ajir.domain.team.service

import com.arij.ajir.common.dto.UserProfileDto
import com.arij.ajir.common.exception.DuplicateArgumentException
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.common.exception.NotAuthorityException
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.domain.member.service.MemberService
import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.model.Team
import com.arij.ajir.domain.team.repository.TeamRepository
import jakarta.validation.constraints.Email
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TeamService(
    private val teamRepository: TeamRepository,
    private val memberRepository: MemberRepository
) {

    fun createTeams(teamRequest: TeamRequest, userProfileDto: UserProfileDto): TeamResponse {

        if(teamRepository.existsByName(teamRequest.name)) throw DuplicateArgumentException("Team", teamRequest.name)
        val teamResult = teamRepository.save(
            Team(
                name = teamRequest.name,
            )
        )
        val leader = memberRepository.findByEmail(userProfileDto.email)

        teamResult.let { leader?.giveTeam(it) }

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), 1, mutableListOf(leader!!))
    }

    @Transactional(readOnly = true)
    fun getTeamList(name: String, userProfileDto: UserProfileDto): List<TeamResponse> {

        if(userProfileDto.role == Role.ADMIN.name) throw NotAuthorityException("권한이 없습니다", userProfileDto.role)

        val teamResult = teamRepository.findAll()
        //TODO("name 에 특정 값이 들어올 경우 들어 온 값으로 Team Repository 에서 필터링 후에 조회")

        return teamResult.map{ TeamResponse.from(it, it.getIssuesSize(), it.getMembersSize(), null) }
    }

    @Transactional(readOnly = true)
    fun getTeamById(teamId: Long, userProfileDto: UserProfileDto): TeamResponse {

        if(userProfileDto.role == Role.USER.name || userProfileDto.role == Role.LEADER.name){
            val member = memberRepository.findByEmail(userProfileDto.email)
            if(member?.team?.id != teamId) throw IllegalArgumentException("다른 팀을 선택 하셨습니다 사용 권한이 업습니다")
        }

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest, userProfileDto: UserProfileDto): TeamResponse {

        if(userProfileDto.role == Role.USER.name || userProfileDto.role == Role.LEADER.name){
            val member = memberRepository.findByEmail(userProfileDto.email)
            if(member?.team?.id != teamId) throw IllegalArgumentException("다른 팀을 선택 하셨습니다 사용 권한이 업습니다")
        }

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        teamResult.name = teamRequest.name

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun deleteTeamById(teamId: Long, userProfileDto: UserProfileDto) {
        when(userProfileDto.role) {
            Role.LEADER.name ->{
                val member = memberRepository.findByEmail(userProfileDto.email)
                if(member?.team?.id != teamId) throw IllegalArgumentException("다른 팀을 선택 하셨습니다 사용 권한이 없습니다")
            }
            Role.USER.name -> throw NotAuthorityException("사용 권한이 없습니다", userProfileDto.role)
        }

        if(teamId == 1L) throw IllegalArgumentException("기본 팀은 선택할 수 없습니다")

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())
        val dummyTeam = teamRepository.findByIdOrNull(1L)!!

        teamResult.members.map{ it.team = dummyTeam }

        teamRepository.delete(teamResult)
    }

    fun inviteMember(memberId: Long, userProfileDto: UserProfileDto): TeamResponse {
        if(userProfileDto.role != Role.LEADER.name) throw NotAuthorityException("해당 맴버는 리더가 아닙니다", userProfileDto.role)

        val leader = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", memberId.toString())
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())

        when(member.team!!.id){
            1L -> member.team = leader.team
            leader.team!!.id -> throw DuplicateArgumentException("Team", leader.team!!.id.toString())
            else -> throw IllegalArgumentException("해당 맴버는 다른 팀에 소속이 되어 있습니다")
        }
        val teamResult = teamRepository.findByIdOrNull(leader.team!!.id) ?: throw ModelNotFoundException("Team", leader.team!!.id.toString())

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun firedMember(memberId: Long, userProfileDto: UserProfileDto):TeamResponse{
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("소속 팀의 리더나 관리자 가 아닐 경우 throw NotAuthenticatedException")
        //TODO("memberId 를 조회 시 없을 경우 throw ModelNotFoundException")
        //TODO("memberId 의 정보를 가져 왔을 때 팀의 id가 authentication 에서 접근 사용자의 TeamId 와 다를 경우 throw illegalArgumentException")
        val leader = memberRepository.findByIdOrNull(6L) ?: throw ModelNotFoundException("Member", memberId.toString())
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())

        when(member.team!!.id){
            leader.team!!.id -> member.team = teamRepository.findByIdOrNull(1L) ?: throw ModelNotFoundException("Team", "1L")
            else -> throw IllegalArgumentException("다른 팀입니다.")
        }

        val teamResult = teamRepository.findByIdOrNull(leader.team!!.id) ?: throw ModelNotFoundException("Team", leader.team!!.id.toString())
        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }


}
