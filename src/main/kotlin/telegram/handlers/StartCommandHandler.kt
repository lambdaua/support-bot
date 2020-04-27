package telegram.handlers

import database.User
import database.Users
import org.telegram.telegrambots.api.objects.Update
import telegram.PeerExt
import java.lang.Exception

class StartCommandHandler() : Handler {
    override fun canHandle(update: Update): Boolean {
        val messageText = update.message.text
        return update.hasMessage() && messageText.startsWith("/start")
    }

    @Throws(Exception::class)
    override fun handle(update: Update, peer: PeerExt, user: User) {
        peer.sendText(
            "Hi, ${user.username}! I'm Lambda's Team Support"
        )
    }

}