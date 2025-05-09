package my.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.messenger.R
import my.messenger.adapters.ChatAdapter
import my.messenger.network.responses.GetChatsResponse
import my.messenger.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsListFragment : Fragment(R.layout.fragment_chat_list) {

    private lateinit var recyclerChats: RecyclerView
    private lateinit var chatsAdapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsAdapter = ChatAdapter(mutableListOf(), this)
        displayChats(view)
        fetchChats()
    }

    private fun displayChats(view: View) {
        recyclerChats = view.findViewById(R.id.chats_recycler)

        recyclerChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerChats.adapter = chatsAdapter

        view.findViewById<FloatingActionButton>(R.id.add_chat_button).setOnClickListener {
            showAddChatDialog()
        }
    }

    private fun showAddChatDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_chat, null)

        val chatName = dialogView.findViewById<EditText>(R.id.chat_name)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("New chat")
            .setView(dialogView)
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.add_chat_dialog_background))
            .setPositiveButton("Create") { _, _ ->
                val name = chatName.text.toString().trim()
                if (name.isNotEmpty()) {
                    createChat(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createChat(name: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatService.createChat(name)
                }
                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChats() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatService.getChats()
                }
                chatsAdapter.updateChats(response.chats)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
