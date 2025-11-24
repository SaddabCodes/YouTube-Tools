package com.sadcodes.youtubetools.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
public class YoutubeTagsController {

    @Value("${youtube.api.key}")
    private String apiKey;

    // Check, is API key work correctly
    private boolean isApiKeyConfigure() {
        return apiKey != null && !apiKey.isEmpty();
    }


    @PostMapping("/search")
    public String videoTags(@RequestParam("videoTitle") String videoTitle, Model model) {

        if (!isApiKeyConfigure()) {
            model.addAttribute("error", "API key is not configured");
            return "home";

        }

        if (videoTitle == null || videoTitle.isEmpty()) {
            model.addAttribute("error", "Video Title is Required");
            return "home";
        }
        try {

        }catch (Exception e){

        }
        return null;


    }

}
