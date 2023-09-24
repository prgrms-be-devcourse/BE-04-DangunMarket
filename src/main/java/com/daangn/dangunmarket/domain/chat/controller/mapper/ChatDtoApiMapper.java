package com.daangn.dangunmarket.domain.chat.controller.mapper;

import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCheckInApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveParamRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatDtoApiMapper {

    ChatRoomCreateRequest toChatRoomCreateRequest(ChatRoomCreateApiRequest chatRoomCreateApiRequest, Long memberId);
    ChatRoomCreateApiResponse toChatRoomCreateApiResponse(Long chatRoomId);

    ChatRoomCheckInApiResponse toChatRoomCheckInApiResponse(ChatRoomCheckInParamResponse chatRoomCheckInParamResponse);
    SessionInfoSaveParamRequest toSessionInfoSaveParamRequest(String sessionId, Long roomId, Long memberId);
}
