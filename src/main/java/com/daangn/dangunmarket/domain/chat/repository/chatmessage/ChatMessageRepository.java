package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto.ChatMessagePageDto;

import java.util.List;

public interface ChatMessageRepository {

    List<ChatMessage> findNotReadMessageByChatRoomIdAndSenderId(Long chatRoomId, Long senderId);

    void markMessagesAsRead(List<String> messageIds);

    List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds);

    List<ChatMessage> findByChatRoomIdWithPagination(ChatMessagePageDto chatMessagePageDto);

}
