package com.daangn.dangunmarket.domain.chat.facade.mapper;

import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatWithPostAndMemberResponse;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatRoomParamDtoMapper {

    ChatRoomCheckInParamResponse toChatRoomCheckInParamResponse(ChatWithPostAndMemberResponse chatWithPostAndMemberResponse);

}

