package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


fun ProjectAggregateState.create(title: String, creatorId: UUID): ProjectCreatedEvent {
    val projectId = UUID.nameUUIDFromBytes(title.toByteArray())

    return ProjectCreatedEvent(
        projectId = projectId,
        title = title,
        creatorId = creatorId,
    )
}


fun ProjectAggregateState.addParticipant(userId: UUID): ProjectParticipantAddedEvent {
    require(!participants.contains(userId)) { "Пользователь не может быть добавлен, если он уже участник проекта" }

    return ProjectParticipantAddedEvent(
        userId = userId,
        projectId = getId()
    )
}

fun ProjectAggregateState.nameEdited(newProjectName: String, userId: UUID): ProjectNameEditedEvent {
    require(ownerId == userId) { "Пользователь должен быть владельцем проекта" }

    return ProjectNameEditedEvent(
        projectId = getId(),
        newProjectName = newProjectName,
    )
}


fun ProjectAggregateState.taskStatusCreatedEvent(
    statusColor: String,
    statusName: String,
    userId: UUID
): TaskStatusCreatedEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }
    require(!taskStatuses.values.any { it.name == statusName }) { "Статус $statusName уже существует в проекте" }

    return TaskStatusCreatedEvent(
        statusId = UUID.randomUUID(),
        projectId = getId(),
        statusColor = statusColor,
        statusName = statusName
    )
}


fun ProjectAggregateState.taskStatusSetEvent(
    taskId: UUID,
    statusId: UUID,
    userId: UUID
): TaskStatusSetEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }
    require(taskStatuses.contains(statusId)) { "Статус с id = $statusId должен существовать в проекте" }

    return TaskStatusSetEvent(
        taskId = taskId,
        statusId = statusId
    )
}

fun ProjectAggregateState.taskStatusDeletedEvent(
    statusId: UUID,
    userId: UUID
): TaskStatusDeletedEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }
    require(tasks.none { it.value.taskStatus.id == statusId }) { "Задач с данным статусом не должно быть" }
    require(taskStatuses.contains(statusId)) { "Статус с данным идентификатором должен присутствовать" }

    return TaskStatusDeletedEvent(
        statusId = statusId
    )
}

fun ProjectAggregateState.taskNameEditedEvent(taskId: UUID, newTaskName: String, userId: UUID): TaskNameEditedEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }

    return TaskNameEditedEvent(
        taskId = taskId,
        newTaskName = newTaskName,
    )
}

fun ProjectAggregateState.taskPerformerSetEvent(taskId: UUID, performerId: UUID, userId: UUID): TaskPerfomerSetEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }
    require(participants.contains(performerId)) { "Назначаемый исполнитель должен быть участником проекта" }

    return TaskPerfomerSetEvent(
        taskId = taskId,
        performer = performerId
    )
}

fun ProjectAggregateState.taskPerformerDeletedEvent(
    taskId: UUID,
    performerId: UUID,
    userId: UUID
): TaskPerfomerDeletedEvent {
    require(tasks.contains(taskId)) { "Задача должна принадлежать проекту" }
    require(tasks[taskId]?.performers?.contains(performerId) == true) { "Пользователь должен быть исполнителем задачи" }
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }

    return TaskPerfomerDeletedEvent(
        taskId = taskId,
        performer = performerId
    )
}

fun ProjectAggregateState.taskDeletedEvent(taskId: UUID, userId: UUID): TaskDeletedEvent {
    require(tasks.contains(taskId)) { "Задача должна принадлежать проекту" }
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }

    return TaskDeletedEvent(
        taskId = taskId
    )
}

fun ProjectAggregateState.addTask(name: String, userId: UUID): TaskCreatedEvent {
    require(participants.contains(userId)) { "Пользователь должен быть участником проекта" }

    return TaskCreatedEvent(
        projectId = getId(),
        taskId = UUID.nameUUIDFromBytes(name.toByteArray()),
        taskName = name
    )
}