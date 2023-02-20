package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.repository.PostDAO;
import com.ebstudy.board_v2.web.Command;
import com.ebstudy.board_v2.web.dto.CategoryDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoadWriteFormCommand implements Command {

    /**
     * 글쓰기 폼에 필요한 데이터와 함께 viewPath 반환하는 메소드
     * @param request
     * @param response
     * @return viewPath
     */
    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {

        PostDAO postDAO = PostDAO.getInstance();
        HashMap<String, String> resultCommandMap = new HashMap<>();

        List<CategoryDTO> categoryList = postDAO.getCategoryList();
        request.setAttribute("categoryList", categoryList);

        resultCommandMap.put("command", "forward");
        resultCommandMap.put("viewPath", "/WEB-INF/boards/free/write-form.jsp");

        return resultCommandMap;
    }
}
