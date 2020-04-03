package com.blkchainsolutions.common;

import lombok.Data;

/**
 * Created by kimi on 2019/09/21.
 */
@Data
public class Result {

    private Integer code;
    private String msg;
    private Object data;
    private String status;

}
