package com.cos.blog.domain.reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cos.blog.config.DB;
import com.cos.blog.domain.reply.dto.ReplyRespDto;
import com.cos.blog.domain.reply.dto.SaveReqDto;


public class ReplyDao {
	
	public int deleteById(int id) {
		String sql = "DELETE FROM reply WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	public List<ReplyRespDto> findAll(int boardId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT r.id, r.userId, r.boardId, r.content, u.username ");
		sb.append("FROM reply r inner join user u ");
		sb.append("on r.userId = u.id ");
		sb.append("WHERE r.boardId = ? ");
		sb.append("ORDER BY r.id DESC");
		
		String sql = sb.toString();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		List<ReplyRespDto> replies = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs =  pstmt.executeQuery();

			// Persistence API
			while(rs.next()) { // 커서를 이동하는 함수
				ReplyRespDto reply = new ReplyRespDto();
				reply.setId(rs.getInt("id"));
				reply.setUserId(rs.getInt("userId"));
				reply.setBoardId(rs.getInt("boardId"));
				reply.setContent(rs.getString("content"));
				reply.setUsername(rs.getString("username"));
				
				replies.add(reply);
			}
			return replies;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public ReplyRespDto findById(int id){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT r.id, r.userId, r.boardId, r.content, u.username ");
		sb.append("FROM reply r inner join user u ");
		sb.append("on r.userId = u.id ");
		sb.append("where r.id = ?");
		
		String sql = sb.toString();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs =  pstmt.executeQuery();

			// Persistence API
			if(rs.next()) { // 커서를 이동하는 함수
				ReplyRespDto reply = new ReplyRespDto();
				reply.setId(rs.getInt("id"));
				reply.setUserId(rs.getInt("userId"));
				reply.setBoardId(rs.getInt("boardId"));
				reply.setContent(rs.getString("content"));
				reply.setUsername(rs.getString("username"));
				return reply;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public int save(SaveReqDto dto) { 
		String sql = "INSERT INTO reply(userId, boardId, content, createDate) VALUES(?,?,?, now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int generateKey;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, dto.getUserId());
			pstmt.setInt(2, dto.getBoardId());
			pstmt.setString(3, dto.getContent());
			int result = pstmt.executeUpdate();
			rs=pstmt.getGeneratedKeys();
			if(rs.next()) {
				generateKey = rs.getInt(1);
				System.out.println("생성된 키(ID) : "+generateKey);
				if(result==1) {
					return generateKey;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
}
