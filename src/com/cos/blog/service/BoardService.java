package com.cos.blog.service;

import java.util.List;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.BoardDao;
import com.cos.blog.domain.board.dto.ReadRespDto;
import com.cos.blog.domain.board.dto.SaveReqDto;

public class BoardService {
	private BoardDao boardDao;
	
	public BoardService() {
		boardDao = new BoardDao();
	}
	
	// 하나의 서비스안에 여러가지 DB관련 로직이 섞여있음
	public ReadRespDto 글상세보기(int id) {
		int result = boardDao.readCountUpdate(id);
		if(result == 1) {
			return boardDao.findById(id);
		} else {
			return null;
		}
	}
	
	public int 글개수() {
		return boardDao.count();
	}
	
	public int 글쓰기(SaveReqDto dto) {
		return boardDao.save(dto);
	}
	
	public List<Board> 목록보기(int page){
		return boardDao.findAll(page);
	}
	
	
}
