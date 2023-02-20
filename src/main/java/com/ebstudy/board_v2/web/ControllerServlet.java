package com.ebstudy.board_v2.web;

import com.ebstudy.board_v2.web.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static java.lang.System.out;

@WebServlet(name = "controllerServlet", urlPatterns = "/boards/free/*")
public class ControllerServlet extends HttpServlet {

    HashMap<String, Command> commandMap = new HashMap<>();

    public ControllerServlet() {
        initilizeCommands();
    }

    // 이 부분을 static으로 사용하면 다형성 등으로 다른 커맨드를 적용해야 할 때 static으로 인해서 사용할 수 없게 된다.
    private void initilizeCommands(){
        commandMap.put("/boards/free/list", new LoadPostListCommand());
        commandMap.put("/boards/free/write", new LoadWriteFormCommand());
        commandMap.put("/boards/free/write-save", new SavePostCommand());
        commandMap.put("/boards/free/view", new LoadPostCommand());
        commandMap.put("/boards/free/comment", new SaveCommentCommand());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // request uri
        String uri = request.getRequestURI();
        out.println("request uri: " + uri);

        // uri에 해당하는 서비스 커맨드를 할당, null인 경우 uri 요청이 잘못된 것으로 간주하고 HTTP 404 status를 반환
        Command command = commandMap.get(uri);
        if (command == null) {
            out.println("uri요청에 해당하는 커맨드를 찾을 수 없음");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 보다 Exception 이 낫다.
        }

        HashMap<String, String> resultCommandMap;
        try {
            resultCommandMap = command.service(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String viewPath;
        switch (resultCommandMap.get("command")) {
            case "forward":
                viewPath = resultCommandMap.get("viewPath");
                RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
                dispatcher.forward(request, response);
                break;
            case "redirect":
                viewPath = resultCommandMap.get("viewPath");
                response.sendRedirect(viewPath);
                break;
        }
    }
}
