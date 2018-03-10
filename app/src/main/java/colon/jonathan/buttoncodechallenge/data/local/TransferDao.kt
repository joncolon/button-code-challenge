package colon.jonathan.buttoncodechallenge.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import colon.jonathan.buttoncodechallenge.data.local.model.MutableTransfer
import colon.jonathan.buttoncodechallenge.model.Transfer

@Dao
interface TransferDao {

    @Query("SELECT * FROM ${AppDatabase.TRANSFER_TABLE}")
    fun getAllTransfers(): LiveData<List<Transfer>>

    @Query("SELECT * FROM ${AppDatabase.TRANSFER_TABLE} WHERE userId = :userId")
    fun getAllTransfersByUserId(userId: Int): LiveData<Transfer>

    @Delete
    fun deleteTransfer(transfer: MutableTransfer)

    @Insert(onConflict = REPLACE)
    fun insertTransfer(transfer: MutableTransfer)

}