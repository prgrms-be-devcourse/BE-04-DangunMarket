package com.daangn.dangunmarket.domain.chat.service.mapper;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatMapper {

    default ChatRoomsFindResponses toChatRoomsFindResponses(Slice<JoinedMemberResponse> roomInfoWithMembers, List<ChatMessage> chatMessages){
        Slice<ChatRoomsFindResponse> mappedResponses = roomInfoWithMembers
                .map((roomInfoWithMember) -> {
                    Long chatRoomId = roomInfoWithMember.chatRoomInfo().getChatRoom().getId();

                    ChatMessage chatMessage = chatMessages.stream()
                            .filter(e -> Objects.equals(e.getChatRoomId(), chatRoomId))
                            .findFirst()
                            .orElseGet(() -> new ChatMessage(
                                    chatRoomId,
                                    "",
                                    null,
                                    "",
                                    "",
                                    1));

                    Integer readOrNot = chatMessage.getReadOrNot();
                    if (!Objects.equals(roomInfoWithMember.member().getId(), chatMessage.getMemberId())){
                        readOrNot = 0;
                    }

                    return new ChatRoomsFindResponse(
                            chatRoomId,
                            roomInfoWithMember.member().getNickName(),
                            chatMessage.getMessage(),
                            readOrNot,
                            chatMessage.getCreatedAt());
                });

        return ChatRoomsFindResponses.from(mappedResponses);
    }

}
