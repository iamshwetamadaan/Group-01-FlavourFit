package com.flavourfit.Homepage;


public class HomepageEventDto {


    /**
     * Data Transfer Object(DTO) for the events table
     */
        private int event_ID;
        private String Event_name;
        private String Start_date;
        private String End_date;
        private String Capacity;
        private String Hostname;
        private String Event_description;

    public HomepageEventDto(int eventId, String eventName, String startDate, String endDate, String capacity, String hostName, String eventDescription) {
        this.event_ID = eventId;
        this.Event_name = eventName;
        this.Start_date = startDate;
        this.End_date = endDate;
        this.Capacity = capacity;
        this.Hostname = hostName;
        this.Event_description =eventDescription;
    }


    public int getevent_ID() {
            return event_ID;
        }

        public void setevent_ID(int event_ID) {
            this.event_ID = event_ID;
        }

        public String getEvent_name() {
            return Event_name;
        }

        public void setEvent_name(String Event_name) {
            this.Event_name = Event_name;
        }

        public String getStart_date() {
            return Start_date;
        }

        public void setStart_date(String Start_date) {
            this.Start_date = Start_date;
        }

        public String getEnd_date() {
            return End_date;
        }

        public void setEnd_date(String End_date) {
            this.End_date = End_date;
        }

        public String getCapacity() {
            return Capacity;
        }

        public void setCapacity(String Capacity) {
            this.Capacity = Capacity;
        }

        public String getHostname() {
            return Hostname;
        }

        public void setHostname(String Hostname) {
            this.Hostname = Hostname;
        }

        public String getEvent_description() {
            return Event_description;
        }

        public void setEvent_description(String Event_description) {
            this.Event_description = Event_description;
        }



        @Override
        public String toString() {
            return "EventDto{" +
                    "event_ID=" + event_ID +
                    ", Event_name='" + Event_name + '\'' +
                    ", Start_date='" + Start_date + '\'' +
                    ", End_date='" + End_date + '\'' +
                    ", Capacity='" + Capacity + '\'' +
                    ", Hostname=" + Hostname +
                    ", Event_description='" + Event_description + '\'' +
                    '}';
        }
    }


