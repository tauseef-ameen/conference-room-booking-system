package com.manning.workout.conference_room_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.manning.workout")
public class ConferenceRoomBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingSystemApplication.class, args);
	}

}
