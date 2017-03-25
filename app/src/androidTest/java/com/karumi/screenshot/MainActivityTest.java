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

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.karumi.screenshot.di.MainComponent;
import com.karumi.screenshot.di.MainModule;
import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroesRepository;
import com.karumi.screenshot.ui.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.when;

public class MainActivityTest extends ScreenshotTest {
    @Mock
    SuperHeroesRepository repository;

    @Rule
    public DaggerMockRule<MainComponent> daggerRule =
            new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
                    new DaggerMockRule.ComponentSetter<MainComponent>() {
                        @Override
                        public void setComponent(MainComponent component) {
                            SuperHeroesApplication app =
                                    (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                                            .getTargetContext()
                                            .getApplicationContext();
                            app.setComponent(component);
                        }
                    });

    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class, true, false);

    @Test
    public void showsEmptyCaseIfThereAreNoSuperHeroes() {
        givenThereAreNoSuperHeroes();

        Activity activity = startActivity();

        compareScreenshot(activity);
    }


    @Test
    public void showJustFiveSuperHeroes() {
        givenThereAreSomeSuperHeroes(5, false);
        Activity activity = startActivity();
        compareScreenshot(activity);
    }

    @Test
    public void showJustTenSuperHeroesTop() {
        givenThereAreSomeSuperHeroes(10, false);
        Activity activity = startActivity();
        compareScreenshot(activity);

        // we need to scroll down and take a second screenshot...
        // and add a number to the screenshot, otherwise it will override the screenshot
        // ... or
        // Have TOP and BOTTOM tests.
    }

    @Test
    public void showJustTenSuperHeroesBottom() {
        givenThereAreSomeSuperHeroes(10, false);
        Activity activity = startActivity();
        //force scroll to bottom

        //now take screenshot.
        compareScreenshot(activity);
    }

    private List<SuperHero> givenThereAreSomeSuperHeroes(int numberOfSuperHeroes, boolean avengers) {
        List<SuperHero> superHeroes = new LinkedList<>();
        for (int i = 0; i < numberOfSuperHeroes; i++) {
            String superHeroName = "SuperHero - " + i;
            String superHeroDescription = "Description Super Hero - " + i;
            SuperHero superHero = new SuperHero(superHeroName, null, avengers, superHeroDescription);
            superHeroes.add(superHero);
            when(repository.getByName(superHeroName)).thenReturn(superHero);
        }
        when(repository.getAll()).thenReturn(superHeroes);
        return superHeroes;
    }

    private void givenThereAreNoSuperHeroes() {
        when(repository.getAll()).thenReturn(Collections.<SuperHero>emptyList());
    }

    private MainActivity startActivity() {
        return activityRule.launchActivity(null);
    }


}