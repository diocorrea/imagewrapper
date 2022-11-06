package com.newsnow.imagewrapper.domain;

import java.util.Objects;
import java.util.UUID;

public class Task {
    private UUID id;
    private String md5;
    private String fileName;
    private String url;
    private Integer width;
    private Integer height;
    private Long created;

    private Task(UUID id, String md5, String fileName, String url, Integer width, Integer height, Long created) {
        this.id = id;
        this.md5 = md5;
        this.fileName = fileName;
        this.url = url;
        this.width = width;
        this.height = height;
        this.created = created;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(md5, task.md5) && Objects.equals(fileName, task.fileName) && Objects.equals(url, task.url) && Objects.equals(width, task.width) && Objects.equals(height, task.height) && Objects.equals(created, task.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, md5, fileName, url, width, height, created);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", md5='" + md5 + '\'' +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", timestamp=" + created +
                '}';
    }


    public static class TaskBuilder {
        private UUID id;
        private String md5;
        private String fileName;
        private String url;
        private Integer width;
        private Integer height;
        private Long created;

        public TaskBuilder md5(String md5) {
            this.md5 = md5;
            return this;
        }

        public TaskBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public TaskBuilder url(String url) {
            this.url = url;
            return this;
        }

        public TaskBuilder width(Integer width) {
            this.width = width;
            return this;
        }

        public TaskBuilder height(Integer height) {
            this.height = height;
            return this;
        }

        public TaskBuilder created(Long created) {
            this.created = created;
            return this;
        }

        public TaskBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public Task build() {
            return new Task(id, md5, fileName, url, width, height, created);
        }
    }

}
