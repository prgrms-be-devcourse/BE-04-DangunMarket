package com.daangn.dangunmarket.domain.chat.repository.chatentry;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class ChatRoomEntryRepositoryImpl implements ChatRoomEntryRepository {

    private final ChatRoomEntryRedisRepository chatEntryRedisRepository;

    public ChatRoomEntryRepositoryImpl(ChatRoomEntryRedisRepository chatEntryRedisRepository) {
        this.chatEntryRedisRepository = chatEntryRedisRepository;
    }

    @Override
    public void addMemberToRoom(String roomId, String memberId) {
        chatEntryRedisRepository.addMemberToRoom(roomId, memberId);
    }

    @Override
    public void removeMemberFromRoom(String roomId, String memberId) {
        chatEntryRedisRepository.removeMemberFromRoom(roomId, memberId);
    }

    @Override
    public Set<String> getMembersInRoom(String roomId) {
        return chatEntryRedisRepository.getMembersInRoom(roomId);
    }

    @Override
    public boolean isMemberInRoom(String roomId, String memberId) {
        return chatEntryRedisRepository.isMemberInRoom(roomId, memberId);
    }

    @Override
    public void deleteChatRoomEntryByRoomId(String roomId) {
        chatEntryRedisRepository.deleteChatRoomEntryByRoomId(roomId);
    }

}
