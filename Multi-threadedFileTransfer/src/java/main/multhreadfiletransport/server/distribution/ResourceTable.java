package multhreadfiletransport.server.distribution;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.util.ParseUtil;
import redis.clients.jedis.Jedis;

/**
 * Created by dela on 2/7/18.
 */

// 使用redis存储资源表
public class ResourceTable {
    private Jedis jedis;

    public ResourceTable() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    // 客户端更新资源表信息
    public void updateClientResource(Message msgMessage) {
        String clientID = String.valueOf(msgMessage.getFrom());
        String[] fileNames = ParseUtil.parseStringToFileNameStringhArray(msgMessage.getMessage());

        // 先检测资源表中有没有这个客户端的资源信息, 如果没有, 就直接插入, 如果有, 就更新
        if (checkClientResourceExist(clientID)) {
            // 更新资源表
        } else {
            // 插入资源信息
        }
    }

    public boolean checkClientResourceExist(String clientId) {

        String result = jedis.get(clientId);
        if (result == null) {
            return false;
        }
        return true;
    }

}
