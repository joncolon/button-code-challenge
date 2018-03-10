package colon.jonathan.buttoncodechallenge.utilities

import android.support.v7.util.DiffUtil
import colon.jonathan.buttoncodechallenge.model.User

class DiffCallback(internal var newMatches: List<User>, internal var oldMatches: List<User>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldMatches.size
    }

    override fun getNewListSize(): Int {
        return newMatches.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatches[oldItemPosition].id == newMatches[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatches[oldItemPosition] == newMatches[newItemPosition]
    }

}