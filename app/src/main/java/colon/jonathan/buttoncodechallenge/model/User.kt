package colon.jonathan.buttoncodechallenge.model

import android.annotation.SuppressLint
import android.os.Parcelable
import colon.jonathan.buttoncodechallenge.data.local.model.MutableUser
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class User(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("email")
        val email: String,

        @field:SerializedName("candidate")
        val candidate: String

) : Parcelable {

    fun mutable(): MutableUser {
        return MutableUser(
                id,
                name,
                email,
                candidate
        )
    }

    fun getInitial(): String {
        return name.substring(0,1)
    }
}