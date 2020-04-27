package telegram.handlers

import database.User
import org.telegram.telegrambots.api.objects.Update
import telegram.PeerExt
import java.lang.Exception

interface Handler {
    fun canHandle(update: Update): Boolean

    @Throws(Exception::class)
    fun handle(update: Update, peer: PeerExt, user: User)
}