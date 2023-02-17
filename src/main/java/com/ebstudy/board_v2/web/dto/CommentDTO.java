package com.ebstudy.board_v2.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class CommentDTO {
    private Long postId;
    private LocalDateTime createdDate;
    private String comment;
}
