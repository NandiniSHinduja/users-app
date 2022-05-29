package com.example.usersapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usersapplication.R
import com.example.usersapplication.activities.UserDetailsActivity
import com.example.usersapplication.models.UsersDetails

class UsersAdapter(private val listener: UserItemClicked) :
    RecyclerView.Adapter<UsersViewHolder>() {
    private var context: Context? = null
    private val items: ArrayList<UsersDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_users, parent, false)
        val viewHolder = UsersViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val currentItem = items[position]
        holder.firstName.text = currentItem.firstName
        holder.lastName.text = currentItem.lastName
        holder.age.text = currentItem.age
        holder.gender.text = currentItem.gender
        holder.email.text = currentItem.email
        holder.temp
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)

        holder.rl.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra("firstName", currentItem.firstName)
            intent.putExtra("lastName", currentItem.lastName)
            intent.putExtra("imageUrl", currentItem.imageUrl)
            intent.putExtra("age", currentItem.age)
            intent.putExtra("gender", currentItem.gender)
            intent.putExtra("email", currentItem.email)
            intent.putExtra("city", currentItem.temp)
            context?.startActivity(intent)
        })

    }


    fun updateUsers(updatedUsers: ArrayList<UsersDetails>) {
        items.clear()
        items.addAll(updatedUsers)
        notifyDataSetChanged()
    }
}

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val firstName: TextView = itemView.findViewById(R.id.firstName)
    val lastName: TextView = itemView.findViewById(R.id.lastName)
    val age: TextView = itemView.findViewById(R.id.age)
    val gender: TextView = itemView.findViewById(R.id.gender)
    val email: TextView = itemView.findViewById(R.id.email)
    val temp: TextView = itemView.findViewById(R.id.temp)
    val image: ImageView = itemView.findViewById(R.id.userImg)
    val rl: RelativeLayout = itemView.findViewById(R.id.userRL)
}

interface UserItemClicked {
    fun onItemClicked(item: UsersDetails)
}