package com.arij.ajir.domain.team.service

import com.arij.ajir.domain.team.dto.TeamRequest
import com.arij.ajir.domain.team.dto.TeamResponse
import org.springframework.stereotype.Service

@Service
class TeamService {

    fun createTeams(teamRequest: TeamRequest): TeamResponse {
        //TODO("Team Repository 에서 teamRequest.name 과 같은 이름이 있을 경우 throw DuplicationArgumentException")
        //TODO("Team Repository.save 로 팀 생성")
        //TODO("Team 생성 사용자 -> 리더로 권한 변경")
        TODO("TeamResponse 에 작성된 팀 정보 반환")
    }

    fun getTeamList(name: String, /*userId : Long*/): List<TeamResponse> {
        //TODO("authentication 에서 접근 사용자가 관리자 인지 확인")
        //TODO("name 에 아무 값도 들어 오지 않을 경우 전체 쿼리 실행 해서 값 반환")
        //TODO("name 에 특정 값이 들어올 경우 들어 온 값으로 Team Repository 에서 필터링 후에 조회")
        TODO("return 시에 members 에 빈 배열을 넣은 후에 반환")
    }

    fun getTeamById(teamId: Long, /*userId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")
        //TODO("TeamRepository 에서 Team 조회 시 없을 경우 throw ModelNotFoundException")
        TODO("TeamResponse 를 반환")
    }

    fun updateTeamById(teamId: Long, teamRequest: TeamRequest, /*userId : Long*/): TeamResponse {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 사용자 와 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자일 경우 소속팀 여부와 상관 없이 모두 조회 가능")
        //TODO("TeamRepository 에서 Team 조회 시 없을 경우 throw ModelNotFoundException")
        //TODO("조회된 팀의 name 을 update")
        TODO("update 된 TeamResponse 를 반환")
    }

    fun deleteTeamById(teamId: Long, /*userId : Long*/) {
        //TODO("authentication 에서 접근 사용자의 권한 확인")
        //TODO("권한이 리더 일 경우 teamId 가 authentication 에서 teamName 을 비교 후에 일치 하지 않으면 throw illegalArgumentException")
        //TODO("권한이 관리자 일 경우 소속팀 여부와 상관 없이 모두 삭제 가능")
        //TODO("권한이 사용자 일 경우 throw NotAuthenticatedException")
        //TODO("TeamRepository 에서 Team 조회 시 없을 경우 throw ModelNotFoundException")
        TODO("받은 팀을 delete 로 삭제")
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
