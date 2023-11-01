package com.daangn.dangunmarket.domain.auth.client;

import com.daangn.dangunmarket.domain.member.model.Member;

public interface ClientProxy {

    Member getUserData(String accessToken);
}
