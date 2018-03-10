package colon.jonathan.buttoncodechallenge.model

import android.annotation.SuppressLint
import android.os.Parcelable
import colon.jonathan.buttoncodechallenge.data.local.model.MutableTransfer
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Transfer(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("amount")
        val amount: String,

        @field:SerializedName("user_id")
        val userId: Int,

        @field:SerializedName("candidate")
        val candidate: String


) : Parcelable {

    fun mutable(): MutableTransfer {
        return MutableTransfer(
                id,
                amount,
                userId,
                candidate
        )
    }
}