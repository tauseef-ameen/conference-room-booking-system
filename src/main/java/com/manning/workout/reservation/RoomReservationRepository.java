package com.manning.workout.reservation;

import java.util.List;

public interface RoomReservationRepository {
    List<RoomReservation> findAll();

    RoomReservation save(RoomReservation roomReservation);
}
