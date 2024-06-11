package com.arij.ajir.domain.team.entity

import jakarta.persistence.*

@Entity
@Table(name = "team")
class Team(

    @Column(name = "name", nullable = false, unique = true)
    val name: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}