package com.inspiringteam.mrnews;


import android.content.res.Resources;
import com.google.android.material.navigation.NavigationView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.allOf;

/**
 * View actions for interacting with {@link NavigationView}
 */
public final class NavigationViewActions {

    private NavigationViewActions() {
        // no Instance
    }

    /**
     * Returns a {@link ViewAction} that navigates to a menu item in {@link NavigationView} using a
     * menu item resource id.
     *
     * View constraints:
     * <li>View must be a child of a DrawerLayout
     * <li>View must be of type {@link NavigationView}
     * <li>View must be visible on screen
     * <li>View must be displayed on screen
     * <ul>
     *
     * @param menuItemId the resource id of the menu item
     * @return a {@link ViewAction} that navigates on a menu item
     */
    public static ViewAction navigateTo(final int menuItemId) {

        return new ViewAction() {

            @Override
            public void perform(UiController uiController, View view) {
                NavigationView navigationView = (NavigationView) view;
                Menu menu = navigationView.getMenu();
                if (null == menu.findItem(menuItemId)) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(new RuntimeException(getErrorMessage(menu, view)))
                            .build();
                }
                menu.performIdentifierAction(menuItemId, 0);
                uiController.loopMainThreadUntilIdle();
            }

            private String getErrorMessage(Menu menu, View view) {
                String NEW_LINE = System.getProperty("line.separator");
                StringBuilder errorMessage = new StringBuilder("Menu item was not found, "
                        + "available menu items:")
                        .append(NEW_LINE);
                for (int position = 0; position < menu.size(); position++) {
                    errorMessage.append("[MenuItem] position=")
                            .append(position);
                    MenuItem menuItem = menu.getItem(position);
                    if (menuItem != null) {
                        CharSequence itemTitle = menuItem.getTitle();
                        if (itemTitle != null) {
                            errorMessage.append(", title=")
                                    .append(itemTitle);
                        }
                        if (view.getResources() != null) {
                            int itemId = menuItem.getItemId();
                            try {
                                errorMessage.append(", id=");
                                String menuItemResourceName = view.getResources()
                                        .getResourceName(itemId);
                                errorMessage.append(menuItemResourceName);
                            } catch (Resources.NotFoundException nfe) {
                                errorMessage.append("not found");
                            }
                        }
                        errorMessage.append(NEW_LINE);
                    }
                }
                return errorMessage.toString();
            }

            @Override
            public String getDescription() {
                return "click on menu item with id";
            }

            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(NavigationView.class),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        isDisplayingAtLeast(90)
                );
            }
        };

    }
}