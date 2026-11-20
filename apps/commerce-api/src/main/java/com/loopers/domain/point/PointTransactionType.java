package com.loopers.domain.point;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointTransactionType {
    CHARGE("충전"),
    DEDUCT("차감");

    private final String description;
}
