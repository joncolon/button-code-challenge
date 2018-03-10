package colon.jonathan.buttoncodechallenge.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.data.local.model.MutableUser

@Dao
interface UserDao {

    @Query("SELECT * FROM ${AppDatabase.USER_TABLE}")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM ${AppDatabase.USER_TABLE} WHERE id = :userId")
    fun getUserById(userId: Int): LiveData<User>

    @Delete
    fun deleteUser(user: MutableUser)

    @Insert(onConflict = REPLACE)
    fun insertUser(user: MutableUser)

}