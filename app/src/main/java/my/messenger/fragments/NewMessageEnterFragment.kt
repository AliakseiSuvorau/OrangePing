package my.messenger.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import my.messenger.R

class NewMessageEnterFragment(
    private val chatFragment: ChatFragment
) : Fragment(R.layout.fragment_new_message) {

    private lateinit var chatMessage: EditText
    private lateinit var sendButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatMessage = view.findViewById(R.id.new_message_text)
        sendButton = view.findViewById(R.id.message_send_button)

        sendButton.setOnClickListener {
            if (chatMessage.text.toString().isNotEmpty()) {
                chatFragment.createMessage(chatMessage.text.toString())
                chatMessage.setText("")
            }
        }
    }
}
