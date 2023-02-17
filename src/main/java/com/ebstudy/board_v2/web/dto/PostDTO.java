package com.ebstudy.board_v2.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class PostDTO {

    // TODO: Builder 어노테이션을 사용하지 못하게 되었기 때문에 각 요청/반환 타입에 맞는 DTO들을 만들어야 한다 왜?
    // -> Mybatis가 setter 주입을 강제하기 때문, OCP가 중요하다고 하지만 프레임워크의 원칙과 충돌하면서 빌더 패턴을 사용해야 할 이유는 없음
    private Long postId;
    private Long hits;
    private Long categoryId;
    private Boolean isHaveFile;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private String author;
    private String category;
    private String passwd;
    private String secondPasswd;
}
