package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.create
import ru.quipy.projections.users.UsersProjectionRepository
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val usersProjectionRepository: UsersProjectionRepository
) {
    @PostMapping("/{nickname}")
    fun createProject(
        @PathVariable nickname: String,
        @RequestBody request: CreateUserRequest
    ): UserCreatedEvent {
        return userEsService.create { it.create(request.name, nickname, request.secret) }
    }

    @GetMapping()
    fun getByName(
        @RequestParam name: String
    ): List<UserProjection> {
        return usersProjectionRepository.findByName(name)
            .map { UserProjection(it.name, it.nickname, it.userId) }
    }

}