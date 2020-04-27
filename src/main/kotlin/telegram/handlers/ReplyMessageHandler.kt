package telegram.handlers

import database.SupportHistory
import database.User
import database.Users
import org.telegram.telegrambots.api.objects.Update
import telegram.ChatBot
import telegram.PeerExt
import telegram.adminGroupId
import java.util.*

class ReplyMessageHandler(
    private val bot: ChatBot,
    private val supportHistory: SupportHistory
) : Handler {
    override fun canHandle(update: Update): Boolean {
        return update.message.isReply && update.message.chatId.toString() == adminGroupId
    }

    override fun handle(update: Update, peer: PeerExt, user: User) {
        val replyMessage = update.message.replyToMessage
        val adminMessageId = replyMessage.messageId
        val messageText = update.message.text

        val support =
            supportHistory.getByAdminMessageId(adminMessageId) ?: throw Exception("Support by $adminMessageId is empty")

        supportHistory.setResponse(messageText, Date(), support.id)

        bot.sendAnswerToUser(messageText, support.userId.toLong())
    }
}