package com.fuyou.play.bean.discuss;

import com.fuyou.play.util.DecryptUtils;
import com.google.gson.Gson;

public class DiscussEntity {

	private int id,label,views,likes,favorites;
	private String type,title,content,labelName,createTime,replyTime,images,author,authorName,authorIcon;	
	public DiscussEntity(){}

	public DiscussEntity(int id, int label, String type, String title, String content, String labelName,
						 String createTime, String replyTime, String images, String author, String authorName, String authorIcon
			, int views, int likes, int favorites) {
		super();
		this.id = id;
		this.label = label;
		this.type = type;
		this.title = title;
		this.content = content;
		this.labelName = labelName;
		this.createTime = createTime;
		this.replyTime = replyTime;
		this.images = images;
		this.author = author;
		this.authorName = authorName;
		this.authorIcon = authorIcon;
		this.views = views;
		this.likes = likes;
		this.favorites = favorites;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return DecryptUtils.decodeBase64(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return DecryptUtils.decodeBase64(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorIcon() {
		return authorIcon;
	}

	public void setAuthorIcon(String authorIcon) {
		this.authorIcon = authorIcon;
	}

	@Override
	public String toString() {
		String result = "";
		try {
			DiscussEntity entity = new DiscussEntity(id, label, type, title, content, labelName, createTime, 
					replyTime, images, author, authorName, authorIcon, views, likes, favorites);
			result = new Gson().toJson(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	
	
	
	
}
