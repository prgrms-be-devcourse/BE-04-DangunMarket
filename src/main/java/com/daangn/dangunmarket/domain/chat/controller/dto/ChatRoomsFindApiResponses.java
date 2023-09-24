package com.daangn.dangunmarket.domain.chat.controller.dto;

import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import org.springframework.data.domain.Slice;

public record ChatRoomsFindApiResponses(Slice<ChatRoomsFindApiResponse> chatRoomsFindApiResponses) {

    public static ChatRoomsFindApiResponses from(ChatRoomsFindResponses responses) {
        Slice<ChatRoomsFindApiResponse> mapResponses = responses.responses()
                .map(e -> new ChatRoomsFindApiResponse(
                        e.chatRoomId(),
                        e.otherMemberName(),
                        e.latestMessage(),
                        e.readOrNot(),
                        e.createdAt()
                ));

        return new ChatRoomsFindApiResponses(mapResponses);
    }

}
