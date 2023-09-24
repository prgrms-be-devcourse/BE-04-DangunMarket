package com.daangn.dangunmarket.domain.chat.service.mapper;

import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.MessageType;
import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessageResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.chat.service.dto.SessionInfoSaveRequest;
import com.daangn.dangunmarket.domain.member.model.Member;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatMapper {

    default ChatRoomsFindResponses toChatRoomsFindResponses(Slice<JoinedMemberResponse> roomInfoWithMembers, List<ChatMessage> chatMessages) {
        Slice<ChatRoomsFindResponse> mappedResponses = roomInfoWithMembers
                .map((roomInfoWithMember) -> {
                    Long chatRoomId = roomInfoWithMember.chatRoomInfo().getChatRoomId();
                    Member member = roomInfoWithMember.member();

                    ChatMessage chatMessage = chatMessages.stream()
                            .filter(m -> Objects.equals(m.getChatRoomId(), chatRoomId))
                            .findFirst()
                            .orElseGet(() -> createDefaultMessage(chatRoomId));

                    Integer readOrNot = chatMessage.getReadOrNot();
                    if (member.isNotMemberId(chatMessage.getMemberId())) {
                        readOrNot = 0;
                    }

                    return new ChatRoomsFindResponse(
                            chatRoomId,
                            member.getNickName(),
                            chatMessage.getMessage(),
                            readOrNot,
                            chatMessage.getCreatedAt());
                });

        return ChatRoomsFindResponses.from(mappedResponses);
    }

    private static ChatMessage createDefaultMessage(Long chatRoomId) {
        return new ChatMessage(
                chatRoomId,
                null,
                "",
                null,
                1,
                MessageType.TALK);
    }

    default ChatMessage toEntity(Long memberId, MessageType type, Integer readOrNot, Long chatRoomId, MessageRequest request) {
        return ChatMessage.builder()
                .message(request.message())
                .type(type)
                .readOrNot(readOrNot)
                .imageUrls(request.imageUrls())
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .build();
    }

    @Mapping(source = "chatMessage.id", target = "chatMessageId")
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    SessionInfo toSessionInfo(SessionInfoSaveRequest request);
}
