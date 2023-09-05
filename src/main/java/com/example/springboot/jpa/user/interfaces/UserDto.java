package com.example.springboot.jpa.user.interfaces;

import com.example.springboot.jpa.user.domain.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserDto {
    @Nullable
    private Long id; // null - create, notnull - update
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private User.Role role;

    public static UserDto fromUser(User user) {
        return UserDto.of(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
