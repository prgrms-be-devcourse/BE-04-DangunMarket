package com.daangn.dangunmarket.domain.chat.repository.sessioninfo;

import com.daangn.dangunmarket.domain.chat.model.SessionInfo;
import org.springframework.data.repository.CrudRepository;

public interface SessionInfoRedisRepository extends CrudRepository<SessionInfo, String> {

}
