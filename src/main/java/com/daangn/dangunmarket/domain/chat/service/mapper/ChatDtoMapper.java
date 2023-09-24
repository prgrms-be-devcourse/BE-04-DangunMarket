package com.daangn.dangunmarket.domain.chat.service.mapper;

import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.dto.ChatMessagePageDto;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedPostWithMemberResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatWithPostAndMemberResponse;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatDtoMapper {

    ChatMessagePageDto toChatMessagePageDto(ChatMessagePageRequest chatMessagePageRequest);

    @Mapping(source = "chatMessage.id", target = "messageId")
    @Mapping(target = "isMine", expression = "java(isMineCheck(chatMessage, memberId))")
    @Mapping(target = "createdAt", source = "chatMessage.createdAt", qualifiedByName = "formatLocalDateTime")
    ChatMessagePageResponse toChatMessagePageResponse(ChatMessage chatMessage, Long memberId);

    default boolean isMineCheck(ChatMessage chatMessage, Long memberId) {
        return chatMessage.getMemberId().equals(memberId);
    }

    @Named("formatLocalDateTime")
    default String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    default ChatWithPostAndMemberResponse toChatWithPostAndMemberResponse(JoinedPostWithMemberResponse joinedPostWithMemberResponse){
       return new ChatWithPostAndMemberResponse(
               joinedPostWithMemberResponse.post().getId(),
               firstPostImage(joinedPostWithMemberResponse),
               joinedPostWithMemberResponse.post().getTradeStatus(),
               joinedPostWithMemberResponse.post().getTitle(),
               joinedPostWithMemberResponse.post().getPrice(),
               joinedPostWithMemberResponse.post().isOfferAllowed(),
               joinedPostWithMemberResponse.member().getNickName(),
               joinedPostWithMemberResponse.member().getReviewScore()
       );
    }

    @Named("firstPostImage")
    default String firstPostImage(JoinedPostWithMemberResponse joinedPostWithMemberResponse) {
        List<PostImage> postImages = joinedPostWithMemberResponse.post().getPostImages();
        if (postImages != null && !postImages.isEmpty()) {
            return postImages.get(0).getUrl();
        }
        return "";
    }

}
