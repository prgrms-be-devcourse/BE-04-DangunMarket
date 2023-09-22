package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatMapper;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMapper mapper;
    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEntryRepository roomEntryRepository;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, ChatMapper mapper,
                           PostRepository postRepository, ChatMessageRepository chatMessageRepository,
                           ChatRoomEntryRepository roomEntryRepository) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.roomEntryRepository = roomEntryRepository;
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
                .map(e -> e.chatRoomInfo().getChatRoom().getId())
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

        if (findChatRoomInfos.size() < 2){
            chatRoomInfo.getChatRoom().deleteChatRoom();
        }
    }

}
