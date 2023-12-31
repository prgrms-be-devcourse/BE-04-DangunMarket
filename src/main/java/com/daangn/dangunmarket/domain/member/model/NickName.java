package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickName {

    private String nickName;

    public NickName(String nickName) {
        validateName(nickName);
        this.nickName = nickName;
    }

    private void validateName(String nickName) {
        if (nickName == null || nickName.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NickName nickName1 = (NickName) o;
        return Objects.equals(nickName, nickName1.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }

}
