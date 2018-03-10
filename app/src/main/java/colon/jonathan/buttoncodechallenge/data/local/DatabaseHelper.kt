package colon.jonathan.buttoncodechallenge.data.local

import android.arch.lifecycle.LiveData
import colon.jonathan.buttoncodechallenge.data.local.model.MutableTransfer
import colon.jonathan.buttoncodechallenge.data.local.model.MutableUser
import colon.jonathan.buttoncodechallenge.model.Transfer
import colon.jonathan.buttoncodechallenge.model.User
import executeInThread
import javax.inject.Inject

class DatabaseHelper @Inject constructor(val database: AppDatabase) : UserDao, TransferDao {

    override fun getAllUsers(): LiveData<List<User>> {
        return database.userDao().getAllUsers()
    }

    override fun getAllTransfers(): LiveData<List<Transfer>> {
        return database.transferDao().getAllTransfers()
    }

    override fun getAllTransfersByUserId(userId: Int): LiveData<Transfer> {
        return database.transferDao().getAllTransfersByUserId(userId)
    }

    override fun getUserById(userId: Int): LiveData<User> {
        return database.userDao().getUserById(userId)
    }

    override fun insertUser(user: MutableUser) {
        return executeInThread { database.userDao().insertUser(user) }
    }

    override fun insertTransfer(transfer: MutableTransfer) {
        return executeInThread { database.transferDao().insertTransfer(transfer) }
    }

    override fun deleteUser(user: MutableUser) {
        return executeInThread { database.userDao().deleteUser(user) }
    }

    override fun deleteTransfer(transfer: MutableTransfer) {
        return executeInThread { database.transferDao().deleteTransfer(transfer) }
    }

}