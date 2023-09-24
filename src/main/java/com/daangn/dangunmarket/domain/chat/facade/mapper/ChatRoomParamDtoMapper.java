package com.daangn.dangunmarket.domain.chat.facade.mapper;

import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveFacaRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.SessionInfoSaveRequest;
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

    @Mapping(target = "productUrl", source = "postFindResponse", qualifiedByName = "firstPostImage")
    @Mapping(target = "tradeState", source = "postFindResponse.tradeStatus")
    ChatRoomCheckInParamResponse toChatRoomCheckInParamResponse(PostFindResponse postFindResponse, MemberFindResponse memberFindResponse);

    @Named("firstPostImage")
    default String firstPostImage(PostFindResponse postFindResponse) {
        List<PostImage> postImages = postFindResponse.postImageList();
        if (postImages != null && !postImages.isEmpty()) {
            return postImages.get(0).getUrl();
        }
        return "";
    }

    SessionInfoSaveRequest toSessionInfoSaveRequest(SessionInfoSaveFacaRequest request);
}

