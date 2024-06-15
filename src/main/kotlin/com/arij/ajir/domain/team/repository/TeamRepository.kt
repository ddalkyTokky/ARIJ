package com.arij.ajir.domain.team.repository

import com.arij.ajir.domain.team.model.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

interface TeamRepository: JpaRepository<Team, Long> {

    fun existsByName(name: String): Boolean

    fun findByName(name: String): List<Team>
}