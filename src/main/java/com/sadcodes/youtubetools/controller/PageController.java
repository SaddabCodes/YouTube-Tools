package com.sadcodes.youtubetools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        model.addAttribute("primaryVideo", null);
        model.addAttribute("relatedVideos", null);
        model.addAttribute("error", null);
        model.addAttribute("activePage", "home");
        return "home";
    }

    @GetMapping("/video-details")
    public String videoDetails() {
        return "video-details";
    }

}
