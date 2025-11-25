package com.sadcodes.youtubetools.controller;

import com.sadcodes.youtubetools.service.ThumbnailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThumbnailController {

    @Autowired
    private ThumbnailsService thumbnailsService;

    @GetMapping("/thumbnail")
    public String getThumbnails(Model model) {
        model.addAttribute("activePage", "thumbnails");
        return "thumbnails";
    }

    @PostMapping("/get-thumbnail")
    public String showThumbnails(@RequestParam("videoUrlOrId") String videoUrlOrId, Model model) {

        String videoId = thumbnailsService.extractVideoId(videoUrlOrId);
        if (videoId == null) {
            model.addAttribute("error", "Invalid Youtube URL");
            model.addAttribute("activePage", "thumbnails");
            return "thumbnails";
        }

        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";

        model.addAttribute("videoId", videoId);
        model.addAttribute("thumbnailUrl", thumbnailUrl);
        model.addAttribute("activePage", "thumbnails");

        return "thumbnails";
    }

}
