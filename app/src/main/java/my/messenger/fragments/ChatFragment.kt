package my.messenger.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.messenger.APP_NAME
import my.messenger.R
import my.messenger.adapters.MessageAdapter
import my.messenger.network.RetrofitClient
import my.messenger.network.responses.GetMessagesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment(
    private val chatId: Int,
    private val chatTitle: String
): Fragment(R.layout.fragment_chat) {

    private lateinit var recyclerMessages: RecyclerView
    private lateinit var messagesAdapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesAdapter = MessageAdapter(mutableListOf())
        displayMessages(view)

        fetchMessages()

        childFragmentManager.commit {
            replace(R.id.new_message_fragment, NewMessageEnterFragment(this@ChatFragment))
        }

        val headerFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.header) as? HeaderFragment

        headerFragment?.setTitle(chatTitle)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            headerFragment?.setTitle(APP_NAME)
            parentFragmentManager.popBackStack()
        }
    }

    private fun displayMessages(view: View) {
        recyclerMessages = view.findViewById(R.id.messages_recycler)

        recyclerMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerMessages.adapter = messagesAdapter
    }

    private fun fetchMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatService.getMessages(chatId)
                }
                messagesAdapter.updateMessages(response.messages)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createMessage(messageText: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatService.createMessage(chatId, messageText)
                }
                messagesAdapter.updateMessages(response.messages)
                recyclerMessages.scrollToPosition(response.messages.size - 1)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
