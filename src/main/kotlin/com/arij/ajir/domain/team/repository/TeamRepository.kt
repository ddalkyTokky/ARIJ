package com.arij.ajir.domain.team.repository

import com.arij.ajir.domain.team.entity.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository: JpaRepository<Team, Long> {
}