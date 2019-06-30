package com.heima.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^                     
 *             no bugs under the protection of Buddha
 * 			Kevin
 *			2019年6月18日 下午2:51:52
 *          
 */
public class Client {
    ZooKeeper zk = null;
    
    private static String rootPath = "/test/";
    
    private static String ephemeralType = "ephemeral";
    private static String persistType = "persist";
    
    private static String ephemeral = rootPath+ephemeralType;
    private static String persist = rootPath+persistType;
    
    //当前使用的临时节点类型还是持久节点
    static String currentPath = ephemeral;
//    private static String currentPath = persist;
    
    
    private static String childPath = currentPath+ "/childNode";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        Client client = new Client();
        //获取连接
        client.getConnection();
        //注册信息
        client.regist(childPath);
        
    }

   

    private void regist(String nodeInfo) throws KeeperException, InterruptedException {
    	CreateMode mode = CreateMode.EPHEMERAL_SEQUENTIAL; 
    	if (currentPath.contains(persist)) {
			mode = CreateMode.PERSISTENT_SEQUENTIAL;
		}
        String path = zk.create(childPath, nodeInfo.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
        System.out.println(path+"上线了");
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void getConnection() throws IOException {
        zk = new ZooKeeper("192.168.25.129:2181", 3000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

                try {
                    List<String> children = zk.getChildren(currentPath, true);
                    ArrayList<String> node = new ArrayList<String>();
                    for (String c:children){
                        byte[] data = zk.getData(currentPath + "/"+c, true, null);
                        node.add(new String(data));
                    }
                    System.out.println("EventType="+watchedEvent.getType());
                    System.out.println("EventState="+watchedEvent.getState());
                    System.out.println("nodeList="+node);
                    System.out.println("=========================================");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            
            }
        });
    }
}



