package com.daangn.dangunmarket.domain.chat.controller.dto;

import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageResponse;
import java.util.List;

public record ChatMessagePageApiResponses(
        List<ChatMessagePageResponse> chatMessagePageResponses
) {
}
