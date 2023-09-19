package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatMessageQueryRepository {

    private MongoTemplate mongoTemplate;

    public ChatMessageQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("chat_room_id").in(chatRoomIds)),
                Aggregation.sort(Sort.Direction.DESC, "created_at"),
                Aggregation.group("chat_room_id").first("$$ROOT").as("latest_message"),
                Aggregation.replaceRoot().withValueOf("$latest_message")
        );

        return mongoTemplate.aggregate(aggregation, "chat_messages", ChatMessage.class).getMappedResults();
    }
}
