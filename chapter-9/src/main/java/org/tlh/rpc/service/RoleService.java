package org.tlh.rpc.service;

import org.tlh.rpc.core.RpcClient;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/11
 * <p>
 * Github: https://github.com/tlhhup
 */
@RpcClient
public interface RoleService {

    void delete(int id);
}
