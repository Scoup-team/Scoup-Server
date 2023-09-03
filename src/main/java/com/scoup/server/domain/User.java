package com.scoup.server.domain;

import com.scoup.server.dto.user.UpdateUserPasswordRequestDto;
import com.scoup.server.dto.user.UpdateUserRequestDto;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean master;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name="cafe_id_list", joinColumns=@JoinColumn(name="user_id"))
    private List<Long> cafeIdList=new ArrayList<>();

    public void updateUser(UpdateUserRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }

    public void updateUserPassword(UpdateUserPasswordRequestDto requestDto) {
        this.password = requestDto.getNewPassword();
    }

}
