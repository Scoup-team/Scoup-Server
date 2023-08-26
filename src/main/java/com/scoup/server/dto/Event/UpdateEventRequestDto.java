package com.scoup.server.dto.Event;

import com.scoup.server.domain.Cafe;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
public class UpdateEventRequestDto {
    private String content;
}
