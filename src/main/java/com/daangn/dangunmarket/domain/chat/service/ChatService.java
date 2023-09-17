package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ChatService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper mapper;

    public ChatService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, ChatMapper mapper) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.mapper = mapper;
    }

    public ChatRoomsFindResponses findChatRoomsByMemberId(Long memberId, Pageable pageable) {
        Slice<ChatRoomInfo> responseChatRoomInfos = chatRoomInfoRepository.findMembersInSameChatRooms(memberId, pageable);

        List<Long> chatRoomInfoIds = responseChatRoomInfos.getContent().stream()
                .map(e -> e.getId())
                .toList();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomInfoIds(chatRoomInfoIds);

        return mapper.toChatRoomsFindResponses(responseChatRoomInfos, chatMessages);
    }

}
