package com.cos.blog.domain.board.dto;

import lombok.Data;

@Data
public class SearchReqDto {
	private int page;
	private String keyword;
}
