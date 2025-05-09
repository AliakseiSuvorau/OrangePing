package my.messenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.messenger.DEFAULT_CHAT_IC_LINK
import my.messenger.model.structs.Chat
import my.messenger.R
import my.messenger.fragments.ChatFragment

class ChatAdapter (
    private val chats: MutableList<Chat>,
    private val fragment: Fragment,
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chatName: TextView = view.findViewById(R.id.chat_name)
        private val chatBar: LinearLayout = view.findViewById(R.id.chat_bar)
        private val chatIcon: ImageView = view.findViewById(R.id.chat_icon)

        fun render(chat: Chat) {
            chatName.text = chat.name
            Glide.with(fragment)
                .load(DEFAULT_CHAT_IC_LINK)
                .into(chatIcon)

            chatBar.setOnClickListener {
                fragment.requireActivity().supportFragmentManager.commit {
                    replace(R.id.fragment_container, ChatFragment(chat.id, chat.name))
                    addToBackStack(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_chat_bar, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount() = chats.size
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) = holder.render(chats[position])

    fun updateChats(newChats: List<Chat>) {
        chats.clear()
        chats.addAll(newChats)
        notifyDataSetChanged()
    }
}
