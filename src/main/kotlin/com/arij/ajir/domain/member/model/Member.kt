package com.arij.ajir.domain.member.model

import com.arij.ajir.common.exception.ModelNotFoundException
import com.arij.ajir.common.exception.ModelNotSavedException
import com.arij.ajir.domain.member.dto.MemberResponse
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType


@Entity
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "ro", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var role: Role = Role.USER

    @Column(nullable = false, unique = true, length = 100)
    var email: String? = null

    @Column(nullable = false, length = 64)
    var password: String? = null

    @Column(nullable = false, unique = true, length = 20)
    var nickname: String? = null

    companion object {
        fun createMember(
            team: Team,
            email: String,
            password: String,
            nickname: String
        ): Member {
            val member: Member = Member()
            member.team = team
            member.email = email
            member.password = password
            member.nickname = nickname
            member.role = Role.USER
            return member
        }
    }

    fun updateNickname(
        nickname: String
    ): Member {
        this.nickname = nickname
        return this
    }

    fun toResponse(): MemberResponse {
        return MemberResponse(
            memberId = this.id ?: throw ModelNotSavedException("Member"),
            teamName = this.team.name,
            email = this.email!!,
            nickname = this.nickname!!
        )
    }
}