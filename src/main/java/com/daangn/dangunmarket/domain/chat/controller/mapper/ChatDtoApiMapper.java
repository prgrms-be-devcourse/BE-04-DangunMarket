package com.daangn.dangunmarket.domain.chat.controller.mapper;

import com.daangn.dangunmarket.domain.chat.controller.dto.*;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveParamRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatDtoApiMapper {

    ChatRoomCreateRequest toChatRoomCreateRequest(ChatRoomCreateApiRequest chatRoomCreateApiRequest, Long memberId);
    ChatRoomCreateApiResponse toChatRoomCreateApiResponse(Long chatRoomId);

    ChatRoomCheckInApiResponse toChatRoomCheckInApiResponse(ChatRoomCheckInParamResponse chatRoomCheckInParamResponse);

    ChatMessagePageRequest toChatMessagePageRequest(ChatMessagePageApiRequest chatMessagePageApiRequest, Long chatRoomId);

    default ChatMessagePageApiResponses toChatMessagePageApiResponses(List<ChatMessagePageResponse> chatMessagePageResponses){
        return new ChatMessagePageApiResponses(chatMessagePageResponses);
    }

    SessionInfoSaveParamRequest toSessionInfoSaveParamRequest(String sessionId, Long roomId, Long memberId);

}
