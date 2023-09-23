package com.daangn.dangunmarket.domain.chat.controller.dto;

import jakarta.validation.constraints.NotNull;

public record SessionInfoSaveApiRequest(@NotNull Long roomId) {
}
