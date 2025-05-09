package my.messenger.network.responses

import my.messenger.model.structs.Chat

data class GetChatsResponse(
    val chats: List<Chat>,
)
