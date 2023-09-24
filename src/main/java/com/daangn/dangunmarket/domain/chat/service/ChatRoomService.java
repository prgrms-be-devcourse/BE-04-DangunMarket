package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto.ChatMessagePageDto;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedPostWithMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.*;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatDtoMapper;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatMapper;
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
    private final ChatMapper mapper;
    private final ChatDtoMapper chatDtoMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEntryRepository roomEntryRepository;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, ChatMapper mapper,
                           ChatDtoMapper chatDtoMapper, ChatMessageRepository chatMessageRepository,
                           ChatRoomEntryRepository roomEntryRepository) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.mapper = mapper;
        this.chatDtoMapper = chatDtoMapper;
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


    public int readAllMessage(Long chatRoomId, Long memberId) {
        Long senderId = chatRoomInfoRepository.findSenderId(chatRoomId, memberId);

        List<ChatMessage> notReadMessages = chatMessageRepository.findNotReadMessageByChatRoomIdAndSenderId(chatRoomId, senderId);
        chatMessageRepository.markMessagesAsRead(notReadMessages.stream().map(ChatMessage::getId).toList());

        return notReadMessages.size();
    }

    public void addMemberToRoom(String chatRoomId, String memberId) {
        roomEntryRepository.addMemberToRoom(chatRoomId, memberId);
    }

    public ChatWithPostAndMemberResponse findPostWithMember(Long chatRoomId) {
        JoinedPostWithMemberResponse postWithMember = chatRoomInfoRepository.findPostWithMember(chatRoomId);
        return chatDtoMapper.toChatWithPostAndMemberResponse(postWithMember);
    }

    public ChatRoomsFindResponses findChatRoomsByMemberId(Long memberId, Pageable pageable) {
        Slice<JoinedMemberResponse> roomInfoWithMembers = chatRoomInfoRepository.findMembersInSameChatRooms(memberId, pageable);

        List<Long> chatRoomIds = roomInfoWithMembers.getContent().stream()
                .map(e -> e.chatRoomInfo().getChatRoom().getId())
                .toList();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIds(chatRoomIds);

        return mapper.toChatRoomsFindResponses(roomInfoWithMembers, chatMessages);
    }

    public void deleteChatRoomByIdAndMemberId(Long chatRoomId, Long deleteRequestMemberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ENTITY));
        List<ChatRoomInfo> findChatRoomInfos = chatRoomInfoRepository.findByChatRoomId(chatRoomId);

        chatRoom.deleteChatRoom(deleteRequestMemberId, findChatRoomInfos);
    }

    public List<ChatMessagePageResponse> findByChatRoomIdWithPagination(ChatMessagePageRequest chatMessageRequest, Long memberId) {
        ChatMessagePageDto chatMessagePageDto = chatDtoMapper.toChatMessagePageDto(chatMessageRequest);

        return chatMessageRepository
                .findByChatRoomIdWithPagination(chatMessagePageDto)
                .stream()
                .map(p->chatDtoMapper.toChatMessagePageResponse(p,memberId))
                .toList();
    }
}
