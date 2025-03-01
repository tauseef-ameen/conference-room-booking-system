package com.manning.workout.reservation;

import java.util.List;

public interface InMemoryRoomReservationRepository {

    List<RoomReservation> findAll();

    RoomReservation save(RoomReservation roomReservation);
}
