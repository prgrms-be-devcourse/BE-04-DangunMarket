package com.daangn.dangunmarket.domain.chat.repository.chatentry;

import java.util.Set;

public interface ChatRoomEntryRepository {

    void addMemberToRoom(String roomId, String memberId);

    void removeMemberFromRoom(String roomId, String memberId);

    Set<String> getMembersInRoom(String roomId);

    boolean isMemberInRoom(String roomId, String memberId);

}
