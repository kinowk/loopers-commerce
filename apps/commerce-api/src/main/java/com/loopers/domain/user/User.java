package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "login_id", nullable = false, updatable = false)
    private String loginId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern LOGIN_ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{1,10}$");

    private User(
            Long userId,
            String loginId,
            String name,
            Gender gender,
            LocalDate birthDate,
            String email
    ) {
        validateLoginId(loginId);
        validateGender(gender);
        validateBirthDate(birthDate);
        validateEmail(email);

        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public static User of(Long userId, String loginId, String name, Gender gender, LocalDate birthDate, String email) {
        return new User(userId, loginId, name, gender, birthDate, email);
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    private void validateLoginId(String loginId) {
        if (!StringUtils.hasText(loginId) || !LOGIN_ID_PATTERN.matcher(loginId).matches()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID 형식이 올바르지 않습니다. ID는 영문 및 숫자 10자 이내로 입력하시기 바랍니다.");
        }
    }

    private void validateGender(Gender gender) {
        if (gender == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 필수입니다.");
        }
        if (LocalDate.now().isAfter(birthDate)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 미래일 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 올바르지 않습니다.");
        }
    }
}
