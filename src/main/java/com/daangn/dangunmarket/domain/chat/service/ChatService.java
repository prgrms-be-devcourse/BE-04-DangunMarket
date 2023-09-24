package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.MessageType;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessageResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.chat.service.mapper.ChatMapper;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ChatService {

    private static final int FILE_SIZE = 3;
    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEntryRedisRepository chatRoomEntryRedisRepository;
    private final S3Uploader s3Uploader;
    private final ChatMapper mapper;

    public ChatService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, ChatRoomEntryRedisRepository chatRoomEntryRedisRepository, S3Uploader s3Uploader, ChatMapper mapper) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomEntryRedisRepository = chatRoomEntryRedisRepository;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    public ChatRoomsFindResponses findChatRoomsByMemberId(Long memberId, Pageable pageable) {
        Slice<JoinedMemberResponse> roomInfoWithMembers = chatRoomInfoRepository.findMembersInSameChatRooms(memberId, pageable);

        List<Long> chatRoomIds = roomInfoWithMembers.getContent().stream()
                .map(e -> e.chatRoomInfo().getChatRoom().getId())
                .toList();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIds(chatRoomIds);

        return mapper.toChatRoomsFindResponses(roomInfoWithMembers, chatMessages);
    }


    public List<ImageInfo> uploadImages(List<MultipartFile> files) {
        if (!isFileCountWithinLimit(files.size())) {
            throw new IllegalArgumentException("파일은 최대 3개까지 업로드할 수 있습니다.");
        }
        return s3Uploader.saveImages(files);
    }

    public ChatMessageResponse saveMessage(Long memberId, Long chatRoomId, MessageRequest messageRequest) {
        //MessageType
        MessageType messageType = MessageType.TALK;
        if (!messageRequest.imageUrls().isEmpty()) {
            messageType = MessageType.IMAGE;
        }

        //접속 여부 확인
        int readOrNot = 1;
        Long theOtherMemberId = chatRoomInfoRepository.findSenderIdByChatRoomInfoAndMyId(chatRoomId, memberId);
        if (chatRoomEntryRedisRepository.isMemberInRoom(chatRoomId.toString(), theOtherMemberId.toString())) {
            readOrNot = 0;
        }

        ChatMessage chatMessage = mapper.toEntity(memberId, messageType, readOrNot, chatRoomId, messageRequest);
        chatMessage = chatMessageRepository.save(chatMessage);

        return mapper.toChatMessageResponse(chatMessage);
    }

    private boolean isFileCountWithinLimit(int size) {
        return (size <= FILE_SIZE);
    }
}
