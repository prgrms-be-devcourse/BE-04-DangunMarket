package com.daangn.dangunmarket.domain.post.controller.dto.post;

import com.daangn.dangunmarket.domain.post.model.TradeStatus;
import jakarta.validation.constraints.NotNull;

public record PostUpdateStatusApiRequest(
        @NotNull(message = "tradeStatus는 null이 될 수 없습니다.") TradeStatus tradeStatus) {
}
