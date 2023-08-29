package com.scoup.server.domain;

import javax.persistence.Embeddable;

@Embeddable
public class CafeIdStamp {
    private Long cafeId;
    private int stamp;

    public Long getCafeId(){
        return this.cafeId;
    }

    public Long getStamp(){
        return this.cafeId;
    }
}
