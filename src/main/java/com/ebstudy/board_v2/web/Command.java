package com.ebstudy.board_v2.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {
    String service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
