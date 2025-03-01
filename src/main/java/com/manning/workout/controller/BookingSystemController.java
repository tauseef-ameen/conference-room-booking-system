package com.manning.workout.controller;

import com.manning.workout.inventory.ConferenceRoom;
import com.manning.workout.inventory.InventoryConferenceRoomClient;
import com.manning.workout.reservation.RoomReservation;
import com.manning.workout.reservation.RoomReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BookingSystemController {

    private final RoomReservationRepository reservationsRepository;
    private final InventoryConferenceRoomClient inventoryClient;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${room.state.service.url}")
    private String roomStateServiceUrl;

    public BookingSystemController(RoomReservationRepository reservationsRepository, InventoryConferenceRoomClient inventoryClient) {
        this.reservationsRepository = reservationsRepository;
        this.inventoryClient = inventoryClient;
    }

    @GetMapping("/available/{startDate}/{endDate}")
    public Collection<ConferenceRoom> availability(@PathVariable("startDate") LocalDateTime startDate,
                                                   @PathVariable("endDate") LocalDateTime endDate) {

        log.info("Getting available conference rooms for startDate {} and endDate {}", startDate, endDate);
        // obtain all cars from inventory
        List<ConferenceRoom> availableRooms = inventoryClient.allRooms();
        // create a map from id to car
        Map<Integer, ConferenceRoom> carsById = new LinkedHashMap<>();
        for (ConferenceRoom room : availableRooms) {
            carsById.put(room.getRoomId(), room);
        }

        // get all current reservations
        List<RoomReservation> reservations = reservationsRepository.findAll();
        // for each reservation, remove the car from the map
        for (RoomReservation reservation : reservations) {
            if (reservation.isAvailable(startDate, endDate)) {
                carsById.remove(reservation.roomId);
            }
        }
        return carsById.values();

    }

    @PostMapping("/reserve")
    public RoomReservation reserve(RoomReservation roomReservation) {
        RoomReservation reservation = reservationsRepository.save(roomReservation);
        String state = getRoomState(reservation.roomId);
        if (!"Available".equals(state)) {
            log.info("Room is currently in state {} and cannot be booked", state);
        }
        return reservation;
    }

    private String getRoomState(int roomId) {
        String url = roomStateServiceUrl + "/state/" + roomId;
        log.info("Getting room state url {}", url);
        return restTemplate.getForObject(url, String.class);

    }


}
