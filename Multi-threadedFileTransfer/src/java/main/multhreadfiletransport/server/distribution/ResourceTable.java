package multhreadfiletransport.server.distribution;

import multhreadfiletransport.model.Message;
import multhreadfiletransport.util.ParseUtil;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dela on 2/7/18.
 */

// 使用redis存储资源表
// 在redis中维护两种表, 一种是<clientID, Set<fileName>>
// 另一种是<fileName, Set<clientID>>
public class ResourceTable {
    private static Jedis jedis;

    static {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public ResourceTable() { }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    // 客户端更新资源表信息
    public static void updateClientResource(Message msgMessage) {
        String clientID = String.valueOf(msgMessage.getFrom());
        String[] fileNames = ParseUtil.parseStringToFileNameStringhArray(msgMessage.getMessage());

        Set<String> newSet = new HashSet<>();
        Collections.addAll(newSet, fileNames);
        Set<String> oldSet = jedis.smembers(clientID);
        Set<String> removeSet = new HashSet<>();
        Set<String> addSet = new HashSet<>();

        // 如果该客户端的资源信息没有更改, 那么就不必要进行更改.
        if (oldSet.equals(newSet)) {
            return;
        }

        removeSet.addAll(oldSet);
        removeSet.removeAll(newSet);

        addSet.addAll(newSet);
        addSet.removeAll(oldSet);

        // 如果这个客户端在资源表里没有任何信息
        if (jedis.exists(clientID)) {
            jedis.sadd(clientID, fileNames);

            for (String fileName : fileNames) {
                if (!jedis.sismember(fileName, clientID)) {
                    jedis.sadd(fileName, clientID);
                }
            }
        } else { // 如果已经有这个客户端的信息
            // 添加没有的
            for (String fileName : addSet) {
                jedis.sadd(fileName, clientID);
            }
            // 删除该删的
            for (String fileName : removeSet) {
                jedis.srem(fileName, clientID);
            }
            // 重置clientID的键值对
            jedis.del(clientID);
            jedis.sadd(clientID, fileNames);
        }
    }

    public static Set<String> getNeedClient(String[] fileNames) {
        Set<String> clients = jedis.sunion(fileNames);
        return clients;
    }

    public static Set<String> getClientsByFileName(String fileName) {
        Set<String> result = jedis.smembers(fileName);
        return result;
    }
}
