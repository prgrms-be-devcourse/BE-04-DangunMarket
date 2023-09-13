package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.area.model.QArea;
import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.repository.dto.QPostDto;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangn.dangunmarket.domain.post.model.QPost.post;
import static com.daangn.dangunmarket.domain.post.model.QPostImage.postImage;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<PostDto> getPostsSimple(Long areaId, Pageable pageable) {
        List<PostDto> postDtos = jpaQueryFactory
                .select(new QPostDto(
                        post,
                        QArea.area.name
                ))
                .from(post)
                .leftJoin(post.postImageList, postImage)
                .join(QArea.area).on(post.areaId.eq(QArea.area.id))
                .where(post.areaId.eq(areaId))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .join(QArea.area).on(post.areaId.eq(QArea.area.id))
                .fetchOne();

        return new PageImpl<>(postDtos, pageable, total);
    }

}
