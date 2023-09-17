package com.daangn.dangunmarket.domain.chat.service.mapper;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
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

    default ChatRoomsFindResponses toChatRoomsFindResponses(Slice<ChatRoomInfo> chatRoomInfos, List<ChatMessage> chatMessages){
        Slice<ChatRoomsFindResponse> mappedResponses = chatRoomInfos
                .map((chatRoomInfo) -> {
                    Long chatRoomInfoId = chatRoomInfo.getId();
                    ChatMessage chatMessage = chatMessages.stream()
                            .filter(e -> Objects.equals(e.getChatRoomInfoId(), chatRoomInfoId))
                            .findFirst()
                            .orElseGet(() -> {
                                return new ChatMessage(
                                        chatRoomInfoId,
                                        "",
                                        null,
                                        "",
                                        "",
                                        true);
                            });

                    boolean readOrNot = chatMessage.isReadOrNot();
                    if (!Objects.equals(chatRoomInfo.getMember().getId(), chatMessage.getMemberId())){
                        readOrNot = false;
                    }

                    return new ChatRoomsFindResponse(
                            chatRoomInfoId,
                            chatRoomInfo.getMember().getNickName(),
                            chatMessage.getMessage(),
                            readOrNot,
                            chatMessage.getCreatedAt());
                });

        return ChatRoomsFindResponses.from(mappedResponses);
    }

}
