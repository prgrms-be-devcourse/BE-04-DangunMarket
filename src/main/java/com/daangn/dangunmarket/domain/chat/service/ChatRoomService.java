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
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_ENTITY;

@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEntryRepository roomEntryRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final ChatMapper mapper;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, PostRepository postRepository, ChatMessageRepository chatMessageRepository, ChatRoomEntryRepository roomEntryRepository, SessionInfoRepository sessionInfoRepository, ChatMapper mapper) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.postRepository = postRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.roomEntryRepository = roomEntryRepository;
        this.sessionInfoRepository = sessionInfoRepository;
        this.mapper = mapper;
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

    public void addMemberToRoom(String roomId, String memberId) {
        roomEntryRepository.addMemberToRoom(roomId, memberId);
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
    public void deleteChatRoomByIdAndMemberId(Long chatRoomId, Long deleteRequestMemberId) {
        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()-> new EntityNotFoundException(NOT_FOUND_ENTITY));
        List<ChatRoomInfo> findChatRoomInfos = chatRoomInfoRepository.findByChatRoomId(chatRoomId);

        chatRoom.deleteChatRoom(deleteRequestMemberId,findChatRoomInfos);
    }

    @Transactional
    public void deleteChatRoomEntryInMemberId(String sessionId) {
        SessionInfo sessionInfo = sessionInfoRepository.getById(sessionId);

        String roomId = sessionInfo.getRoomId().toString();
        String memberId = sessionInfo.getMemberId().toString();

        if (roomEntryRepository.isMemberInRoom(roomId, memberId)){
            roomEntryRepository.removeMemberFromRoom(roomId, memberId);
        }

        if (roomEntryRepository.getMembersInRoom(roomId).size() == 0){
            roomEntryRepository.deleteChatRoomEntryByRoomId(roomId);
        }

        sessionInfoRepository.delete(sessionInfo);
    }

}
