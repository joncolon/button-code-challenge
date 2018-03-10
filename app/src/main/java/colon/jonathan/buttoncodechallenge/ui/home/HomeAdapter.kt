package colon.jonathan.buttoncodechallenge.ui.home

import DEBUG
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import colon.jonathan.buttoncodechallenge.databinding.ItemUserBinding
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.ui.base.BaseViewHolder
import colon.jonathan.buttoncodechallenge.ui.common.OnUserClickListener
import java.util.*
import javax.inject.Inject

class HomeAdapter @Inject constructor(val homeViewModel: HomeViewModel) : RecyclerView.Adapter<BaseViewHolder>(), OnUserClickListener {

    override fun onUserClicked(user: User) {
        //todo something with user
    }

    val users: MutableList<User>

    init {
        this.users = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemUserBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.adapterPosition
        holder.itemView.setOnClickListener({
            val adapterPosition = holder.adapterPosition
            when {adapterPosition != RecyclerView.NO_POSITION -> onUserClicked(users[position])
            }
        })
        holder.onBind(position)
    }

    override fun getItemCount(): Int = users.size

    fun addItems(newMatches: List<User>) {
        users.addAll(newMatches)
    }

    fun clearItems() {
        users.clear()
    }

    inner class UserListViewHolder(private val binding: ItemUserBinding) :
            BaseViewHolder(binding.root) {

        lateinit var user: User

        override fun onBind(position: Int) {
            user = users[position]
            DEBUG("onBind: ${user.name}")
            binding.viewModel = user
            binding.executePendingBindings()
        }
    }
}