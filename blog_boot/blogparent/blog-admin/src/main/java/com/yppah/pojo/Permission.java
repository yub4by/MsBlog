package com.yppah.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    @TableId(type = IdType.AUTO) //取消分布式ID，使用一般的自增ID即可
    private Long id;

    private String name;

    private String path;

    private String description;

}
