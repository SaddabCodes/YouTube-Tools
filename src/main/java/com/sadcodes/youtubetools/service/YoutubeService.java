package com.sadcodes.youtubetools.service;

import com.sadcodes.youtubetools.model.SearchVideo;
import com.sadcodes.youtubetools.model.Video;
import com.sadcodes.youtubetools.model.VideoDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class YoutubeService {

    private final WebClient.Builder webClientBuilder;

    public YoutubeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    public SearchVideo searchVideos(String videoTitle) {
        List<String> videoId = searchForVideoIds(videoTitle);
        if (videoId.isEmpty()) {
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideos(Collections.emptyList())
                    .build();
        }
        String primaryVideoId = videoId.get(0);
        List<String> relatedVideoIds = videoId.subList(1, Math.min(videoId.size(), maxRelatedVideos + 1));

        Video primaryVideo = getVideoById(primaryVideoId);

        List<Video> relatedVideos = new ArrayList<>();
        for (String id : relatedVideoIds) {
            Video video = getVideoById(id);
        }
        return SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideos(relatedVideos)
                .build();
    }

    private Video getVideoById(String videoId) {

        VideoApiResponse response = webClientBuilder
                .baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items == null) {
            return null;
        }
        Snippet snippet = response.items.get(0).snippet;
        return Video.builder()
                .id(videoId)
                .channelTitle(snippet.channelTitle)
                .title(snippet.title)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .build();
    }

    private List<String> searchForVideoIds(String videoTitle) {
        SearchResponse response = webClientBuilder.baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snipper")
                        .queryParam("q", videoTitle)
                        .queryParam("type", "video")
                        .queryParam("maxResult", maxRelatedVideos)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .block();

        if (response == null || response.items == null) {
            return Collections.emptyList();
        }
        List<String> videoIds = new ArrayList<>();
        for (SearchItem item : response.items) {
            videoIds.add(item.id.videoId);
        }
        return videoIds;
    }

    public VideoDetails getVideoDetails(String videoId) {
        VideoApiResponse response = webClientBuilder
                .baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items == null) {
            return null;
        }
        Snippet snippet = response.items.get(0).snippet;
        String thumbnailUrl = snippet.thumbnails.getBestThumbnaiIlJrI();
        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.title)
                .description(snippet.description)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .thumbnailUrl(thumbnailUrl)
                .channelTitle(snippet.channelTitle)
                .publishedAt(snippet.publishedAt)
                .build();
    }

    @Data
    static class SearchResponse {
        List<SearchItem> items;
    }

    @Data
    static class SearchItem {
        Id id;
    }

    @Data
    static class Id {
        String videoId;
    }

    @Data
    static class VideoApiResponse {
        List<VideoItem> items;
    }

    @Data
    static class VideoItem {
        Snippet snippet;
    }

    @Data
    static class Snippet {
        String title;
        String description;
        String channelTitle;
        String publishedAt;
        List<String> tags;
        Thumbnails thumbnails;
    }

    @Data
    static class Thumbnails {
        Thumbnail maxres;
        Thumbnail high;
        Thumbnail medium;
        Thumbnail _default;

        String getBestThumbnaiIlJrI() {
            if (maxres != null)
                return maxres.url;
            if (high != null)
                return high.url;
            if (medium != null)
                return medium.url;

            return _default != null ? _default.url : "";

        }
    }

    @Data
    static class Thumbnail {
        String url;
    }
}

