package com.arij.ajir.domain.team.service

import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import com.arij.ajir.domain.team.entity.Team
import com.arij.ajir.domain.team.repository.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: TeamRepository
) {

    fun createTeams(teamRequest: TeamRequest, /*userId : Long*/): TeamResponse {
        //TODO("Team Repository 에서 teamRequest.name 과 같은 이름이 있을 경우 throw DuplicationArgumentException")
        if(teamRepository.existsName(teamRequest.name)) throw DuplicationArgumentException("중복 되는 팀 이름이 있습니다")
        //TODO("Team Repository.save 로 팀 생성")
        val teamResult = teamRepository.save(
            Team.createTeam(teamRequest.name)
        )
        //TODO("Team 생성 사용자 -> 리더로 권한 변경")
        //TODO("이슈와 맴버의 개수를 세는 로직 작성")
        val members = memberRepository.findAllByTeamId(teamResult.id)
        //TODO("TeamResponse 에 작성된 팀 정보 반환")
        return TeamResponse.from(teamResult, Team.getIssuesSize(), Team.getMembersSize(), members)
    }

    fun getTeamList(name: String, /*userId : Long*/): List<TeamResponse> {
        //TODO("authentication 에서 접근 사용자가 관리자 인지 확인")
        val teamResult = teamRepository.findAll()
        //TODO("name 에 특정 값이 들어올 경우 들어 온 값으로 Team Repository 에서 필터링 후에 조회")

        return teamResult.map{ TeamResponse.from(it, Team.getIssuesSize(), Team.getMembersSize(), null) }
    }

    fun getTeamById(teamId: Long, /*userId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")
        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("존재 하지 않는 팀 입니다")

        val members = memberRepository.findAllByTeamId(teamResult.id)

        return TeamResponse.from(teamResult, Team.getIssuesSize(), Team.getMembersSize(), members)
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest, /*userId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")
        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("존재 하지 않는 팀 입니다")

        teamResult.name = teamRequest.name

        val members = memberRepository.findAllByTeamId(teamResult.id)

        return TeamResponse.from(teamResult, Team.getIssuesSize(), Team.getMembersSize(), members)
    }

    fun deleteTeamById(teamId: Long, /*userId : Long*/) {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자 일 경우 소속팀 여부와 상관 없이 모두 삭제 가능")
        //TODO("권한이 사용자 일 경우 throw NotAuthenticatedException")
        val teamResult = teamRepository.findByIdOrNull(teamId) ?: throw ModelNotFoundException("존재 하지 않는 팀 입니다")

        return teamRepository.delete(teamResult)
    }

    fun inviteMember(memberId: Long, /*userId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("소속 팀의 리더가 아닐 경우 throw NotAuthenticatedException")
        //TODO("memberId 의 정보를 가져 왔을 때 팀의 id가 1이 아닌 다른 값을 가질 때 throw illegalArgumentException")
        //TODO("memberId 의 정보를 가져 왔을 때 팀의 id가 authentication 에서 접근 사용자의 teamId 와 같을 경우 throw DuplicationArgumentException")
        //TODO("memberId 를 조회 시 없을 경우 throw ModelNotFoundException")
        TODO("member 의 팀 Id 를 authentication 에서 접근 사용자의 Id와 매칭")
    }

    fun firedMember(memberId: Long, /*userId : Long*/) {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("소속 팀의 리더나 관리자 가 아닐 경우 throw NotAuthenticatedException")
        //TODO("memberId 를 조회 시 없을 경우 throw ModelNotFoundException")
        //TODO("memberId 의 정보를 가져 왔을 때 팀의 id가 authentication 에서 접근 사용자의 TeamId 와 다를 경우 throw illegalArgumentException")

        TODO("member 의 팀 Id를 1로 변경")
    }


}
