package com.daangn.dangunmarket.domain.member.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String message;

    @Convert(converter = StringArrayConverter.class)
    private List<EvaluationItem> evaluation_items = new ArrayList<>();

    public Review(Long id, Long productId, Member member, String message, List<EvaluationItem> evaluation_items) {
        this.id = id;
        this.productId = productId;
        this.member = member;
        this.message = message;
        this.evaluation_items = evaluation_items;
    }

}
