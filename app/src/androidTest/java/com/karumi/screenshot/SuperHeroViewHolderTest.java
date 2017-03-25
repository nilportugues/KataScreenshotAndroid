/*
 * Copyright (C) 2017 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.screenshot;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.view.LayoutInflater;
import android.view.View;

import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroesRepository;
import com.karumi.screenshot.ui.presenter.SuperHeroesPresenter;
import com.karumi.screenshot.ui.view.SuperHeroDetailActivity;
import com.karumi.screenshot.ui.view.SuperHeroViewHolder;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SuperHeroViewHolderTest extends ScreenshotTest {

    private static final int SUPER_HERO_ROW_HEIGHT = R.dimen.super_hero_row_height;
    private static final int SUPER_HERO_LAYOUT = R.layout.super_hero_row;

    @Mock
    private SuperHeroesRepository repository;

    @Rule
    public ActivityTestRule<SuperHeroDetailActivity> activityRule
            = new ActivityTestRule<>(SuperHeroDetailActivity.class, true, false);

    @Test
    public void showsAnySuperHero() {
        final SuperHero superHero = givenASuperHero();
        final SuperHeroViewHolder holder = givenASuperHeroViewHolder();
        holder.render(superHero);

        compareScreenshot(holder, R.dimen.super_hero_row_height);
    }

    @Test
    public void showsSuperHeroesWithLongNames() {
        final SuperHero superHero = givenASuperHeroWithALongName();
        compareScreenshotWithSuperHeroe(superHero);
    }

    private void compareScreenshotWithSuperHeroe(SuperHero superHero) {
        final SuperHeroViewHolder holder = givenASuperHeroViewHolder();
        holder.render(superHero);

        compareScreenshot(holder, SUPER_HERO_ROW_HEIGHT);
    }

    @Test
    public void showsSuperHeroesWithLongDescriptions() {
        final SuperHero superHero = givenASuperHeroWithALongDescription();
        compareScreenshotWithSuperHeroe(superHero);
    }

    @Test
    public void showsAvengersBadge() {
        final SuperHero superHero = givenAnAvenger();
        compareScreenshotWithSuperHeroe(superHero);
    }

    private SuperHeroViewHolder givenASuperHeroViewHolder() {
        final Context context = getInstrumentation().getTargetContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(SUPER_HERO_LAYOUT, null, false);

        return new SuperHeroViewHolder(view, mock(SuperHeroesPresenter.class));
    }

    private SuperHero givenASuperHeroWithALongDescription() {
        String superHeroName = "Super Hero Name";
        String superHeroDescription =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt "
                        + "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
                        + "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
                        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
                        + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt "
                        + "mollit anim id est laborum.";

        return givenASuperHero(superHeroName, superHeroDescription, false);
    }

    private SuperHero givenASuperHeroWithALongName() {
        String superHeroName =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt "
                        + "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
                        + "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
                        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
                        + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt "
                        + "mollit anim id est laborum.";
        String superHeroDescription = "Description Super Hero";

        return givenASuperHero(superHeroName, superHeroDescription, false);
    }

    private SuperHero givenAnAvenger() {
        return givenASuperHero("Super Hero Name", "Super Hero Description", true);
    }

    private SuperHero givenASuperHero() {
        return givenASuperHero("Super Hero Name", "Super Hero Description", false);
    }

    private SuperHero givenASuperHero(String superHeroName,
                                      String superHeroDescription,
                                      boolean isAvenger) {
        return new SuperHero(superHeroName, null, isAvenger, superHeroDescription);
    }



    private SuperHero givenThereIsASuperHero(boolean isAvenger) {
        final String superHeroName = "SuperHero";
        final String superHeroDescription = "Super Hero Description";
        final SuperHero superHero = new SuperHero(superHeroName, null, isAvenger, superHeroDescription);

        when(repository.getByName(superHeroName)).thenReturn(superHero);

        return superHero;
    }

    private SuperHeroDetailActivity startActivity(SuperHero superHero) {
        final Intent intent = new Intent();
        intent.putExtra("super_hero_name_key", superHero.getName());

        return activityRule.launchActivity(intent);
    }


    @Test
    public void itShouldShowAnAvenger() {
        final SuperHero superHero = givenThereIsASuperHero(true);
        final SuperHeroDetailActivity activity = startActivity(superHero);

        compareScreenshot(activity);
    }
}