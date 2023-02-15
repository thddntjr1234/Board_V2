package com.ebstudy.board_v2.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class PostDTO {

    // TODO: Builder 어노테이션을 사용하지 못하게 되었기 때문에 각 요청/반환 타입에 맞는 DTO들을 만들어야 한다
    private Long postId;
    private Long hits;
    private Long categoryId;
    private Boolean isHaveFile;
    private String createdDate;
    private String modifiedDate;
    private String title;
    private String content;
    private String author;
    private String category;
    private String passwd;
}
