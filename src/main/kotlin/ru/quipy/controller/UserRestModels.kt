package ru.quipy.controller

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateUserRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("secret") val secret: String
)

data class UserProjection(
    val name: String,
    val nickname: String,
    val userId: UUID
)