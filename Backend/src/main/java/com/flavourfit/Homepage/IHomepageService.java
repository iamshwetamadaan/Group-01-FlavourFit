package com.flavourfit.Homepage;

import com.flavourfit.Trackers.Calories.CalorieGraphDto;

import java.util.List;

public interface IHomepageService {

    List<HomepageEventDto> getEventList();
}
