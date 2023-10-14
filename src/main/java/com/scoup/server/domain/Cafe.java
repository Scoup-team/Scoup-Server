package com.scoup.server.domain;

import com.scoup.server.dto.admin.PatchAdminCafeRequestDto;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scoup.server.dto.mainPage.UpdateMainPageRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String name;

    @Column
    private String imageUrl;

    @Column
    private String licenseeNumber;

    @Column
    private String runningTime;

    @Column
    private String phoneNumber;

    @JsonIgnoreProperties({"cafe"})
    @OneToMany(mappedBy="cafe", cascade = CascadeType.REMOVE)
    private List<Event> eventList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="master_Id")
    private User user;

    public void patchCafe(PatchAdminCafeRequestDto requestDto){
        this.name = requestDto.getName();
        this.location = requestDto.getLocation();
        this.licenseeNumber = requestDto.getLicenseeNumber();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.runningTime = requestDto.getRunningTime();
        this.imageUrl=requestDto.getImageUrl();
    }

}
