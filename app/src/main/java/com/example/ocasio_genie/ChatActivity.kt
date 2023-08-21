package com.example.ocasio_genie

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity() {
    private lateinit var closeButton: ImageView
    private lateinit var chatListView: ListView
    private lateinit var chatInputEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        closeButton = findViewById(R.id.closeButton)
        chatListView = findViewById(R.id.chatListView)
        chatInputEditText = findViewById(R.id.chatInputEditText)
        sendButton = findViewById(R.id.sendButton)

        chatAdapter = ChatAdapter(this, chatMessages)
        chatListView.adapter = chatAdapter

        closeButton.setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val message = chatInputEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                addMessage(message)
                chatInputEditText.text.clear()
            }
        }
    }

    private fun addMessage(message: String) {
        chatMessages.add(message)
        chatAdapter.notifyDataSetChanged()
        scrollListViewToBottom()
    }

    private fun scrollListViewToBottom() {
        chatListView.post {
            chatListView.smoothScrollToPosition(chatAdapter.count - 1)
        }
    }
}
