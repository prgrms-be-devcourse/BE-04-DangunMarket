package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto.ChatMessagePageDto;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class ChatMessageQueryRepository {

    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "chat_messages";

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
        AggregationResults<ChatMessage> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ChatMessage.class);

        return results.getMappedResults();
    }

    public void markMessagesAsRead(List<String> messageIds) {
        Query query = new Query(Criteria.where("_id").in(messageIds));
        Update update = new Update().set("read_or_not", 0);

        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }

    public List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("chat_room_id").in(chatRoomIds)),
                Aggregation.sort(Sort.Direction.DESC, "created_at"),
                Aggregation.group("chat_room_id").first("$$ROOT").as("latest_message"),
                Aggregation.replaceRoot().withValueOf("$latest_message")
        );

        return mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ChatMessage.class).getMappedResults();
    }

    public List<ChatMessage> findByChatRoomIdWithPagination(ChatMessagePageDto chatMessagePageDto) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("chat_room_id").is(chatMessagePageDto.chatRoomId())
        );

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Order.desc("created_at")));

        SkipOperation skipOperation = Aggregation.skip( (chatMessagePageDto.page() - 1) * chatMessagePageDto.pageSize());
        LimitOperation limitOperation = Aggregation.limit(chatMessagePageDto.pageSize());

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
               sortOperation,
                skipOperation,
                limitOperation
        );

        AggregationResults<ChatMessage> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ChatMessage.class);

        List<ChatMessage> mappedResults = results.getMappedResults();
        List<ChatMessage> sortedResults = new ArrayList<>(mappedResults);
        sortedResults.sort(Comparator.comparing(ChatMessage::getCreatedAt));

        return sortedResults;
    }

}
