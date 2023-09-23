package com.daangn.dangunmarket.domain.chat.facade.mapper;

import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveFacaRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.SessionInfoSaveRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ChatFacadeMapper {
    SessionInfoSaveRequest toSessionInfoSaveRequest(SessionInfoSaveFacaRequest request);
}
