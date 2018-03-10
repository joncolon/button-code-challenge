package colon.jonathan.buttoncodechallenge.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import colon.jonathan.buttoncodechallenge.data.local.model.MutableTransfer
import colon.jonathan.buttoncodechallenge.data.local.model.MutableUser

@Database(
        entities = arrayOf(MutableUser::class, MutableTransfer::class),
        version = 1,
        exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun transferDao(): TransferDao

    companion object {
        const val FILENAME: String = "ButtonAppDatabase.db"
        const val USER_TABLE = "users"
        const val TRANSFER_TABLE = "transfers"
    }
}