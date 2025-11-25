package com.sadcodes.youtubetools.controller;

import com.sadcodes.youtubetools.service.ThumbnailsService;
import com.sadcodes.youtubetools.service.YoutubeService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeVideoController {

    private final YoutubeService youtubeService;
    private final ThumbnailsService thumbnailsService;

    public YoutubeVideoController(YoutubeService youtubeService, ThumbnailsService thumbnailsService) {
        this.youtubeService = youtubeService;
        this.thumbnailsService = thumbnailsService;
    }

    @PostMapping("/youtube/video-deatils")
    public String showVideoForm(@RequestParam String videoOrId, Model model){
        String videoId = thumbnailsService.extractVideoId(videoOrId);

        if (videoId==null){
            model.addAttribute("error","Invalid Video Id");
            return "video-details";
        }

    }
}
