package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.repository.FileDAO;
import com.ebstudy.board_v2.repository.PostDAO;
import com.ebstudy.board_v2.web.Command;
import com.ebstudy.board_v2.web.dto.CategoryDTO;
import com.ebstudy.board_v2.web.dto.PostDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 게시물 조회
 */
public class LoadPostListCommand implements Command {

    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {

        PostDAO postDAO = PostDAO.getInstance();

        int totalPostCount = postDAO.getPostCount();

        Integer pageNumber = 1;
        if (request.getParameter("pageNumber") != null) {
            pageNumber = Integer.valueOf(request.getParameter("pageNumber"));
        }

        // 페이징에 필요한 변수 값 계산
        HashMap<String, Integer> pagingValues = calPagingValues(totalPostCount, pageNumber);

        // 현재 페이지에 대한 게시글 리스트 저장
        List<CategoryDTO> categoryList = postDAO.getCategoryList();
        List<PostDTO> postList = postDAO.getPostList(pagingValues.get("currentPage"));

        // 현재 게시글 리스트별로 파일 소유 여부를 확인
        FileDAO fileDAO = FileDAO.getInstance();
        HashMap<Long, Boolean> postFileFlagList = new HashMap<>();

        for (PostDTO post : postList) {
            long postId = post.getPostId();

            if (fileDAO.checkFileExistence(postId)) {
                postFileFlagList.put(postId, true);
                continue;
            }
            postFileFlagList.put(postId, false);
        }

        // 페이징에 필요한 변수와 카테고리 리스트, 총 게시글 개수와 페이징된 게시글 리스트를 모델에 저장
        request.setAttribute("pagingValues", pagingValues);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("totalPostCount", totalPostCount);
        request.setAttribute("postList", postList);
        request.setAttribute("postFileFlagList", postFileFlagList);

        HashMap<String, String> resultCommandMap = new HashMap<>();

        resultCommandMap.put("command", "forward");
        resultCommandMap.put("viewPath", "/WEB-INF/boards/free/list.jsp");

        return resultCommandMap;
    }

    /**
     * 페이징에 사용되는 변수들을 계산해서 반환
     *
     * @param totalPostCount 총 게시글 개수
     * @param pageNumber     uri 파라미터로 전달받은 요청 페이지 번호
     * @return 시작 페이지, 끝 페이지, 총 페이지 개수와 예외 처리된 현재 페이지 값
     */
    HashMap<String, Integer> calPagingValues(int totalPostCount, Integer pageNumber) {
        HashMap<String, Integer> values = new HashMap<>();

        int totalPage = totalPostCount / 10;
        if (totalPostCount % 10 > 0) {
            totalPage++;
        }

        int currentPage = pageNumber;

        // 유효 페이지 이외의 값을 파라미터로 받게 되는 경우의 예외처리
        if (currentPage > totalPage || currentPage < 0) {
            currentPage = 1;
        }

        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = startPage + 10 - 1;

        // 마지막 페이지에서 페이지 범위 조정
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        values.put("startPage", startPage);
        values.put("endPage", endPage);
        values.put("currentPage", currentPage);
        values.put("totalPage", totalPage);

        return values;
    }

}
