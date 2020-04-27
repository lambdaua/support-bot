package database

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import java.util.*

class SupportHistory(database: MongoDatabase) {
    private val collection: MongoCollection<Support> = database.getCollection()

    fun get(userId: Int): Support? {
        return collection.find(Support::userId eq userId).first()
    }

    fun create(userId: Int, text: String, adminMessageId: Int): Support? {
        val question = Support(questionText = text, userId = userId, adminMessageId = adminMessageId)
        collection.insertOne(question)
        return question
    }

    fun getByAdminMessageId(messageId: Int): Support? {
        return collection.find(Support::adminMessageId eq messageId).first()
    }

    fun setResponse(responseText: String, responseDate: Date, id: Id<Support>) {
        collection.updateOne(
            Support::id eq id,
            combine(
                set(Support::responseText, responseText),
                set(Support::responseDate, responseDate)
            )
        )

    }
}

data class Support(
    @BsonId val id: Id<Support> = newId(),
    val questionText: String,
    val responseText: String? = null,
    val questionDate: Date? = Date(),
    val responseDate: Date? = null,
    val adminMessageId: Int? = null,
    val userId: Int
)