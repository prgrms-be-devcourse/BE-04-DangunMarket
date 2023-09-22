package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageQueryRepository chatMessageQueryRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;

    public ChatMessageRepositoryImpl(ChatMessageQueryRepository chatMessageQueryRepository, ChatMessageMongoRepository chatMessageMongoRepository) {
        this.chatMessageQueryRepository = chatMessageQueryRepository;
        this.chatMessageMongoRepository = chatMessageMongoRepository;
    }

    @Override
    public List<ChatMessage> findNotReadMessageByChatRoomIdAndSenderId(Long chatRoomId, Long senderId) {
        return chatMessageQueryRepository.findNotReadMessageByChatRoomIdAndSenderId(chatRoomId, senderId);
    }

    @Override
    public void markMessagesAsRead(List<String> messageIds) {
        chatMessageQueryRepository.markMessagesAsRead(messageIds);
    }

}
