package com.motionmate.dto.routine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineTitleUpdateRequest {
    private Long userId;
    private String oldTitle;
    private String newTitle;
}
