package com.cos.blog.service;

import java.util.List;

import com.cos.blog.domain.reply.Reply;
import com.cos.blog.domain.reply.ReplyDao;
import com.cos.blog.domain.reply.dto.ReplyRespDto;
import com.cos.blog.domain.reply.dto.SaveReqDto;

public class ReplyService {
	
	private ReplyDao replyDao;
	
	public ReplyService() {
		replyDao = new ReplyDao();
	}
	
	public int 댓글쓰기(SaveReqDto dto) {
		return replyDao.save(dto);
	}
	
	public ReplyRespDto 댓글찾기(int id) {
		return replyDao.findById(id);
	}
	
	public List<ReplyRespDto> 댓글목록(int boardId){
		return replyDao.findAll(boardId);
	}
	
}
