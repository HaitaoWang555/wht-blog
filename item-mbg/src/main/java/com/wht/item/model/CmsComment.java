package com.wht.item.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class CmsComment implements Serializable {
    private Long id;

    private String username;

    private Long articleId;

    private String content;

    private String link;

    private Date createTime;

    private String quoteContent;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    private String userAgent;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", articleId=").append(articleId);
        sb.append(", content=").append(content);
        sb.append(", link=").append(link);
        sb.append(", createTime=").append(createTime);
        sb.append(", quoteContent=").append(quoteContent);
        sb.append(", ip=").append(ip);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}