package ru.quipy.projections.projects

import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import ru.quipy.jooq.model.Tables.PROJECT_INFO
import ru.quipy.jooq.model.Tables.PROJECT_PARTICIPANTS
import ru.quipy.jooq.model.tables.records.ProjectInfoRecord
import java.util.*

@Repository
class ProjectInfoRepository(
    val dslContext: DSLContext
) {

    fun save(projectId: UUID, projectTitle: String) {
        dslContext.insertInto(PROJECT_INFO)
            .set(PROJECT_INFO.PROJECT_ID, projectId)
            .set(PROJECT_INFO.PROJECT_TITLE, projectTitle)
            .onConflict(PROJECT_INFO.PROJECT_ID)
            .doUpdate()
            .set(PROJECT_INFO.PROJECT_TITLE, projectTitle)
            .execute()
    }

    fun findAll(): Result<ProjectInfoRecord> {
        return dslContext.selectFrom(PROJECT_INFO)
            .fetch()
    }

}