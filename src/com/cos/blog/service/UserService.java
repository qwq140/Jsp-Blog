package com.cos.blog.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cos.blog.config.DB;
import com.cos.blog.domain.user.User;
import com.cos.blog.domain.user.UserDao;
import com.cos.blog.domain.user.dto.JoinReqDto;
import com.cos.blog.domain.user.dto.LoginReqDto;
import com.cos.blog.domain.user.dto.UpdateReqDto;
import com.mysql.cj.xdevapi.PreparableStatement;

public class UserService {
	// 회원가입, 회원수정, 로그인, 아이디중복체크
	private UserDao userDao;
	public UserService() {
		userDao = new UserDao();
	}
	// 회원가입은 insert를 하여 성공 또는 실패 두가지 경우 리턴하므로 int 타입
	public int 회원가입 (JoinReqDto dto) {
		return userDao.save(dto);
	}
	
	// 로그인을 하면 select를 하여 모델 User클래스에 데이터를 다 저장, 리턴은 User 타입으로
	public User 로그인(LoginReqDto dto) {
		return userDao.findByUsernameAndPassword(dto);
	}
	
	// 회원수정은 insert를 하여 성공 또는 실패 두가지 경우 리턴하므로 int 타입
	public int 회원수정(UpdateReqDto dto) {
		return -1;
	}
	
	// 아이디중복체크는 아이디가 중복되었는 안되었는지 두 가지 경우의 리턴
	public int 유저네임중복체크(String username) {
		int result = userDao.findByUsername(username);
		return result;
	}
}
