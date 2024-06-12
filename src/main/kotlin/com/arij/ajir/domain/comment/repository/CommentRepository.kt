package com.arij.ajir.domain.comment.repository

import com.arij.ajir.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}