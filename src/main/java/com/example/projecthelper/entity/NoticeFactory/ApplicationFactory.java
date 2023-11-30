package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;
import java.time.LocalDateTime;

public class ApplicationFactory extends AbstractNoticeFactory{
    @Override
    public Notice createNotice(Notice notice) {
        Notice nt = new ApplicationNotice();
        nt.setTitle(notice.getTitle());
        nt.setContent(notice.getContent());
        nt.setCreatorId(null);
        nt.setFromId(notice.getFromId());
        nt.setGroupId(notice.getGroupId());
        nt.setProjectId(notice.getProjectId());
        nt.setStatus(Notice.Status.UNDECIDED.ordinal());
        nt.setCreateTime(LocalDateTime.now());
        return nt;
    }
}
