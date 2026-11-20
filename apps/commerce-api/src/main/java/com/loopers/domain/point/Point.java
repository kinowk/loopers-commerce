package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @Column(name = "point_id", nullable = false, updatable = false)
    private Long pointId;

    @Column(name = "ref_user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private PointTransactionType type;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private Point(Long pointId, Long userId, PointTransactionType type, Long amount, Long balance) {
        validateTransactionType(type);
        validateAmount(amount);
        validateBalance(balance);

        this.pointId = pointId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public static Point of(Long pointId, Long userId, PointTransactionType type, Long amount, Long balance) {
        return new Point(pointId, userId, type, amount, balance);
    }

    public long charge(Long amount) {
        validateAmount(amount);

        if (balance > Long.MAX_VALUE - amount) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트 한도를 초과할 수 없습니다.");
        }

        balance += amount;
        return balance;
    }

    public long deduct(Long amount) {
        validateAmount(amount);

        if (balance - amount < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }

        balance -= amount;
        return balance;
    }

    private void validateTransactionType(PointTransactionType type) {
        if (type == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }

    private void validateAmount(Long amount) {
        if (amount == null || amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0원 보다 커야 합니다.");
        }
    }

    private void validateBalance(Long balance) {
        if (balance == null || balance < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "잔액은 0원 이상이어야 합니다.");
        }
    }
}
