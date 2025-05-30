package com.motionmate.dto.routine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDeleteSetRequest {
    private Long userId;
    private String title;
    private String exerciseName;
    private String exerciseCategory;
    private int setNumber;
}
