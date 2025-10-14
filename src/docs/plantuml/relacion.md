@startuml
title Relación entre Shift, WorkingHours, Appointment y TimeIntervalRules

class Appointment {
- LocalDateTime start
- LocalDateTime end
- Dentist dentist
- Patient patient
- AppointmentType type
- String reason
  }

class Dentist {
- User user
- WorkingHours workingHours
- Schedule schedule
+ canScheduleBetween(start, end)
  }

class Patient {
- User user
- Shift shift
+ canScheduleBetween(start, end)
  }

class WorkingHours {
- DayOfWeek dayOfWeek
- LocalTime start
- LocalTime end
+ isWithin(dateTime)
+ isWithinRange(start, end)
  }

class Shift {
- DayOfWeek dayOfWeek
- LocalTime start
- LocalTime end
- boolean active
- List<Appointment> appointments
+ isAvailableAt(dateTime)
+ isAvailableBetween(start, end)
+ reschedule(newStart, newEnd)
  }

class TimeIntervalRules {
+ contains(aStart, aEnd, bStart, bEnd)
+ overlaps(aStart, aEnd, bStart, bEnd)
+ isAdjacent(aStart, aEnd, bStart, bEnd)
+ isEqual(aStart, aEnd, bStart, bEnd)
+ isValid(start, end)
  }

Dentist "1" --> "1" WorkingHours : delega validación
Patient "1" --> "1" Shift : delega validación
Shift "1" --> "*" Appointment : contiene
Appointment "*" --> "1" Dentist
Appointment "*" --> "1" Patient

WorkingHours ..> TimeIntervalRules : usa
Shift ..> TimeIntervalRules : usa

@enduml