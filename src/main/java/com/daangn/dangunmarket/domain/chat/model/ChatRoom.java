package com.daangn.dangunmarket.domain.chat.model;

import com.daangn.dangunmarket.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void deleteChatRoom(Long deleteRequestMemberId, List<ChatRoomInfo> chatRoomInfos) {

        if (chatRoomInfos == null) return;

        long removedCnt = chatRoomInfos.stream()
                .filter(chatRoomInfo -> chatRoomInfo.deleteChatRoomInfo(deleteRequestMemberId))
                .count();

        if (hasParticipation(removedCnt, chatRoomInfos.size())) {
            isDeleted = true;
        }

    }

    private boolean hasParticipation(long removedCnt, long participationSize) {
        return removedCnt == participationSize;
    }

}
