package com.cos.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.dto.DeleteReqDto;
import com.cos.blog.domain.board.dto.DeleteRespDto;
import com.cos.blog.domain.board.dto.ReadRespDto;
import com.cos.blog.domain.board.dto.SaveReqDto;
import com.cos.blog.domain.board.dto.SearchReqDto;
import com.cos.blog.domain.board.dto.UpdateReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.util.Script;
import com.google.gson.Gson;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BoardController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		BoardService boardService = new BoardService();
		HttpSession session = request.getSession();
		if (cmd.equals("saveForm")) {
			User principal = (User) session.getAttribute("principal");
			if (principal != null) {
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

			if (result == 1) { // 정상
				response.sendRedirect("index.jsp");
			} else {
				Script.back(response, "글쓰기실패");
			}
		} else if (cmd.equals("list")) {
			int page = Integer.parseInt(request.getParameter("page"));
			String keyword = request.getParameter("keyword");
			
//			System.out.println("keyword : " +keyword);
			int boardCount=0;
			
			if(keyword.equals("")) {
				List<Board> boards = boardService.목록보기(page);
				request.setAttribute("boards", boards);

				// 계산 (전체 데이터수랑 한페이지몇개 - 총 몇페이지 나와야되는 계산) 3page라면 page의 맥스값은 2
				boardCount = boardService.글개수();
			} else {
				SearchReqDto dto = new SearchReqDto();
				dto.setPage(page);
				dto.setKeyword(keyword);

				List<Board> boards = boardService.목록보기(dto);
				request.setAttribute("boards", boards);

				// 계산 (전체 데이터수랑 한페이지몇개 - 총 몇페이지 나와야되는 계산) 3page라면 page의 맥스값은 2
				boardCount = boardService.글개수(keyword);
			}
			
			int lastPage = (boardCount - 1) / 4; // 2/4 = 0, 3/4 = 0, 4/4 = 1. 9/4 = 2 ( 0page, 1page, 2page )
			double currentPosition = (double) page / lastPage * 100;
			
//			System.out.println("lastPage : "+lastPage);
//			System.out.println("currentPosition : "+currentPosition);

			request.setAttribute("lastPage", lastPage);
			request.setAttribute("currentPosition", currentPosition);
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("read")) {
			int id = Integer.parseInt(request.getParameter("id"));
			ReadRespDto dto = boardService.글상세보기(id); // board테이블 + user테이블 = 조인된 데이터 !!
			if (dto == null) {
				Script.back(response, "상세보기를 실패하셨습니다.");
			} else {
				request.setAttribute("dto", dto);
				RequestDispatcher dis = request.getRequestDispatcher("board/read.jsp");
				dis.forward(request, response);
			}

		} else if (cmd.equals("delete")) {
			// 1.요청 받은 json 데이터를 자바 오브젝트로 파싱
			BufferedReader br = request.getReader();
			String data = br.readLine();

			Gson gson = new Gson();
			DeleteReqDto dto = gson.fromJson(data, DeleteReqDto.class);

			System.out.println("data : " + data);
			System.out.println("dto : " + dto);

			// 2, DB에서 id값으로 글 삭제
			int result = boardService.글삭제하기(dto.getBoardId());

			// 3. 응답할 json 데이터를 생성
			DeleteRespDto respDto = new DeleteRespDto();
			if (result == 1) {
				respDto.setStatus("ok");
			} else {
				respDto.setStatus("fail");
			}
			String respData = gson.toJson(respDto);
			System.out.println("respData : " + respData);
			PrintWriter out = response.getWriter();
			out.print(respData);
			out.flush();
		} else if (cmd.equals("updateForm")) {
			int id = Integer.parseInt(request.getParameter("id"));
			ReadRespDto dto = boardService.글상세보기(id);
			request.setAttribute("dto", dto);
			RequestDispatcher dis = request.getRequestDispatcher("board/updateForm.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("update")) {
			int id = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			UpdateReqDto dto = new UpdateReqDto();
			dto.setId(id);
			dto.setTitle(title);
			dto.setContent(content);

			int result = boardService.글수정(dto);

			if (result == 1) {
				response.sendRedirect("/blog/board?cmd=read&id=" + id);
			} else {
				Script.back(response, "글 수정에 실패하였습니다.");
			}
		} else if (cmd.equals("search")) {
			int page = Integer.parseInt(request.getParameter("page"));
			String keyword = request.getParameter("keyword");
			
			if(keyword.equals("")) {
				Script.back(response, "검색어를 입력하세요");
			} else {
				response.sendRedirect("/blog/board?cmd=list&page=" + page +"&keyword=" +keyword);
			}
			
		}
	}

}
