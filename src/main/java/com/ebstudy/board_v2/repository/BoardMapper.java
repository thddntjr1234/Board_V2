package com.ebstudy.board_v2.repository;

import com.ebstudy.board_v2.web.dto.PostDTO;

import java.util.List;

public interface BoardMapper {

    List<PostDTO> getPostLists(int pageNumber);

    int getPostCount();


}
