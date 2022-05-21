package com.yppah.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {

    //防止前端 精度损失 把id从long转为string
    //分布式id较长，传到前端时会有精度损失，必须转换为string类型进行传输
//    @JsonSerialize(using = ToStringSerializer.class)
//    private Long id;

    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;

}
