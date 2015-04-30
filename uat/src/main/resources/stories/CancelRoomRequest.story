Cancel room requests

Narrative:
In order to release a room that I won't use
As an organizer
I want to cancel my reservations

Scenario: Cancel a reservation
Given a room request without a room assigned
When I cancel the room request
Then the room request is cancelled
And the room request is archived

Scenario: Cancel an assigned room request
Given a room request with a room assigned
When I cancel the room request
Then the reservation is cancelled
And the reservation is archived
