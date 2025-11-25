package com.sadcodes.youtubetools.controller;

import com.sadcodes.youtubetools.model.SearchVideo;
import com.sadcodes.youtubetools.model.Video;
import com.sadcodes.youtubetools.service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/youtube")
public class YoutubeTagsController {

    @Autowired
    private YoutubeService youtubeService;

    @Value("${youtube.api.key}")
    private String apiKey;

    // Check, is API key work correctly
    private boolean isApiKeyConfigure() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @PostMapping("/search")
    public String searchTags(@RequestParam("videoTitle") String videoTitle, Model model) {
        if (!isApiKeyConfigure()) {
            model.addAttribute("error", "YouTube API key not configured.");
            model.addAttribute("primaryVideo", null);
            model.addAttribute("relatedVideos", Collections.emptyList());
            model.addAttribute("activePage", "home");
            return "home";
        }

        SearchVideo result = youtubeService.searchVideos(videoTitle);

        Video primary = result.getPrimaryVideo();
        List<Video> related = result.getRelatedVideos() == null ? Collections.emptyList() : result.getRelatedVideos();

        model.addAttribute("primaryVideo", primary);
        model.addAttribute("relatedVideos", related);
        model.addAttribute("activePage", "home");

        // Provide strings used by Thymeleaf copy buttons (handle nulls)
        String primaryVideoTagsAsString = (primary == null || primary.getTags() == null)
                ? "" : String.join(", ", primary.getTags());
        String allTagsAsString = related.stream()
                .flatMap(v -> v.getTags() == null ? java.util.stream.Stream.empty() : v.getTags().stream())
                .collect(Collectors.joining(", "));

        model.addAttribute("primaryVideoTagsAsString", primaryVideoTagsAsString);
        model.addAttribute("allTagsAsString", allTagsAsString);

        return "home";
    }

}
