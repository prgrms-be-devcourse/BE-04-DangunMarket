package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, PostRepository postRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.postRepository = postRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public Long createChatRoom(Long writerId, ChatRoomCreateRequest request) {

        ChatRoom chatRoom = new ChatRoom();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomInfo sellerChatRoomInfo = new ChatRoomInfo(true, request.postId(), savedChatRoom, writerId);
        chatRoomInfoRepository.save(sellerChatRoomInfo);
        ChatRoomInfo buyerChatRoomInfo = new ChatRoomInfo(false, request.postId(), savedChatRoom, request.memberId());
        chatRoomInfoRepository.save(buyerChatRoomInfo);

        return chatRoom.getId();
    }

    public int readAllMessage(Long chatRoomId, Long senderId) {
        List<ChatMessage> notReadMessages = chatMessageRepository.findNotReadMessageByChatRoomIdAndSenderId(chatRoomId, senderId);
        chatMessageRepository.markMessagesAsRead(notReadMessages.stream().map(ChatMessage::getId).toList());

        return notReadMessages.size();
    }

}
