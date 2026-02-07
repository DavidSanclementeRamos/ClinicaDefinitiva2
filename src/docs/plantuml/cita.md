```
Usuario                AppointmentSchedulingService    Dentist    Patient    Shift    AppointmentRepository
  |                              |                        |          |         |               |
  |--scheduleAppointment()------>|                        |          |         |               |
  |                              |                        |          |         |               |
  |                              |--canScheduleBetween()-->|         |         |               |
  |                              |<-----------------------|          |         |               |
  |                              |                        |          |         |               |
  |                              |--canScheduleBetween()---------->  |         |               |
  |                              |<----------------------------------|         |               |
  |                              |                        |          |         |               |
  |                              |--findActiveByDate()------------------>     |               |
  |                              |<---shift-----------------------------------|               |
  |                              |                        |          |         |               |
  |                              |--canAccommodateAppointment()------->       |               |
  |                              |<--------------------------------------------|               |
  |                              |                        |          |         |               |
  |                              |--findConflictingForDentist()------------------>            |
  |                              |<--(empty list)-------------------------------------------- |
  |                              |                        |          |         |               |
  |                              |--findConflictingForPatient()---------------------->        |
  |                              |<--(empty list)--------------------------------------------  |
  |                              |                        |          |         |               |
  |                              |--new Appointment.Builder()         |         |               |
  |                              |<--appointment          |          |         |               |
  |                              |                        |          |         |               |
  |                              |--save(appointment)------------------------------------>    |
  |                              |<--saved appointment----------------------------------------|
  |                              |                        |          |         |               |
  |<--appointment----------------|                        |          |         |               |
```