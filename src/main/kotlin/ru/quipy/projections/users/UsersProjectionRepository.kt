package ru.quipy.projections.users

import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import ru.quipy.jooq.model.Tables.USERS_PROJECTIONS
import ru.quipy.jooq.model.tables.records.UsersProjectionsRecord
import java.util.*

@Repository
class UsersProjectionRepository(
    val dslContext: DSLContext
) {

    fun save(userId: UUID,
             nickname: String,
             userName: String) {
        dslContext.insertInto(USERS_PROJECTIONS)
            .set(USERS_PROJECTIONS.USER_ID, userId)
            .set(USERS_PROJECTIONS.NICKNAME, nickname)
            .set(USERS_PROJECTIONS.NAME, userName)
            .onConflict(USERS_PROJECTIONS.USER_ID)
            .doNothing()
            .execute()
    }

    fun findByName(name: String): Result<UsersProjectionsRecord> {
        return dslContext.selectFrom(USERS_PROJECTIONS)
            .where(USERS_PROJECTIONS.NAME.like("%$name%"))
            .fetch()
    }

}