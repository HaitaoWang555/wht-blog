package com.wht.item.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class CmsMiddle implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "文章ID")
    private Integer aId;

    @ApiModelProperty(value = "分类/标签ID")
    private Integer mId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
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