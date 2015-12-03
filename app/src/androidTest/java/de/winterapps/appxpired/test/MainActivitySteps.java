package de.winterapps.appxpired.test;

import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import cucumber.api.CucumberOptions;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.menuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

/**
  * Created by CodeLionX on 01.11.2015.
  */
@CucumberOptions(features = "features")
public class MainActivitySteps extends ActivityInstrumentationTestCase2<menuActivity> {

        public MainActivitySteps() {
        super(menuActivity.class);
        }

        @Given("^I see MainActivity$")
        public void I_see_MainActivity() {
         assertNotNull(getActivity());
         }

        @When("^I press nothing$")
        public void I_see_MainActivity1() {
        }

        @Then("^I see \"(.*?)\"$")
        public void I_see_MainActivity2(final String w) {
         onView(withId(R.id.menuAddButton)).check(matches(withText(w)));
        }

    @Given("^the user is logged in at appXpired$")
    public void the_user_is_logged_in_at_appXpired() {
        assertNotNull(getActivity());
    }

    @When("^he clicks on Add$")
    public void he_clicks_on_Add()  {
        onView(withId(R.id.menuAddButton)).perform(click());
    }

    @And("^he writes a valid name into the input field \"([^\"]*)\"$")
    public void he_writes_a_valid_name_into_the_input_field(String arg1)  {
        onView(withId(R.id.editName)).perform(click());
        onView(withId(R.id.editName)).perform(typeTextIntoFocusedView(arg1));
        onView(withId(R.id.editName)).perform(closeSoftKeyboard());


    }

    @And("^he clicks on add$")
    public void he_clicks_on_add() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            Log.v("OhOh", "OhOh");
            e.printStackTrace();
        }
        onView(withId(R.id.addAddButton)).perform(click());
    }


    @Then("^he will see a success message$")
    public void he_will_see_a_success_message() {
        //Todo
    }

    @When("^he clicks on Show$")
    public void he_clicks_on_Show() throws Throwable {
        onView(withId(R.id.menuShowButton)).perform(click());
    }

    @And("^he clicks on consume next to an entry of food from the list$")
    public void he_clicks_on_consume_next_to_an_entry_of_food_from_the_list()  {
        onView(withId(R.id.listView));

    }
}