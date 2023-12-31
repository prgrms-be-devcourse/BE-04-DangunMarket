package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto.ChatMessagePageDto;
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

    @Override
    public List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds) {
        return chatMessageQueryRepository.findByChatRoomIds(chatRoomIds);
    }


    @Override
    public List<ChatMessage> findByChatRoomIdWithPagination(ChatMessagePageDto chatMessagePageDto) {
        return chatMessageQueryRepository.findByChatRoomIdWithPagination(chatMessagePageDto);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(chatMessage);
    }

}
