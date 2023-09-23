package com.daangn.dangunmarket.domain.chat.repository.chatmessage;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository {

    List<ChatMessage> findByChatRoomIds(List<Long> chatRoomIds);

    ChatMessage save(ChatMessage chatMessage);
}
