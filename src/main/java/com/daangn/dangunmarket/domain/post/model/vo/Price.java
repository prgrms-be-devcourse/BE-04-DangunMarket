package com.daangn.dangunmarket.domain.post.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    private long value;

    public Price(long value) {
        if (value < 0){
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }

        this.value = value;
    }
}
