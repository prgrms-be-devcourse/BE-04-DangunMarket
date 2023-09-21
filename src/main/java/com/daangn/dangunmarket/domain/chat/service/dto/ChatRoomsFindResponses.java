package com.daangn.dangunmarket.domain.chat.service.dto;

import org.springframework.data.domain.Slice;

public record ChatRoomsFindResponses(Slice<ChatRoomsFindResponse> resposes) {

    public static ChatRoomsFindResponses from(Slice<ChatRoomsFindResponse> responses) {
        return new ChatRoomsFindResponses(responses);
    }

}