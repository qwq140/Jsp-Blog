package com.cos.blog.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.dto.SaveReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.util.Script;




@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		BoardService boardService = new BoardService();
		HttpSession session = request.getSession();
		if(cmd.equals("saveForm")) {
			User principal = (User)session.getAttribute("principal");
			if(principal != null) {
//				response.sendRedirect("board/saveForm.jsp");
				RequestDispatcher dis = request.getRequestDispatcher("board/saveForm.jsp");
				dis.forward(request, response);
			} else {
//				response.sendRedirect("user/loginFrom.jsp");
				RequestDispatcher dis = request.getRequestDispatcher("user/loginForm.jsp");
				dis.forward(request, response);
			}
		} else if (cmd.equals("save")) {
			int userId = Integer.parseInt(request.getParameter("userId"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			SaveReqDto dto = new SaveReqDto();
			dto.setUserId(userId);
			dto.setTitle(title);
			dto.setContent(content);
			int result = boardService.글쓰기(dto);
			
			if (result == 1 ) { // 정상
				response.sendRedirect("index.jsp");
			} else {
				Script.back(response, "글쓰기실패");
			}
		} else if (cmd.equals("list")) {
			int page = Integer.parseInt(request.getParameter("page"));
			List<Board> boards = boardService.목록보기(page);
			request.setAttribute("boards", boards);
			
			int boardCount = boards.size();
			boolean isEnd = false;
			if (boardCount != 4) {
				isEnd = true;
				
			}
			request.setAttribute("isEnd", isEnd);
//			System.out.println(boardCount);
//			System.out.println(page);
//			System.out.println(isEnd);
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("read")) {
			RequestDispatcher dis = request.getRequestDispatcher("board/read.jsp");
			dis.forward(request, response);
		}
	}

}
