package com.newsnow.imagewrapper.repository;

import com.newsnow.imagewrapper.domain.Task;
import com.newsnow.imagewrapper.repository.generated.tables.records.TaskRecord;

import java.time.ZoneOffset;

public class TaskRecordConverter {
    private TaskRecordConverter() {
        //nothing
    }

    public static com.newsnow.imagewrapper.domain.Task asTask(TaskRecord taskRecord) {
        var taskBuilder = new com.newsnow.imagewrapper.domain.Task.TaskBuilder();

        taskBuilder.id(taskRecord.getId())
                .md5(taskRecord.getMd5())
                .url(taskRecord.getUrl())
                .fileName(taskRecord.getFilename())
                .height(taskRecord.getHeight())
                .width(taskRecord.getWidth())
                .created(taskRecord.getCreated().toEpochSecond(ZoneOffset.UTC));

        return taskBuilder.build();
    }

    public static TaskRecord asRecord(Task task) {
        var ret = new TaskRecord();
        ret.setMd5(task.getMd5());
        ret.setUrl(task.getUrl());
        ret.setFilename(task.getFileName());
        ret.setHeight(task.getHeight());
        ret.setWidth(task.getWidth());
        return ret;
    }
}
