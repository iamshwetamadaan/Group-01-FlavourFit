package com.flavourfit.Homepage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HomePageServiceImplTest {



        @InjectMocks
        private HomepageServiceImpl homepageService;

        @Mock
        private HomepageDaoImpl homepageDao;

        @BeforeEach
        public void initMocks() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testFetchEventList() throws SQLException {

            List<HomepageEventDto> mockEventList = new ArrayList<>();
            mockEventList.add(new HomepageEventDto(1, "Inhale and exhale", "2023-09-01", "2023-09-01", "100", "Sasha Berkley", "Yoga and Pilates event"));
            mockEventList.add(new HomepageEventDto(2, "Fitness freak", "2023-09-07", "2023-09-07", "100", "John Mendow", "HIIT Workout session"));

            when(homepageDao.getEventList()).thenReturn(mockEventList);
            List<HomepageEventDto> eventList = homepageService.fetcheventlist();

            assertEquals(1, eventList.size()); // Check if two events are returned as expected
            assertEquals("Inhale and exhale", eventList.get(0).getEvent_name());
           // assertEquals("Fitness freak", eventList.get(1).getEvent_name());

        }
    }
