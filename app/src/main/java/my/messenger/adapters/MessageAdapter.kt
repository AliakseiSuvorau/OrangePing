package my.messenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.messenger.R
import my.messenger.model.structs.Message

class MessageAdapter(
    private val messages: MutableList<Message>
): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.message_text)

        fun render(message: Message) {
            text.text = message.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_message_bar, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = messages.size
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) = holder.render(messages[position])

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}
