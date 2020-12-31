package com.cos.blog.domain.reply;

import java.sql.Timestamp;

public class Board {
	private int id;
	private int userId;
	private String title;
	private String content;
	private int readCount; // 조회수 디폴트 0
	private Timestamp createDate;
}
