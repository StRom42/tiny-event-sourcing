package ru.quipy.projections

import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.projections.projects.ProjectInfoRepository
import ru.quipy.projections.projects.ProjectParticipantsRepository
import ru.quipy.projections.projects.TaskInfoRepository
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-project-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber(
    val projectInfoRepository: ProjectInfoRepository,
    val projectParticipantsRepository: ProjectParticipantsRepository,
    val taskInfoRepository: TaskInfoRepository
) {
    @SubscribeEvent
    fun projectCreatedSubscriber(event: ProjectCreatedEvent) {
        projectInfoRepository.save(event.projectId, event.title)
    }

    @SubscribeEvent
    fun projectParticipantAddedEventSubscriber(event: ProjectParticipantAddedEvent) {
        projectParticipantsRepository.save(event.projectId, event.userId)
    }

    @SubscribeEvent
    fun projectNameEditedEventSubscriber(event: ProjectNameEditedEvent) {
        projectInfoRepository.save(event.projectId, event.newProjectName)
    }

    @SubscribeEvent
    fun taskStatusCreatedEventSubscriber(event: TaskStatusCreatedEvent) {
        taskInfoRepository.saveTaskStatus(event.projectId, event.statusId, event.statusName, event.statusColor)
    }

    @SubscribeEvent
    fun taskStatusSetEventSub(event: TaskStatusSetEvent) {
        val task = taskInfoRepository.findTaskById(event.taskId)
        taskInfoRepository.saveTask(task.taskId, task.projectId, task.taskName, event.statusId)
    }

    @SubscribeEvent
    fun taskStatusDeletedEventSub(event: TaskStatusDeletedEvent) {
        val taskStatus = taskInfoRepository.removeTaskStatus(event.statusId)
    }

    @SubscribeEvent
    fun taskCreatedEventSub(event: TaskCreatedEvent) {
        taskInfoRepository.saveTask(event.taskId, event.projectId, event.taskName)
    }

    @SubscribeEvent
    fun taskNameEditedEventSub(event: TaskNameEditedEvent) {
        val task = taskInfoRepository.findTaskById(event.taskId)
        taskInfoRepository.saveTask(task.taskId, task.projectId, event.newTaskName)
    }

    @SubscribeEvent
    fun taskPerfomerSetEventSub(event: TaskPerfomerSetEvent) {
        taskInfoRepository.addTaskPerformer(event.taskId, event.performer)
    }

    @SubscribeEvent
    fun taskPerfomerDeletedEventSub(event: TaskPerfomerDeletedEvent) {
        taskInfoRepository.removeTaskPerformer(event.taskId, event.performer)
    }

    @SubscribeEvent
    fun taskDeletedEventSub(event: TaskDeletedEvent) {
        taskInfoRepository.removeTask(event.taskId)
    }

}