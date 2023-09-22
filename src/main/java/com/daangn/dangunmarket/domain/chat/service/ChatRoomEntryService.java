package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChatRoomEntryService {

    private final ChatRoomEntryRepository roomEntryRepository;

    public ChatRoomEntryService(ChatRoomEntryRepository roomEntryRepository) {
        this.roomEntryRepository = roomEntryRepository;
    }

    public void addMemberToRoom(String roomId, String memberId) {
        roomEntryRepository.addMemberToRoom(roomId, memberId);

    }

    public void removeMemberFromRoom(String roomId, String memberId) {
        roomEntryRepository.removeMemberFromRoom(roomId, memberId);
    }

    public Set<String> getMembersInRoom(String roomId) {
        return roomEntryRepository.getMembersInRoom(roomId);
    }

    public boolean isMemberInRoom(String roomId, String memberId) {
        return roomEntryRepository.isMemberInRoom(roomId, memberId);
    }

}
