package com.ebstudy.board_v2.File;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class FileDTO {
    private Long postid;
    private String fileName;
    private String fileRealName;
}
