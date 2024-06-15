package com.arij.ajir.domain.team.model

import com.arij.ajir.domain.issue.model.Issue
import com.arij.ajir.domain.member.model.Member
import jakarta.persistence.*

@Entity
@Table(name = "team")
class Team(
    @Column(name = "name", nullable = false, unique = true)
    var name: String = "",

    @OneToMany(mappedBy = "team", orphanRemoval = true, fetch = FetchType.LAZY)
    val issues: MutableList<Issue> = mutableListOf(),

    @OneToMany(mappedBy = "team", orphanRemoval = false, fetch = FetchType.LAZY)
    var members: MutableList<Member> = mutableListOf()
){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}