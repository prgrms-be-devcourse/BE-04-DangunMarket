package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatMessageQueryRepository {

    private MongoTemplate mongoTemplate;

    public ChatMessageQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ChatMessage> findNotReadMessageByChatRoomIdAndSenderId(Long chatRoomId, Long senderId) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("chat_room_id").is(chatRoomId)
                        .and("member_id").is(senderId)
                        .and("read_or_not").is(1)
        );

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Order.desc("created_at")));

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation);
        AggregationResults<ChatMessage> results = mongoTemplate.aggregate(aggregation, "chat_messages", ChatMessage.class);

        return results.getMappedResults();
    }

    public void markMessagesAsRead(List<String> messageIds) {
        Query query = new Query(Criteria.where("_id").in(messageIds));
        Update update = new Update().set("read_or_not", 0);

        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }

}
