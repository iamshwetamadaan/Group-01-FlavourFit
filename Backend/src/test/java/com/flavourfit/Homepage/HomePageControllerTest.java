package com.flavourfit.Homepage;




import com.flavourfit.ResponsesDTO.GetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class HomePageControllerTest {



        @InjectMocks
        private HomepageController homepageController;

        @Mock
        private HomepageServiceImpl homepageService;

        @BeforeEach
        public void initMocks() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testFetchEventList_Success() throws SQLException {

            List<HomepageEventDto> mockEventList = new ArrayList<>();
            mockEventList.add(new HomepageEventDto(1, "Inhale and exhale", "2023-09-01", "2023-09-01", "100", "Sasha Berkley", "Yoga and Pilates event"));
            mockEventList.add(new HomepageEventDto(2, "Fitness freak", "2023-09-07", "2023-09-07", "100", "John Mendow", "HIIT Workout session"));

            when(homepageService.fetcheventlist()).thenReturn(mockEventList);


            ResponseEntity<GetResponse> responseEntity = homepageController.fetcheventlist();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            GetResponse response = responseEntity.getBody();
            assertNotNull(response);
            assertTrue(response.isSuccess());

            assertEquals(mockEventList, response.getData());
        }


    }


