package com.daangn.dangunmarket.domain.member.member.domain;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class NickName {

    private String nickName;


    protected NickName() { }

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


    public void setNickName(String nickName) {
        this.nickName = nickName;
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
