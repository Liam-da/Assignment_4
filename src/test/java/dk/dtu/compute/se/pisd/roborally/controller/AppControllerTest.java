package dk.dtu.compute.se.pisd.roborally.controller;
import java.io.File;


import javafx.application.Platform;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {

    private AppController appController;
    private static boolean isJavaFXInitialized = false;

    /**
     * Ensure JavaFX is initialized before running tests.
     */
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        if (!isJavaFXInitialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown); // Initialiser JavaFX-platformen
            latch.await(5, TimeUnit.SECONDS);
            isJavaFXInitialized = true;
        }
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            appController = new AppController(new RoboRally());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * Test that a new game initializes correctly.
     */
    @Test
    void testNewGameInitialization() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> appController.newGame(), "New game initialization should not throw an exception.");
            assertTrue(appController.isGameRunning(), "Game should be running after initialization.");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * Test saving the game.
     */
    @Test
    void testSaveGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            appController.newGame();
            appController.saveGame();
            assertTrue(new File("roborally_save.dat").exists(), "Saved game file should exist.");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * Test stopping a game.
     */
    @Test
    void testStopGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            appController.newGame();
            assertTrue(appController.stopGame(), "Game should be stopped successfully.");
            assertFalse(appController.isGameRunning(), "No game should be running after stopping.");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
