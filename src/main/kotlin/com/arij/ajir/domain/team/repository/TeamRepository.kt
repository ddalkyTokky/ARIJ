package com.arij.ajir.domain.team.repository

import com.arij.ajir.domain.team.model.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository: JpaRepository<Team, Long> {


    fun existsByName(name: String): Boolean

    fun findByName(name: String): List<Team>
}