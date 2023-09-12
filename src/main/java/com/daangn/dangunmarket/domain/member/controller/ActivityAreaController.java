package com.daangn.dangunmarket.domain.member.controller;

import com.daangn.dangunmarket.domain.auth.jwt.AuthTokenProvider;
import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiResponse;
import com.daangn.dangunmarket.domain.member.controller.mapper.ActivityAreaApiMapper;
import com.daangn.dangunmarket.domain.member.facade.ActivityAreaFacade;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateResponseParam;
import com.daangn.dangunmarket.domain.member.service.dto.ActivityAreaCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/activity-area",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ActivityAreaController {

    private final ActivityAreaFacade activityAreaFacade;
    private final ActivityAreaApiMapper activityAreaApiMapper;

    public ActivityAreaController(ActivityAreaFacade activityAreaFacade, ActivityAreaApiMapper activityAreaApiMapper) {
        this.activityAreaFacade = activityAreaFacade;
        this.activityAreaApiMapper = activityAreaApiMapper;
    }

    @PostMapping
    public ResponseEntity<ActivityAreaCreateApiResponse> createActivityArea(@RequestBody @Valid ActivityAreaCreateApiRequest activityAreaCreateApiRequest
            , Authentication authentication) {
        ActivityAreaCreateRequestParam activityAreaCreateRequestParam = activityAreaApiMapper.toActivityAreaCreateRequestParam(activityAreaCreateApiRequest);

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Long activityAreaId = activityAreaFacade.createActivityArea(activityAreaCreateRequestParam, customUser.memberId());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(activityAreaId)
                .toUri();

        ActivityAreaCreateApiResponse activityAreaCreateApiResponse = activityAreaApiMapper.toActivityAreaCreateApiResponse(activityAreaId);
        return ResponseEntity.created(uri).body(activityAreaCreateApiResponse);
    }

    @GetMapping
    public ResponseEntity<ActivityAreaIsVerifiedApiResponse> isVerifiedActivityArea(@RequestParam Double latitude, @RequestParam Double longitude, Authentication authentication) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        boolean isVerified = activityAreaFacade.isVerifiedActivityArea(longitude, latitude, customUser.memberId());

        ActivityAreaIsVerifiedApiResponse activityAreaIsVerifiedApiResponse = activityAreaApiMapper.toActivityAreaIsVerifiedApiResponse(isVerified);
        return ResponseEntity.ok(activityAreaIsVerifiedApiResponse);
    }

}
