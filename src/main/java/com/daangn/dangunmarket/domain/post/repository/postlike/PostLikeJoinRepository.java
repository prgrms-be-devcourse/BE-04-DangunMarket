package com.daangn.dangunmarket.domain.post.repository.postlike;

import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedWithArea;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangn.dangunmarket.domain.area.model.QArea.area;
import static com.daangn.dangunmarket.domain.post.model.QPost.post;
import static com.daangn.dangunmarket.domain.post.model.QPostLike.postLike;

@Repository
public class PostLikeJoinRepository {

    private final JPAQueryFactory queryFactory;

    public PostLikeJoinRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<JoinedWithArea> findDetailsByMemberId(Long memberId, Pageable pageable){
        int pageSize = pageable.getPageSize();

        List<JoinedWithArea> contents = queryFactory
                .select(Projections.fields(JoinedWithArea.class,
                        post.as("post"),
                        area.as("area")
                ))
                .from(postLike)
                .join(post)
                .join(area).on(post.areaId.eq(area.id))
                .where(postLike.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageSize){
            contents.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

}
