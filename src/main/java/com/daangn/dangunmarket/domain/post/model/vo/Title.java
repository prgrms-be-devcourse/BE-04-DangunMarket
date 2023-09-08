package com.daangn.dangunmarket.domain.post.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private String title;

    public Title(String title) {
        Assert.notNull(title, "title은 null이 될 수 없습니다.");
        if (title.isBlank()) {
            throw new IllegalArgumentException("title은 공백이 될 수 없습니다.");
        }

        this.title = title;
    }
}
