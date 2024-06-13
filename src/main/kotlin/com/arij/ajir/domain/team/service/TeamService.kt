package com.arij.ajir.domain.team.service

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

    fun createTeams(teamRequest: TeamRequest, /*memberId : Long*/): TeamResponse {
        //TODO("Team Repository 에서 teamRequest.name 과 같은 이름이 있을 경우 throw DuplicateArgumentException")
        if(teamRepository.existsByName(teamRequest.name)) throw DuplicateArgumentException("Team", teamRequest.name)
        //TODO("Team Repository.save 로 팀 생성")
        val teamResult = teamRepository.save(
            Team(
                name = teamRequest.name,
            )
        )
        //TODO("Team 생성 사용자 -> 리더로 권한 변경")
        //TODO("이슈와 맴버의 개수를 세는 로직 작성")
//        val leader = memberService.findById(memberId)
//
//        teamResult.id?.let { leader.giveTeamId(it) }

        //TODO("TeamResponse 에 작성된 팀 정보 반환")
        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    @Transactional(readOnly = true)
    fun getTeamList(name: String, email: String): List<TeamResponse> {
        val admin = memberRepository.findByEmail(email)
        if(admin?.role == Role.ADMIN) throw NotAuthorityException("권한이 없습니다", admin.role.name)

        val teamResult = teamRepository.findAll()
        //TODO("name 에 특정 값이 들어올 경우 들어 온 값으로 Team Repository 에서 필터링 후에 조회")

        return teamResult.map{ TeamResponse.from(it, it.getIssuesSize(), it.getMembersSize(), null) }
    }

    @Transactional(readOnly = true)
    fun getTeamById(teamId: Long, /*memberId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")

        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest, /*memberId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")
        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        teamResult.name = teamRequest.name

        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun deleteTeamById(teamId: Long, /*memberId : Long*/) {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자 일 경우 소속팀 여부와 상관 없이 모두 삭제 가능")
        //TODO("권한이 사용자 일 경우 throw NotAuthenticatedException")
        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("Team", teamId.toString())

        teamRepository.delete(teamResult)
    }

    fun inviteMember(memberId: Long, /*memberId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("소속 팀의 리더가 아닐 경우 throw NotAuthenticatedException")
        val leader = memberRepository.findByIdOrNull(6L) ?: throw ModelNotFoundException("Member", memberId.toString())
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId.toString())

        when(member.team!!.id){
            1L -> member.team = leader.team
            leader.team!!.id -> throw DuplicateArgumentException("Team", leader.team!!.id.toString())
            else -> throw IllegalArgumentException("해당 맴버는 다른 팀에 소속이 되어 있습니다")
        }
        val teamResult = teamRepository.findByIdOrNull(leader.team!!.id) ?: throw ModelNotFoundException("Team", leader.team!!.id.toString())
        return TeamResponse.from(teamResult, teamResult.getIssuesSize(), teamResult.getMembersSize(), teamResult.members)
    }

    fun firedMember(memberId: Long, /*memberId : Long*/):TeamResponse{
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
