package my.messenger.network.responses

import my.messenger.model.structs.Message

data class GetMessagesResponse(
    val id: Int,
    val messages: List<Message>,
)
