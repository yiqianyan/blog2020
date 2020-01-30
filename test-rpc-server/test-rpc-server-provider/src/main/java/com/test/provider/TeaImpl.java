package com.test.provider;

import com.test.api.ITea;

//定位项目中的哪些类能做RPC服务
//@Component
@RpcService(ITea.class)
public class TeaImpl implements ITea {
    public String produceTea(Integer type) {
        if (type == 1) {
            return "红茶";
        } else if (type == 2) {
            return "黑茶";
        } else {
            return "普洱茶";
        }
    }
}
