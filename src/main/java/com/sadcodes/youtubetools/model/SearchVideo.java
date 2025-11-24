package com.sadcodes.youtubetools.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchVideo {

    private Video primaryVideo;
    private List<Video> relatedVideos;
}
