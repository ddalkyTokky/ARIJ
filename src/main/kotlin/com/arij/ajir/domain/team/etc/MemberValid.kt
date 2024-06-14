package com.arij.ajir.domain.team.etc

import com.arij.ajir.common.exception.NotAuthorityException
import com.arij.ajir.domain.member.model.Member
import com.arij.ajir.domain.member.model.Role

class MemberValid{
    companion object {
        fun validNotAdmin(userProfile: Member, teamId: Long){
                if(userProfile.role != Role.ADMIN){
                    if(userProfile.team?.id != teamId) throw IllegalArgumentException("다른 팀을 선택 하셨습니다 사용 권한이 없습니다")
                }
        }

        fun validNotLeader(userProfile: Member, teamId: Long){
            when(userProfile.role) {
                Role.LEADER ->{
                    if(userProfile.team?.id != teamId) throw IllegalArgumentException("다른 팀을 선택 하셨습니다 사용 권한이 없습니다")
                }
                Role.USER -> throw NotAuthorityException("사용 권한이 없습니다", userProfile.role.name)
                else -> throw IllegalArgumentException("잘못된 접근 입니다")
            }
        }

        fun validNotLeaderOrAdmin(userProfile: Member){
            if(userProfile.role == Role.USER) throw NotAuthorityException("리더와 관리자만 접근이 가능 합니다", userProfile.role.name)
        }

        fun validRole(userProfile: Member, role: Role, errorMsg: String){
            if(userProfile.role != role) throw NotAuthorityException(errorMsg, userProfile.role.name)
        }
    }

}

