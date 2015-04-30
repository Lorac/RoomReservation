Assign room requests

Narrative:
To maximize the use of the rooms and allow as many people to have access to it
As a company
I want to have an automated mechanism for assigning rooms

Scenario: Room request are assigned by lot
Given multiple room requests
And multiple available rooms
When the room requests are processed
Then all the room request are assigned to a room

Scenario: Room request with higher priority are assigned first
Given a room request with low priority
And a room request with high priority
And multiple available rooms
When the room requests are processed
Then the room request with higher priority is assigned before

Scenario: Room request are assigned in order of arrival
Given a first room request
And a second room request
And multiple available rooms
When the room requests are processed
Then the first room request is assigned before the second request

Scenario: Maximize room usage
Given a room request with attendees
And multiple unreserved rooms of different capacities
When the room requests are processed
Then the smallest room fitting the request needs is found

Scenario: While maximizing room usage, if more than one room fits the needs, then any of these room is selected
Given a room request with attendees
And multiple unreserved rooms of same capacities
When the room requests are processed
Then any room fitting the request needs is found
