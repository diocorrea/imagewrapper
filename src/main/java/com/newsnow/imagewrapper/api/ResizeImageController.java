package com.newsnow.imagewrapper.api;

import com.newsnow.imagewrapper.domain.Task;
import com.newsnow.imagewrapper.service.ResizeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class ResizeImageController {

    private final ResizeImageService resizeImageService;

    public ResizeImageController(@Autowired ResizeImageService resizeImageService) {
        this.resizeImageService = resizeImageService;
    }

    @PostMapping(path = "/task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> resizeImage(@RequestParam MultipartFile image,
                                            @RequestParam Integer width,
                                            @RequestParam Integer height) throws IOException {

        return ResponseEntity.ok()
                .body(resizeImageService.resizeTask(
                        image.getInputStream(),
                        image.getOriginalFilename(),
                        width,
                        height));
    }

    @GetMapping(path = "/task/{taskid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> resizeImage(@PathVariable UUID taskid) {
        return resizeImageService.searchTask(taskid)
                .map(task -> ResponseEntity.ok().body(task))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}