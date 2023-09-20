package com.daangn.dangunmarket.domain.chat.controller.mapper;

import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatDtoApiMapper {

    ChatRoomCreateRequest toChatRoomCreateRequest(ChatRoomCreateApiRequest chatRoomCreateApiRequest);
    ChatRoomCreateApiResponse toChatRoomCreateApiResponse(Long chatRoomId);
}
