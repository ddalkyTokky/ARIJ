package com.arij.ajir.domain.team.entity

import com.arij.ajir.domain.member.model.Member
import jakarta.persistence.*

@Entity
@Table(name = "team")
class Team(
    @Column(name = "name", nullable = false, unique = true)
    var name: String = "",

        @OneToMany(mappedBy = "issue", orphanRemoval = true)
    val issues: MutableList<Issues> = mutableListOf(),

    @OneToMany(mappedBy = "member", orphanRemoval = false)
    val members: MutableList<Member> = mutableListOf()
){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null



//    companion object{
//        fun createTeam(
//            name: String,
//        ):Team{
//
//            val team = Team()
//
//            team.name = name
//
//            return team
//        }
//
//    }

    fun getIssuesSize():Long{
        return this.issues.size.toLong()
    }

    fun getMembersSize():Long{
        return this.members.size.toLong()
    }

}