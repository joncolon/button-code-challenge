package colon.jonathan.buttoncodechallenge.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import colon.jonathan.buttoncodechallenge.data.local.AppDatabase
import colon.jonathan.buttoncodechallenge.model.User


@Entity(tableName = AppDatabase.USER_TABLE)
class MutableUser(

        @PrimaryKey
        var id: Int = UNKNOWN_ID,

        val name: String,

        val email: String,

        val candidate: String
) {

    fun immutable() : User {
        return User(
                id,
                name,
                email,
                candidate
        )
    }

    companion object {
        private val UNKNOWN_ID = -1
    }
}