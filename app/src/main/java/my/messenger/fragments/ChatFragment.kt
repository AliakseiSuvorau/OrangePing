package my.messenger.fragments

import android.os.Bundle
import android.view.View
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
import my.messenger.MainActivity
import my.messenger.R
import my.messenger.adapters.MessageAdapter
import my.messenger.network.RetrofitClient

class ChatFragment : Fragment(R.layout.fragment_chat), OnMessageSendListener {
    private var chatId: Int = -1
    private var chatName: String = ""
    private var scroll: Int = 0

    private lateinit var recyclerMessages: RecyclerView
    private lateinit var messagesAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            chatId = it.getInt(CHAT_ID)
            chatName = it.getString(CHAT_NAME).orEmpty()
        }
    }

    companion object {
        private const val CHAT_ID = "chat_id"
        private const val CHAT_NAME = "chat_name"
        private const val SCROLL = "scroll"

        fun newInstance(chatId: Int, chatName: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putInt(CHAT_ID, chatId)
                putString(CHAT_NAME, chatName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesAdapter = MessageAdapter(mutableListOf())
        displayMessages(view)

        fetchMessages()

        childFragmentManager.commit {
            replace(
                R.id.new_message_fragment, NewMessageEnterFragment())
        }

        val headerFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.header) as? HeaderFragment

        headerFragment?.setTitle(chatName)

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
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatService.getMessages(chatId)
                }

                if (!isAdded) return@launch

                messagesAdapter.updateMessages(response.messages)
                val scrollTo = (activity as? MainActivity)?.chatInfoMap?.let { it[chatId]?.first ?: 0 } ?: 0
                recyclerMessages.post {
                    recyclerMessages.scrollBy(0, scrollTo)
                }
            } catch (e: Exception) {
                if (!isAdded) return@launch

                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createMessage(messageText: String) {
        lifecycleScope.launch {
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

    override fun onMessageSend(text: String) {
        createMessage(text)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCROLL, recyclerMessages.computeVerticalScrollOffset())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            scroll = it.getInt(SCROLL, 0)
        }
    }
}
