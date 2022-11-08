package com.newsnow.imagewrapper.service;

import com.newsnow.imagewrapper.domain.Task;
import com.newsnow.imagewrapper.repository.TaskRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Service
public class ResizeImageService {

    @Autowired
    private final TaskRepository taskRepository;

    @Value("${baseUrl}")
    private String baseUrl;
    @Value("${basePath}")
    private String basePath;

    public ResizeImageService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task resizeTask(InputStream inputStream, Integer width, Integer height) throws IOException {

        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(width);
        Objects.requireNonNull(height);

        var tmpFileName = UUID.randomUUID();

        Path baseFolder = Path.of(basePath);
        var id = UUID.randomUUID();
        String newName = id + ".png";
        var url = baseUrl + newName;

        Path oldFile = storeFile(inputStream, tmpFileName.toString(), baseFolder);

        String md5 = getMd5(oldFile.toFile());

        var stored = taskRepository.selectTaskByMd5AndWidthHeight(md5,width,height);
        if(stored.isPresent()){
            return stored.get();
        }


        File newFile = new File(baseFolder.toAbsolutePath() + File.separator + newName);

        Task task = new Task.TaskBuilder()
                .id(id)
                .url(url)
                .width(width)
                .height(height)
                .fileName(id.toString())
                .md5(md5).build();

        transformImage(width, height, oldFile.toFile(), newFile);

        Files.delete(oldFile);

        return taskRepository.create(task);
    }


    private static Path storeFile(InputStream inputStream, String imageName, Path baseFolder) throws IOException {
        Path oldFile = Path.of(baseFolder.toAbsolutePath() + File.separator + "tmp" + File.separator + imageName);
        Files.createDirectories(oldFile);
        Files.copy(
                inputStream,
                oldFile,
                StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(inputStream);
        return oldFile;
    }



    public String getMd5(File oldFile) throws IOException {
        String md5 = "";
        try (InputStream secondStream = new FileInputStream(oldFile)) {
            md5 = md5Hex(secondStream);
        }
        return md5;
    }

    private static void transformImage(Integer width, Integer height, File oldFile, File newFile) throws IOException {
        Thumbnails.of(oldFile)
                .size(width, height)
                .keepAspectRatio(false)
                .outputFormat("png")
                .toFile(newFile);
    }

    public Optional<Task> searchTask(UUID taskid) {
        return taskRepository.selectTaskById(taskid);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
