package br.com.devcave.reactive.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.validation.constraints.NotEmpty

@Table
data class Anime(
    @Id
    val id: Long,
    @field:NotEmpty
    val name: String
)