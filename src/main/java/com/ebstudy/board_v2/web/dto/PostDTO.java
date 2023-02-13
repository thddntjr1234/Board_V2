package com.ebstudy.board_v2.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Builder @ToString
public class PostDTO {
    private Long id;
    private Long hits;
    private Boolean isHaveFile;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private String author;
    private String category;
    private String passwd;

}
