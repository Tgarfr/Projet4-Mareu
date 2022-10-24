package com.example.mareu;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.Participant;
import com.example.mareu.model.Room;
import com.example.mareu.repository.MeetingRepository;
import com.example.mareu.ui.meeting_add.MeetingAddActivity;
import com.example.mareu.ui.meeting_list.MeetingListActivity;
import com.example.mareu.utils.RecyclerViewItemAssertion;

import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MeetingAddTest {

    private MeetingRepository meetingRepository;

    @Rule
    public ActivityScenarioRule<MeetingListActivity> activityScenarioRule = new ActivityScenarioRule<MeetingListActivity> (MeetingListActivity.class);

    @Before
    public void setUp() {
        meetingRepository = MeetingRepository.getInstance();
    }

    @Test
    public void meetingAddTest_check() {
        // Given
        Meeting expectedMeeting = getFakeMeeting();

        // When
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());

        onView(ViewMatchers.withId(R.id.add_meeting_name))
                .perform(replaceText(expectedMeeting.getName()));

        onView(ViewMatchers.withId(R.id.add_meeting_date_button)).perform(click());
        onView(ViewMatchers.withId(R.id.dialog_date_picker))
                .perform(PickerActions.setDate(expectedMeeting.getBeginDate().get(Calendar.YEAR), expectedMeeting.getBeginDate().get(Calendar.MONTH), expectedMeeting.getBeginDate().get(Calendar.DAY_OF_MONTH)));
        onView(ViewMatchers.withId(R.id.dialog_date_button)).perform(click());

        onView(ViewMatchers.withId(R.id.add_meeting_hour_button)).perform(click());
        onView(ViewMatchers.withId(R.id.dialog_hour))
                .perform(PickerActions.setTime(expectedMeeting.getBeginDate().get(Calendar.HOUR_OF_DAY), expectedMeeting.getBeginDate().get(Calendar.MINUTE)));
        onView(ViewMatchers.withId(R.id.dialog_hour_button)).perform(click());

        onView(ViewMatchers.withId(R.id.add_meeting_endTime_button)).perform(click());
        onView(ViewMatchers.withId(R.id.dialog_hour))
                .perform(PickerActions.setTime(expectedMeeting.getEndDate().get(Calendar.HOUR_OF_DAY), expectedMeeting.getEndDate().get(Calendar.MINUTE)));
        onView(ViewMatchers.withId(R.id.dialog_hour_button)).perform(click());

        onView(ViewMatchers.withId(R.id.add_meeting_room_spinner)).perform(click());
        onView(ViewMatchers.withText(expectedMeeting.getRoom().getName())).perform(click());

        for (int i = 0; i < expectedMeeting.getParticipantList().size(); i++) {
            onView(ViewMatchers.withId(R.id.add_meeting_participant_add_EditText)).perform(replaceText(expectedMeeting.getParticipantList().get(i).getEmail()));
            onView(ViewMatchers.withId(R.id.add_meeting_participant_add_button)).perform(click());
        }

        onView(ViewMatchers.withId(R.id.add_meeting_valid_button)).perform(click());

        // Then
        Boolean foundMeeting = false;
        for (int i = 0; i < meetingRepository.countMeeting(); i++) {
            if (meetingRepository.getMeetingList().get(0).getName().equals(expectedMeeting.getName())) {
                assertEquals(expectedMeeting.getBeginDate().get(Calendar.YEAR), meetingRepository.getMeetingList().get(i).getBeginDate().get(Calendar.YEAR));
                assertEquals(expectedMeeting.getBeginDate().get(Calendar.MONTH), meetingRepository.getMeetingList().get(i).getBeginDate().get(Calendar.MONTH));
                assertEquals(expectedMeeting.getBeginDate().get(Calendar.DAY_OF_MONTH), meetingRepository.getMeetingList().get(i).getBeginDate().get(Calendar.DAY_OF_MONTH));
                assertEquals(expectedMeeting.getBeginDate().get(Calendar.HOUR_OF_DAY), meetingRepository.getMeetingList().get(i).getBeginDate().get(Calendar.HOUR_OF_DAY));
                assertEquals(expectedMeeting.getBeginDate().get(Calendar.MINUTE), meetingRepository.getMeetingList().get(i).getBeginDate().get(Calendar.MINUTE));
                assertEquals(expectedMeeting.getEndDate().get(Calendar.HOUR_OF_DAY), meetingRepository.getMeetingList().get(i).getEndDate().get(Calendar.HOUR_OF_DAY));
                assertEquals(expectedMeeting.getEndDate().get(Calendar.MINUTE), meetingRepository.getMeetingList().get(i).getEndDate().get(Calendar.MINUTE));
                assertEquals(expectedMeeting.getRoom().getName(), meetingRepository.getMeetingList().get(i).getRoom().getName());
                for (int j = 0; j < expectedMeeting.getParticipantList().size(); j++) {
                    assertEquals(expectedMeeting.getParticipantList().get(j).getEmail(), meetingRepository.getMeetingList().get(i).getParticipantList().get(j).getEmail());
                }
                foundMeeting = true;
            }
        }
        assertTrue(foundMeeting);
    }

    private Meeting getFakeMeeting() {
        String name = "Meeting Name Test";
        Calendar beginDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE, 45);
        Room room = new Room(3, "Salle 3");
        List<Participant> participantList = new ArrayList<Participant>();
        participantList.add(new Participant("test@yahoo.fr"));
        participantList.add(new Participant("test2@google.fr"));
        participantList.add(new Participant("test3@laposte.net"));
        return new Meeting(0L, name, beginDate, endDate, room, participantList);
    }



}