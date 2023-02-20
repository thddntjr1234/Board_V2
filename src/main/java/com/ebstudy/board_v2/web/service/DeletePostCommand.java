package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.web.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class DeletePostCommand implements Command {
    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {
        return null;

    }
}
