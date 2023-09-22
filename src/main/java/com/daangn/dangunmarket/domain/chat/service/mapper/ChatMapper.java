package com.daangn.dangunmarket.domain.chat.service.mapper;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.member.model.Member;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
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
                    Long chatRoomId = roomInfoWithMember.chatRoomInfo().getChatRoom().getId();
                    Member member = roomInfoWithMember.member();

                    ChatMessage chatMessage = chatMessages.stream()
                            .filter(e -> Objects.equals(e.getChatRoomId(), chatRoomId))
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
                1);
    }

}