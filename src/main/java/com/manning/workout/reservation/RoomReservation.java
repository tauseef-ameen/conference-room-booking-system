package com.manning.workout.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoomReservation {
    public int roomId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public int bookingId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;


    public boolean isAvailable( final LocalDateTime start, final LocalDateTime end) {
        return (!(this.endTime.isBefore(start) || this.startTime.isAfter(end)));
    }

}
