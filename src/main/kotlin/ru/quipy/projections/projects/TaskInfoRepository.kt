package ru.quipy.projections.projects

import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import ru.quipy.jooq.model.Tables.*
import ru.quipy.jooq.model.tables.records.TaskExecutorsRecord
import ru.quipy.jooq.model.tables.records.TaskInfosRecord
import ru.quipy.jooq.model.tables.records.TaskStatusesRecord
import java.util.*

@Repository
class TaskInfoRepository(
    val dslContext: DSLContext
) {

    fun saveTask(taskId: UUID, projectId: UUID, taskName: String, statusId: UUID? = null) {
        dslContext.insertInto(TASK_INFOS)
            .set(TASK_INFOS.TASK_ID, taskId)
            .set(TASK_INFOS.PROJECT_ID, projectId)
            .set(TASK_INFOS.TASK_STATUS_ID, statusId)
            .set(TASK_INFOS.TASK_NAME, taskName)
            .onConflict(TASK_INFOS.TASK_ID)
            .doUpdate()
            .set(TASK_INFOS.TASK_NAME, taskName)
            .execute()
    }

    fun saveTaskStatus(projectId: UUID, statusId: UUID, statusName: String, statusColor: String) {
        dslContext.insertInto(TASK_STATUSES)
            .set(TASK_STATUSES.PROJECT_ID, projectId)
            .set(TASK_STATUSES.STATUS_ID, statusId)
            .set(TASK_STATUSES.STATUS_NAME, statusName)
            .set(TASK_STATUSES.HEX_COLOR, statusColor)
            .onConflict(TASK_STATUSES.PROJECT_ID, TASK_STATUSES.STATUS_ID)
            .doNothing()
            .execute()
    }

    fun findTaskById(taskId: UUID): TaskInfosRecord {
        return dslContext.selectFrom(TASK_INFOS)
            .where(TASK_INFOS.TASK_ID.eq(taskId))
            .fetchSingle()
    }

    fun addTaskPerformer(taskId: UUID, performer: UUID) {
        dslContext.insertInto(TASK_EXECUTORS)
            .set(TASK_EXECUTORS.TASK_ID, taskId)
            .set(TASK_EXECUTORS.USER_ID, performer)
            .onConflict(TASK_EXECUTORS.TASK_ID, TASK_EXECUTORS.USER_ID)
            .doNothing()
            .execute()
    }

    fun removeTaskPerformer(taskId: UUID, performer: UUID) {
        dslContext.delete(TASK_EXECUTORS)
            .where(TASK_EXECUTORS.TASK_ID.eq(taskId))
            .and(TASK_EXECUTORS.USER_ID.eq(performer))
            .execute()
    }

    fun removeTaskStatus(statusId: UUID) {
        dslContext.delete(TASK_STATUSES)
            .where(TASK_STATUSES.STATUS_ID.eq(statusId))
            .execute()
    }

    fun removeTask(taskId: UUID) {
        dslContext.delete(TASK_INFOS)
            .where(TASK_INFOS.TASK_ID.eq(taskId))
            .execute()
    }

    fun findByTaskName(projectId: UUID, taskTitle: String): Map<TaskInfosRecord, List<TaskExecutorsRecord>> {
        return dslContext.select().from(TASK_INFOS)
            .leftJoin(TASK_EXECUTORS).on(TASK_INFOS.TASK_ID.eq(TASK_EXECUTORS.TASK_ID))
            .where(TASK_INFOS.TASK_NAME.like("%$taskTitle%"))
            .and(TASK_INFOS.PROJECT_ID.eq(projectId))
            .fetchGroups(
                { it.into(TASK_INFOS) },
                { it.into(TASK_EXECUTORS) }
            )
    }

    fun findAll(projectId: UUID): Map<TaskInfosRecord, List<TaskExecutorsRecord>> {
        return dslContext.select().from(TASK_INFOS)
            .leftJoin(TASK_EXECUTORS).on(TASK_INFOS.TASK_ID.eq(TASK_EXECUTORS.TASK_ID))
            .where(TASK_INFOS.PROJECT_ID.eq(projectId))
            .fetchGroups(
                { it.into(TASK_INFOS) },
                { it.into(TASK_EXECUTORS) }
            )
    }

    fun findAllProjectStatuses(projectId: UUID): Result<TaskStatusesRecord> {
        return dslContext.select().from(TASK_STATUSES)
            .where(TASK_STATUSES.PROJECT_ID.eq(projectId))
            .fetch()
            .into(TASK_STATUSES)
    }

}