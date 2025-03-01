package com.manning.workout.reservation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoomReservation {
    public int roomId;
    public int id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public boolean isAvailable( final LocalDateTime start, final LocalDateTime end) {
        return (!(this.endTime.isBefore(start) || this.startTime.isAfter(end)));
    }

    public boolean bookRoom( final LocalDateTime start, final LocalDateTime end) {
        if (isAvailable(start, end)) {
            this.startTime = start;
            this.endTime = end;
            return true;
        }
        return false;
    }

}
