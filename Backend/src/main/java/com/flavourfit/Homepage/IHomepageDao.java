package com.flavourfit.Homepage;

import java.sql.SQLException;
import java.util.List;

public interface IHomepageDao {
    List<HomepageEventDto> getEventList() throws SQLException;
}
