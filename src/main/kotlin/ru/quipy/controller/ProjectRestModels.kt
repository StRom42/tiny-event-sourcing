package ru.quipy.controller

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class CreateProjectRequest(
    @JsonProperty("projectTitle") val projectTitle: String
)

data class AddParticipantRequest(
    @JsonProperty("userId") val userId: UUID
)

data class EditProjectNameRequest(
    @JsonProperty("newName") val newName: String
)

data class CreateTaskRequest(
    @JsonProperty("taskName") val taskName: String
)

data class CreateTaskStatusRequest(
    @JsonProperty("statusColor") val statusColor: String,
    @JsonProperty("statusName") val statusName: String
)

data class SetTaskStatusRequest(
    @JsonProperty("statusId") val statusId: UUID
)

data class EditTaskNameRequest(
    @JsonProperty("newTaskName") val newTaskName: String
)

data class SetTaskPerformerRequest(
    @JsonProperty("performerId") val performerId: UUID
)

data class TaskInfoProjection(
    val taskId: UUID,
    val taskTitle: String,
    val status: Status,
    val executors: List<UserProjection>,
)

data class Status(
    val statusId: UUID? = null,
    val statusName: String,
    val statusColor: String
) {

    companion object {
        val DEFAULT_STATUS = Status(null, "Default", "#FFFFFF")
    }
}

data class ProjectProjection(
    val projectId: UUID,
    val projectTitle: String
)
