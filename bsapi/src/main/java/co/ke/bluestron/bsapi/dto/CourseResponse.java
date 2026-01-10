package co.ke.bluestron.bsapi.dto;

import java.util.List;

import co.ke.bluestron.bsapi.entity.CourseCategory;

public record CourseResponse(
    Long id,
    String slug,
    String title,
    String shortDescription,
    String fullDescription,
    List<String> learningOutcomes,
    String thumbnailUrl,
    String status,
    CourseCategory category
) {}

