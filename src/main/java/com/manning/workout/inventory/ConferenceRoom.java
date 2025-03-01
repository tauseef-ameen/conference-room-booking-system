package com.manning.workout.inventory;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
@EqualsAndHashCode
@Setter
@RequiredArgsConstructor
public class ConferenceRoom {
    private int roomId;
    private String buildingName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ConferenceRoom(int roomId, String buildingName) {
        this.roomId = roomId;
        this.buildingName = buildingName;
    }
}
