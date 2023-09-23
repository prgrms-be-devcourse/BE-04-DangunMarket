package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final ChatMessageQueryRepository chatMessageQueryRepository;

    public ChatMessageRepositoryImpl(ChatMessageMongoRepository chatMessageMongoRepository, ChatMessageQueryRepository chatMessageQueryRepository) {
        this.chatMessageMongoRepository = chatMessageMongoRepository;
        this.chatMessageQueryRepository = chatMessageQueryRepository;
    }

    public List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds) {
        return chatMessageQueryRepository.findByChatRoomIds(chatRoomIds);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(chatMessage);
    }

}
