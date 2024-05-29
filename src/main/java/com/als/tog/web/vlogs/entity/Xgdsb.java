package com.als.tog.web.vlogs.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dkw001
 * @since 2024年04月08日
 */
@Data
@TableName("blogInfo")
public class Xgdsb implements Serializable {

    private static final long serialVersionUID = 7193486139257172915L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("title")
    private String title;

    @TableField("summary")
    private String summary;

    @TableField("content")
    private String content;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("tagTypes")
    private String tagTypes;

    @TableField("author")
    private String author;

    @TableField("userId")
    private Long userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
