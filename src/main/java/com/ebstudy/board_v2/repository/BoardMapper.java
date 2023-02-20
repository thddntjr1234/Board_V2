package com.ebstudy.board_v2.repository;

import com.ebstudy.board_v2.web.dto.CategoryDTO;
import com.ebstudy.board_v2.web.dto.CommentDTO;
import com.ebstudy.board_v2.web.dto.FileDTO;
import com.ebstudy.board_v2.web.dto.PostDTO;

import java.util.List;

public interface BoardMapper {

    List<PostDTO> getPostList(int pageNumber);

    int getPostCount();

    PostDTO getPost(long postId);

    List<CategoryDTO> getCategoryList();

    long savePost(PostDTO post);

    void increaseHits(long postId);

    List<FileDTO> getFileList(long postId);

    boolean checkFileExistence(long postId);

    void saveFile(FileDTO file);

    List<CommentDTO> getCommentList(long postId);

    void saveComment(CommentDTO comment);
}
