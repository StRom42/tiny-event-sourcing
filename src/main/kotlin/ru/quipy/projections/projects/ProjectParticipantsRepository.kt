package ru.quipy.projections.projects

import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import ru.quipy.jooq.model.Tables.*
import ru.quipy.jooq.model.tables.records.ProjectInfoRecord
import ru.quipy.jooq.model.tables.records.UsersProjectionsRecord
import java.util.*

@Repository
class ProjectParticipantsRepository(
    val dslContext: DSLContext
) {

    fun save(projectId: UUID, userId: UUID) {
        dslContext.insertInto(PROJECT_PARTICIPANTS)
            .set(PROJECT_PARTICIPANTS.PROJECT_ID, projectId)
            .set(PROJECT_PARTICIPANTS.USER_ID, userId)
            .execute()
    }

    fun findProjectsByUserId(userId: UUID): Result<ProjectInfoRecord> {
        return dslContext.select().from(PROJECT_INFO)
            .join(PROJECT_PARTICIPANTS)
            .on(PROJECT_INFO.PROJECT_ID.eq(PROJECT_PARTICIPANTS.PROJECT_ID))
            .where(PROJECT_PARTICIPANTS.USER_ID.eq(userId))
            .fetch()
            .into(PROJECT_INFO)
    }

    fun findParticipantsByProjectId(projectId: UUID): Result<UsersProjectionsRecord> {
        return dslContext.select().from(USERS_PROJECTIONS)
            .join(PROJECT_PARTICIPANTS)
            .on(USERS_PROJECTIONS.USER_ID.eq(PROJECT_PARTICIPANTS.USER_ID))
            .where(PROJECT_PARTICIPANTS.PROJECT_ID.eq(projectId))
            .fetch()
            .into(USERS_PROJECTIONS)
    }
}