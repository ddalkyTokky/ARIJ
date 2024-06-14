package com.arij.ajir.domain.team.service

import com.arij.ajir.common.dto.UserProfileDto
import com.arij.ajir.common.exception.DuplicateArgumentException
import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.domain.member.model.Role
import com.arij.ajir.domain.member.repository.MemberRepository
import com.arij.ajir.domain.team.dto.TeamIdRequest
import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.etc.MemberValid
import com.arij.ajir.domain.team.model.Team
import com.arij.ajir.domain.team.repository.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TeamService(
    private val teamRepository: TeamRepository,
    private val memberRepository: MemberRepository
) {

    fun createTeams(teamRequest: TeamRequest, userProfileDto: UserProfileDto): String {

        if(teamRepository.existsByName(teamRequest.name)) throw DuplicateArgumentException("Team", teamRequest.name)
        val teamResult = teamRepository.save(
            Team(
                name = teamRequest.name,
            )
        )
        val leader = memberRepository.findByEmail(userProfileDto.email)

        teamResult.let { leader?.giveTeam(it) }

        return "TeamId : ${teamResult.id}"
    }

    @Transactional(readOnly = true)
    fun getTeamList(name: String?, userProfileDto: UserProfileDto): List<TeamResponse> {

        val userProfile = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", userProfileDto.email)

        MemberValid.validRole(userProfile, Role.ADMIN, "권한이 없습니다")


        val teamResult:List<Team> = if(name == null || name == ""){
          teamRepository.findAll()
        }else{
          teamRepository.findByName(name)
        }

        //TODO("name 에 특정 값이 들어올 경우 들어 온 값으로 Team Repository 에서 필터링 후에 조회")

        


        return teamResult.map{ TeamResponse.from(it, it.members ) }
    }

    @Transactional(readOnly = true)
    fun getTeamById(teamId: Long, userProfileDto: UserProfileDto): TeamResponse {

        val userProfile = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", userProfileDto.email)

        MemberValid.validNotAdmin(userProfile, teamId)

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        return TeamResponse.from(teamResult, teamResult.members)
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest, userProfileDto: UserProfileDto) {

        val userProfile = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", userProfileDto.email)

        MemberValid.validNotAdmin(userProfile, teamId)

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        teamResult.name = teamRequest.name

    }

    fun deleteTeamById(teamId: Long, userProfileDto: UserProfileDto) {

        val userProfile = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", userProfileDto.email)

        if(userProfile.role != Role.ADMIN) MemberValid.validNotLeader(userProfile, teamId)

        if(teamId == 1L) throw IllegalArgumentException("기본 팀은 선택할 수 없습니다")

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())
        val dummyTeam = teamRepository.findByIdOrNull(1L)!!

        teamResult.members.map{
            it.team = dummyTeam
            if (it.role == Role.LEADER) {
                it.fireTeam(teamResult)
            }
        }

        teamRepository.delete(teamResult)
    }

    fun inviteMember(memberId: Long, userProfileDto: UserProfileDto) {
        val leader = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", memberId.toString())

        MemberValid.validRole(leader, Role.LEADER, "해당 맴버는 리더가 아닙니다")

        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())

        when(member.team!!.id){
            1L -> member.team = leader.team
            leader.team!!.id -> throw DuplicateArgumentException("Team", leader.team!!.id.toString())
            else -> throw IllegalArgumentException("해당 맴버는 다른 팀에 소속이 되어 있습니다")
        }

    }

    fun inviteMemberByAdmin(memberId: Long, teamIdRequest: TeamIdRequest, userProfileDto: UserProfileDto){

        val userProfile = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", userProfileDto.email)

        MemberValid.validRole(userProfile, Role.ADMIN, "권한이 없습니다")

        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())
        val team = teamRepository.findByIdOrNull(teamIdRequest.teamId) ?: throw ModelNotFoundException("Team", teamIdRequest.teamId.toString())

        when(member.team!!.id){
            team.id -> throw DuplicateArgumentException("Team", team.id.toString())
            else -> member.team = team
        }
    }

    fun firedMember(memberId: Long, userProfileDto: UserProfileDto){
        val leader = memberRepository.findByEmail(userProfileDto.email) ?: throw ModelNotFoundException("Member", memberId.toString())

        MemberValid.validNotLeaderOrAdmin(leader)

        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())

        if(member.id == leader.id) throw IllegalArgumentException("나 자신은 탈퇴 시킬 수 없습 니다")

        when(member.team!!.id){
            leader.team!!.id -> member.team = teamRepository.findByIdOrNull(1L) ?: throw ModelNotFoundException("Team", "1L")
            else -> throw IllegalArgumentException("다른 팀입니다.")
        }

    }




}
