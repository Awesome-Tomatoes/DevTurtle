package com.devturtle.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	private int userID;
	private String userName;
	private String loginID;
	private String loginPW;
	private String nickname;
	private String createdAt;
	private String updatedAt;
	private String solvedID;
	private String gitID;
	private String userBio;
	private int totalScore;
	private int solvedScore;
	private int gitScore;
	private int rank;
	
	public UserVO(String userName, String loginID, String loginPW, String nickname, String solvedID, String gitID,
			String userBio, int totalScore, int solvedScore, int gitScore) {
		super();
		this.userName = userName;
		this.loginID = loginID;
		this.loginPW = loginPW;
		this.nickname = nickname;
		this.solvedID = solvedID;
		this.gitID = gitID;
		this.userBio = userBio;
		this.totalScore = totalScore;
		this.solvedScore = solvedScore;
		this.gitScore = gitScore;
	}
}
