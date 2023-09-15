package com.daangn.dangunmarket.domain.post.repository.post;

import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangn.dangunmarket.domain.post.model.QPost.post;
import static com.daangn.dangunmarket.domain.post.model.QPostImage.postImage;
import static org.springframework.util.StringUtils.hasText;

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
                .leftJoin(post.postImages.postImageList, postImage)
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

    public Page<PostDto> getPostsByConditions(Long areaId, PostSearchConditionRequest conditions) {
        List<PostDto> postDtos = jpaQueryFactory
                .select(new QPostDto(
                        post,
                        QArea.area.name
                ))
                .from(post)
                .leftJoin(post.postImages.postImageList, postImage)
                .join(QArea.area).on(post.areaId.eq(QArea.area.id))
                .where(post.areaId.eq(areaId),
                        eqTitle(conditions.keyword()),
                        eqContent(conditions.keyword()),
                        goePrice(conditions.minPrice()),
                        loePrice(conditions.maxPrice()),
                        eqCategory(conditions.category()))
                .orderBy(postSort(conditions.pageable()))
                .offset(conditions.pageable().getOffset())
                .limit(conditions.pageable().getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .join(QArea.area).on(post.areaId.eq(QArea.area.id))
                .fetchOne();

        return new PageImpl<>(postDtos, conditions.pageable(), total);
    }


    //조건1. 제목
    private BooleanExpression eqTitle(String keyword) {
        if (hasText(keyword)) {
            return post.title.title.contains(keyword);
        }
        return null;
    }

    //조건2. 내용
    private BooleanExpression eqContent(String keyword) {
        if (hasText(keyword)) {
            return post.content.contains(keyword);
        }
        return null;
    }

    //조건3. 가격 (minPrice)
    private BooleanExpression goePrice(Long minPrice) {
        if (minPrice != null) {
            return post.price.value.goe(minPrice);
        }
        return null;
    }

    //조건4. 가격 (maxPrice)
    private BooleanExpression loePrice(Long maxPrice) {
        if (maxPrice != null) {
            return post.price.value.loe(maxPrice);
        }
        return null;
    }

    //조건5. 카테고리
    private BooleanExpression eqCategory(Integer category) {
        if (category != null) {
            return post.category.id.eq(Long.valueOf(category));
        }
        return null;
    }

    //정렬
    private OrderSpecifier<?> postSort(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return new OrderSpecifier(Order.DESC, post.refreshedAt);
        }

        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "date":
                    return new OrderSpecifier(Order.DESC, post.refreshedAt);
                case "popular":
                    return new OrderSpecifier(Order.DESC, post.likeCount);
            }
        }
        return null;
    }
}
