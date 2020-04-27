package telegram.handlers

import database.SupportHistory
import database.User
import database.Users
import org.telegram.telegrambots.api.objects.Update
import telegram.ChatBot
import telegram.PeerExt

class TextHandler(
    private val bot: ChatBot,
    private val support: SupportHistory
) : Handler {
    override fun canHandle(update: Update): Boolean {
        if (update.hasMessage()) {
            val message = update.message
            return message.hasText()
        }
        return false
    }

    @Throws(java.lang.Exception::class)
    override fun handle(update: Update, peer: PeerExt, user: User) {
        val message = update.message
        val userId = update.message.from.id
        val msgText = message.text

        peer.sendText("Thank you for your question. We will answer you as soon as possible")

        val adminMessageId = bot.sendQuesToAdminGroup(msgText, user) ?: throw Exception()
        support.create(userId, msgText, adminMessageId)
    }
}