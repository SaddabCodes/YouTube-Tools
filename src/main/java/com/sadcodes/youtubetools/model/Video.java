package com.sadcodes.youtubetools.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    private String id;
    private String channelTitle;
    private String title;
    private List<String> tags;
}
