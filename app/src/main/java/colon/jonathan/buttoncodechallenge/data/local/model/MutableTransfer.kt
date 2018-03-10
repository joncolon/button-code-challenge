package colon.jonathan.buttoncodechallenge.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import colon.jonathan.buttoncodechallenge.data.local.AppDatabase
import colon.jonathan.buttoncodechallenge.model.Transfer

@Entity(tableName = AppDatabase.TRANSFER_TABLE)
data class MutableTransfer(

        @PrimaryKey
        val id: Int,

        val amount: String,

        val userId: Int,

        val candidate: String

) {

    fun immutable(): Transfer {
        return Transfer(
                id,
                amount,
                userId,
                candidate
        )
    }
}