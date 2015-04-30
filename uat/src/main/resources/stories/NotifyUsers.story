Notify users

Narrative:
To take action and go to meetings
As a user
I want to be notified when rooms are assigned or changed

Scenario: Notify an accepted request
Given a room request
And 1 unreserved rooms
When room requests are processed
Then an email is sent to the organizer and reservationClerk
And the email indicates the room number that was assigned

Scenario: Notify a refused request because of insufficient rooms
Given a room request
And 0 unreserved rooms
When room requests are processed
Then an email is sent to the organizer and reservationClerk
And the email indicates the insufficient rooms refusal

Scenario: Notify a canceled room request before it was processed
Given a room request
When the room request is modified
Then an email is sent to all attendees, organizer and reservation clerk
And the email indicates that the room request was canceled

Scenario: Notify a canceled room request after is was processed
Given a room request with an assigned room
When the room request is modified
Then an email is sent to all attendees, organizer and reservation clerk
And the email indicates that the room request with room assigned was canceled

