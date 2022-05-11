package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class BasicCommandTests {

  private GameServer server;

  // Make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
      File entitiesFile = Paths.get("config/extended-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config/extended-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  // Test to spawn a new server and send a simple "look" command
  @Test
  void testLookingAroundStartLocation() {
    String response = server.handleCommand("player 1: look").toLowerCase();
    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  @Test
  void test1() {
    server.handleCommand("Name: goto forest").toLowerCase();
    server.handleCommand("Name: get key").toLowerCase();
    server.handleCommand("Name: goto cabin").toLowerCase();
    String response = server.handleCommand("Name: open trapdoor").toLowerCase();
//    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
//    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
//    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
}

  @Test
  void test2() {
    server.handleCommand("Name: get axe").toLowerCase();
    server.handleCommand("Name: goto forest").toLowerCase();
    server.handleCommand("Name: cut tree").toLowerCase();
//    String response = server.handleCommand("Name: open trapdoor").toLowerCase();
//    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
//    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
//    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  @Test
  void test3() {
    server.handleCommand("Name: goto forest").toLowerCase();
    server.handleCommand("Name: get key").toLowerCase();
    server.handleCommand("Name: goto cabin").toLowerCase();
    server.handleCommand("Name: open trapdoor").toLowerCase();
    server.handleCommand("Name: goto cellar").toLowerCase();
    server.handleCommand("Name: attack elf").toLowerCase();
    server.handleCommand("Name: attack elf").toLowerCase();
//    String response = server.handleCommand("Name: open trapdoor").toLowerCase();
//    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
//    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
//    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  @Test
  void test4() {
    server.handleCommand("Name: get potion").toLowerCase();
    server.handleCommand("Name: get axe").toLowerCase();
    server.handleCommand("Name: goto forest").toLowerCase();
    server.handleCommand("Name: get key").toLowerCase();
    server.handleCommand("Name: goto cabin").toLowerCase();
    server.handleCommand("Name: open trapdoor").toLowerCase();
    server.handleCommand("Name: goto cellar").toLowerCase();
    server.handleCommand("Name: attack elf").toLowerCase();
    server.handleCommand("Name: attack elf").toLowerCase();
    server.handleCommand("Name: attack elf").toLowerCase();
//    String response = server.handleCommand("Name: open trapdoor").toLowerCase();
//    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
//    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
//    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  // Add more unit tests or integration tests here.

}
