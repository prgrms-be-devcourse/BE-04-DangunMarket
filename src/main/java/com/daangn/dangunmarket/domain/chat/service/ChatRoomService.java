package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.repository.sessioninfo.SessionInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatMapper;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatRoomService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final ChatRoomEntryRepository chatRoomEntryRepository;
    private final ChatMapper mapper;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, SessionInfoRepository sessionInfoRepository, ChatRoomEntryRepository chatRoomEntryRepository, ChatMapper mapper) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.sessionInfoRepository = sessionInfoRepository;
        this.chatRoomEntryRepository = chatRoomEntryRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Long createChatRoom(Long memberId, Long writerId, ChatRoomCreateRequest request) {

        ChatRoom chatRoom = new ChatRoom();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomInfo sellerChatRoomInfo = new ChatRoomInfo(true, request.postId(), savedChatRoom, writerId);
        chatRoomInfoRepository.save(sellerChatRoomInfo);
        ChatRoomInfo buyerChatRoomInfo = new ChatRoomInfo(false, request.postId(), savedChatRoom, memberId);
        chatRoomInfoRepository.save(buyerChatRoomInfo);

        return chatRoom.getId();
    }

    public ChatRoomsFindResponses findChatRoomsByMemberId(Long memberId, Pageable pageable) {
        Slice<JoinedMemberResponse> roomInfoWithMembers = chatRoomInfoRepository.findMembersInSameChatRooms(memberId, pageable);

        List<Long> chatRoomIds = roomInfoWithMembers.getContent().stream()
                .map(e -> e.chatRoomInfo().getChatRoomId())
                .toList();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIds(chatRoomIds);

        return mapper.toChatRoomsFindResponses(roomInfoWithMembers, chatMessages);
    }

    @Transactional
    public void deleteChatRoomByIdAndMemberId(Long chatRoomId, Long memberId) {
        List<ChatRoomInfo> findChatRoomInfos = chatRoomInfoRepository.findByChatRoomId(chatRoomId);

        ChatRoomInfo chatRoomInfo = findChatRoomInfos.stream()
                .filter(e -> e.isSameMember(memberId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));

        chatRoomInfo.deleteChatRoomInfo();

        if (findChatRoomInfos.size() <= 1){
            chatRoomInfo.getChatRoom().deleteChatRoom();
        }
    }

    @Transactional
    public void deleteChatRoomEntryInMemberId(String sessionId) {
        SessionInfo sessionInfo = sessionInfoRepository.getById(sessionId);

        String roomId = Long.toString(sessionInfo.getRoomId());
        String memberId = Long.toString(sessionInfo.getMemberId());

        if (chatRoomEntryRepository.isMemberInRoom(roomId, memberId)){
            chatRoomEntryRepository.removeMemberFromRoom(roomId, memberId);
        }

        if (chatRoomEntryRepository.getMembersInRoom(roomId).size() == 0){
            chatRoomEntryRepository.deleteChatRoomEntryByRoomId(roomId);
        }

        sessionInfoRepository.delete(sessionInfo);
    }

}
