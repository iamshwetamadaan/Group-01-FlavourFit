package com.flavourfit.HealthCoach;

import com.flavourfit.Authentication.IAuthService;
import com.flavourfit.Feeds.FeedDto;
import com.flavourfit.HealthCoach.Appointments.AppointmentsDto;
import com.flavourfit.HealthCoach.Appointments.IAppointmentsService;
import com.flavourfit.ResponsesDTO.GetResponse;
import com.flavourfit.ResponsesDTO.PutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/health-coach")
public class HealthCoachController {
    private static Logger logger = LoggerFactory.getLogger(HealthCoachController.class);

    private IHealthCoachService healthCoachService;
    private IAuthService authService;
    private IAppointmentsService appointmentService;
    @Autowired
    public HealthCoachController(IHealthCoachService healthCoachService, IAuthService authService, IAppointmentsService appointmentService) {
        this.healthCoachService = healthCoachService;
        this.authService = authService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/get-all")
    public ResponseEntity<GetResponse> getAllHealthCoaches() {
        logger.info("Entered controller method getAllHealthCoaches()");
        try {
            ArrayList<HealthCoachDto> coaches = this.healthCoachService.getAllHealthCoaches();
            logger.info("Retrieved list of health coaches");
            return ResponseEntity.ok().body(new GetResponse(true, "Successfully retrieved health Coaches information", coaches));
        } catch (Exception e) {
            logger.error("Failed to retrieve health coaches information");
            return ResponseEntity.badRequest().body(new GetResponse(false, "Failed to retrieve health coaches information:" + e.getMessage()));
        }
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<PutResponse> bookAppointments(
            @RequestBody AppointmentsDto appointment
            , @RequestHeader("Authorization") String token
            ) {
        logger.info("Entered controller method bookAppointments()");
        try {
            int userId = authService.extractUserIdFromToken(token);
//            int userId=7;
            int count = appointmentService.bookAppointments(appointment,userId);
            if(count>0){
                logger.info("Booked an appointment with health coach");
                return ResponseEntity.ok().body(new PutResponse(true, "Successfully booked the appointment", null));
            }
            else{
                logger.error("Unable to book the appointment");
                return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to book the appointment :" + "Something went wrong"));
            }

        } catch (Exception e) {
            logger.error("Failed to retrieve health coaches information");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to book the appointment :" + e.getMessage()));
        }
    }
}
