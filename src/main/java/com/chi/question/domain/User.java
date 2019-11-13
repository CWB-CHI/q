package com.chi.question.domain;

public class User {
	private int id;
	private String name;
	private String password;
	private String salt;
	private String headUrl;

	private int commentCount;
	private int followerCount;
	private int followeeCount;
	private boolean followed;

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public int getFolloweeCount() {
		return followeeCount;
	}

	public void setFolloweeCount(int followeeCount) {
		this.followeeCount = followeeCount;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public User() {

	}

	public User(String name) {
		this.name = name;
		this.password = "";
		this.salt = "";
		this.headUrl = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", headUrl='" + headUrl + '\'' +
				'}';
	}
}
