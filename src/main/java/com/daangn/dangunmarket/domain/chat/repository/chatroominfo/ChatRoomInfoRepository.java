package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedPostWithMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ChatRoomInfoRepository {

    Long findSenderId(Long chatRoomId, Long myId);

    /**
     *  인자로 들어온 memberId와 동일한 ChatRoom을 가진 ChatRoomInfo들 조회
     *  (반환되는 값은 인자 memberId를 가진 ChatRoomInfo는 제외하고 반환한다.)
     */
    Slice<JoinedMemberResponse> findMembersInSameChatRooms(Long memberId, Pageable pageable);

    Optional<ChatRoomInfo> findChatRoomInfoByPostIdAndMemberId(Long postId, Long memberId);

    ChatRoomInfo save(ChatRoomInfo chatRoomInfo);

    List<ChatRoomInfo> findByChatRoomId(Long chatRoomId);

    List<ChatRoomInfo> saveAll(List<ChatRoomInfo> chatRoomInfos);

    JoinedPostWithMemberResponse findPostWithMember(Long chatRoomId);

}
