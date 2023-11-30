package com.example.projecthelper.service.Flyweight;

import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupManagerFactory {
    private volatile static GroupManagerFactory gmf;
    private static Map<Long, GroupManager> idGmMap;
    private GroupManagerFactory(){

    }
    public static GroupManagerFactory getInstance(){
        if(gmf==null){
            synchronized (GroupManagerFactory.class){
                if(gmf == null){
                    gmf = new GroupManagerFactory();
                    idGmMap = new HashMap<>();
                    return gmf;
                }
            }
        }
        return gmf;
    }

    public GroupManager getGroupManager(Long gpId, GroupMapper groupMapper, NoticeMapper noticeMapper){
        if(!idGmMap.containsKey(gpId)){
            synchronized (GroupManagerFactory.class){
                if(!idGmMap.containsKey(gpId)){
                    idGmMap.put(gpId, new GroupManager(groupMapper, noticeMapper));
                }
            }
        }
        return idGmMap.get(gpId);
    }
}
