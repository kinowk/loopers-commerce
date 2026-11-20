package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLike {

    @Id
    @Column(name = "product_like_id", nullable = false, updatable = false)
    private Long productLikeId;

    @Column(name = "ref_product_id", nullable = false, updatable = false)
    private Long productId;

    @Column(name = "ref_user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private ProductLike(Long productLikeId, Long productId, Long userId) {
        validateProductId(productId);
        validateUserId(userId);

        this.productLikeId = productLikeId;
        this.productId = productId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public static ProductLike of(Long productLikeId, Long productId, Long userId) {
        return new ProductLike(productLikeId, productId, userId);
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }
}
