package com.wht.item.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class CmsMiddle implements Serializable {
    private Long id;

    @ApiModelProperty(value = "文章ID")
    private Long aId;

    @ApiModelProperty(value = "分类/标签ID")
    private Long mId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", aId=").append(aId);
        sb.append(", mId=").append(mId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}