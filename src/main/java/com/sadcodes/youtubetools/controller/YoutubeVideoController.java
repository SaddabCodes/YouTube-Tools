package com.sadcodes.youtubetools.controller;

import com.sadcodes.youtubetools.service.ThumbnailsService;
import com.sadcodes.youtubetools.service.YoutubeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class YoutubeVideoController {

    private final YoutubeService youtubeService;
    private final ThumbnailsService thumbnailsService;

    public YoutubeVideoController(YoutubeService youtubeService, ThumbnailsService thumbnailsService) {
        this.youtubeService = youtubeService;
        this.thumbnailsService = thumbnailsService;
    }

    @PostMapping("/youtube/video-details")
    public String showVideoForm(@RequestParam("videoUrlOrId") String videoUrlOrId, Model model) {

        String videoId = thumbnailsService.extractVideoId(videoUrlOrId);

        if (videoId == null) {
            model.addAttribute("error", "Invalid Video Id");
            model.addAttribute("videoUrlOrId", videoUrlOrId);
            model.addAttribute("activePage", "video");
            return "video-details";
        }

        model.addAttribute("videoUrlOrId", videoUrlOrId);
        model.addAttribute("videoId", videoId);
        model.addAttribute("videoDetails", youtubeService.getVideoDetails(videoId));
        model.addAttribute("activePage", "video");

        return "video-details";
    }
}
