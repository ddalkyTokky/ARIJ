package com.arij.ajir.domain.team.entity

import jakarta.persistence.*

@Entity
@Table(name = "team")
class Team{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "name", nullable = false, unique = true)
    var name: String = ""

    @OneToMany(mappedBy = "issue", orphanRemoval = true)
    val issues: Issue? = null

    @OneToMany(mappedBy = "member", orphanRemoval = false)
    val members: Member = Member()

    companion object{
        fun createTeam(
            name: String,
        ):Team{

            val team = Team()

            team.name = name

            return team
        }

        fun getIssuesSize():Long{
           return Team().issues.size.toLong() ?: 0L
        }

        fun getMembersSize():Long{
            return Team().members.size.toLong()
        }
    }

}