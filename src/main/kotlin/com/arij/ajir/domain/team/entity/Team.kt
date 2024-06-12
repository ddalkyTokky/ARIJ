package com.arij.ajir.domain.team.entity

import com.arij.ajir.domain.member.model.Member
import jakarta.persistence.*

@Entity
@Table(name = "team")
class Team(

    @OneToMany(mappedBy = "issue", orphanRemoval = true)
    val issues: Issue? = null,

    @OneToMany(mappedBy = "member", orphanRemoval = false)
    val members: MutableList<Member> = mutableListOf()
){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "name", nullable = false, unique = true)
    open var name: String = ""
    private set

    companion object{
        fun createTeam(
            name: String,
        ):Team{

            val team = Team()

            team.name = name

            return team
        }

    }
    fun getIssuesSize():Long{
        return this.issues.size.toLong() ?: 0L
    }

    fun getMembersSize():Long{
        return this.members.size.toLong()
    }

}