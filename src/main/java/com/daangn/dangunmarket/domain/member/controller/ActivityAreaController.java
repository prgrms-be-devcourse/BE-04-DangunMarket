package com.daangn.dangunmarket.domain.member.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaCreateApiResponse;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiRequest;
import com.daangn.dangunmarket.domain.member.controller.dto.ActivityAreaIsVerifiedApiResponse;
import com.daangn.dangunmarket.domain.member.controller.mapper.ActivityAreaApiMapper;
import com.daangn.dangunmarket.domain.member.facade.ActivityAreaFacade;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaCreateRequestParam;
import com.daangn.dangunmarket.domain.member.facade.dto.ActivityAreaIsVerifiedRequestParam;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/activity-area",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ActivityAreaController {

    private final ActivityAreaFacade activityAreaFacade;
    private final ActivityAreaApiMapper activityAreaApiMapper;

    public ActivityAreaController(
            ActivityAreaFacade activityAreaFacade,
            ActivityAreaApiMapper activityAreaApiMapper) {
        this.activityAreaFacade = activityAreaFacade;
        this.activityAreaApiMapper = activityAreaApiMapper;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityAreaCreateApiResponse> createActivityArea(
            @RequestBody @Valid ActivityAreaCreateApiRequest activityAreaCreateApiRequest,
            Authentication authentication)
    {
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
    public ResponseEntity<ActivityAreaIsVerifiedApiResponse> isVerifiedActivityArea(
            @ModelAttribute ActivityAreaIsVerifiedApiRequest request,
            Authentication authentication)
    {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        ActivityAreaIsVerifiedRequestParam requestParam = activityAreaApiMapper.toActivityAreaIsVerifiedRequestParam(request);
        boolean isVerified = activityAreaFacade.isVerifiedActivityArea(requestParam, customUser.memberId());

        ActivityAreaIsVerifiedApiResponse activityAreaIsVerifiedApiResponse = activityAreaApiMapper.toActivityAreaIsVerifiedApiResponse(isVerified);
        return ResponseEntity.ok(activityAreaIsVerifiedApiResponse);
    }

}
