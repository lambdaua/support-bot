package telegram

import database.SupportHistory
import database.Users
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.User
import telegram.handlers.ReplyMessageHandler
import telegram.handlers.StartCommandHandler
import telegram.handlers.TextHandler


class MessagesReceiver(
    private val chatBot: ChatBot,
    private val users: Users,
    private val supportHistory: SupportHistory
) {

    private val handlers = listOf(
        //commands
        StartCommandHandler(),

        //text
        ReplyMessageHandler(chatBot, supportHistory),
        TextHandler(chatBot, supportHistory)
    )

    fun handle(update: Update) {
        if (!update.shouldProcess()) return

        try {
            val sender = update.sender()!!
            val userId = sender.id
            val username = sender.userName ?: ""
            val firstName = sender.firstName

            val user = users.getByTelegramId(userId) ?: users.create(userId, username, firstName)

            val peer = PeerExt(chatBot, userId.toString())

            handlers.find { it.canHandle(update) }?.handle(update, peer, user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

fun Update.shouldProcess(): Boolean {
    if (this.hasMessage()) {
        if (this.message.isUserMessage || this.message.chatId.toString() == adminGroupId) {
            return true
        }
    }
    return false
}

fun Update.sender(): User? {
    if (hasMessage()) return message.from
    return null
}