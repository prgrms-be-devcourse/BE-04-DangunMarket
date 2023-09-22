package com.daangn.dangunmarket.domain.chat.repository.chatentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    public void addMemberToRoom(String roomId, String memberId) {
        redisTemplate.opsForSet().add(getRoomKey(roomId), memberId);
    }

    public void removeMemberFromRoom(String roomId, String memberId) {
        redisTemplate.opsForSet().remove(getRoomKey(roomId), memberId);
    }

    public Set<String> getMembersInRoom(String roomId) {
        return redisTemplate.opsForSet().members(getRoomKey(roomId));
    }

    public boolean isMemberInRoom(String roomId, String memberId) {
        return redisTemplate.opsForSet().isMember(getRoomKey(roomId), memberId);
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

    private String getRoomKey(String roomId) {
        return CHAT_ROOM_KEY_PREFIX + roomId;
    }

}
