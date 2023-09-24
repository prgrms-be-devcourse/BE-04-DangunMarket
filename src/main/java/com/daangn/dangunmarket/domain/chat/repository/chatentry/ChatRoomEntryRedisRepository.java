package com.daangn.dangunmarket.domain.chat.repository.chatentry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class ChatRoomEntryRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String CHAT_ROOM_KEY_PREFIX = "ChatRoomEntry:";

    @Autowired
    public ChatRoomEntryRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private SetOperations<String, String> getSetOperations() {
        return redisTemplate.opsForSet();
    }

    private String getRoomKey(String roomId) {
        return CHAT_ROOM_KEY_PREFIX + roomId;
    }

    public void addMemberToRoom(String roomId, String memberId) {
        getSetOperations().add(getRoomKey(roomId), memberId);
    }

    public void removeMemberFromRoom(String roomId, String memberId) {
        getSetOperations().remove(getRoomKey(roomId), memberId);
    }

    public Set<String> getMembersInRoom(String roomId) {
        return getSetOperations().members(getRoomKey(roomId));
    }

    public boolean isMemberInRoom(String roomId, String memberId) {

        Boolean isMember = getSetOperations().isMember(getRoomKey(roomId), memberId);

        return isMember != null && isMember;
    }

    public void deleteAllWithPrefix() {
        Set<String> keys = redisTemplate.keys(CHAT_ROOM_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteChatRoomEntryByRoomId(String roomId) {
        redisTemplate.delete(getRoomKey(roomId));
    }

}
