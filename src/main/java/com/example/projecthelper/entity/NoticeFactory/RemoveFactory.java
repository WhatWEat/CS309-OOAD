package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;
import java.time.LocalDateTime;

public class RemoveFactory extends AbstractNoticeFactory{
    @Override
    public Notice createNotice(Notice notice) {
        Notice nt = new RemoveNotice();
        nt.setTitle(notice.getTitle());
        nt.setContent(notice.getContent());
        nt.setCreatorId(notice.getCreatorId());
        nt.setGroupId(notice.getGroupId());
        nt.setProjectId(notice.getProjectId());
//        nt.setStatus(Notice.Status.UNDECIDED.getValue());
        nt.setCreateTime(LocalDateTime.now());
        return nt;
    }
}