package edu.uob;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class DBTests{

    private DBServer server;

    // we make a new server for every @Test (i.e. this method runs before every @Test test case)
    @BeforeEach
    void setup(@TempDir File dbDir) {
        // Notice the @TempDir annotation, this instructs JUnit to create a new temp directory somewhere
        // and proceeds to *delete* that directory when the test finishes.
        // You can read the specifics of this at
        // https://junit.org/junit5/docs/5.4.2/api/org/junit/jupiter/api/io/TempDir.html

        // If you want to inspect the content of the directory during/after a test run for debugging,
        // simply replace `dbDir` here with your own File instance that points to somewhere you know.
        // IMPORTANT: If you do this, make sure you rerun the tests using `dbDir` again to make sure it
        // still works and keep it that way for the submission.

        server = new DBServer(dbDir);
    }

    // Here's a basic test for spawning a new server and sending an invalid command,
    // the spec dictates that the server respond with something that starts with `[ERROR]`
    @Test
    void testInvalidCommandIsAnError() throws IOException {
        assertTrue(server.handleCommand("foo").startsWith("[ERROR]"));
    }

//    @Test
//    void testServer() throws IOException {
//        server.handleCommand("USE db1;");
//        server.handleCommand("Create table x;");
////        assertTrue(server.handleCommand("foo").startsWith("[ERROR]"));
//    }

    @Test
    void testServer1() throws IOException {
        server.handleCommand("create  database test ;");
        server.handleCommand("use test;");
        server.handleCommand("Create table test1 (Name, Age, Email);");
        assertEquals("[ERROR] Invalid query", server.handleCommand("drop x;"));
    }

    @Test
    void testServer2() throws IOException {
        server.handleCommand("create  database test1 ;");
        server.handleCommand("use test1;");
        server.handleCommand("Create table test1 (Name, Age, Email);");
        server.handleCommand("DROP DATABASE test1;");
        assertEquals("[ERROR] No database, please create", server.handleCommand("create table x;"));
    }

    @Test
    void testServer3() throws IOException {
        assertEquals("[ERROR] Invalid query | Missing ;", server.handleCommand("create database x "));
    }

    @Test
    void testServer4() throws IOException {
        server.handleCommand("create  database test1 ;");
        server.handleCommand("use test1;");
        server.handleCommand("Create table test1;");
        server.handleCommand("DROP table test1;");
        assertEquals("[OK]", server.handleCommand("Create table test2;"));
    }

    @Test
    void testServer5() throws IOException {
        server.handleCommand("create  database test1 ;");
        server.handleCommand("use test1;");
        assertEquals("[OK] Database deleted", server.handleCommand("DROP database test1;"));
    }

    @Test
    void testServer6() throws IOException {
        server.handleCommand("use db1    ;");
        server.handleCommand("alter table x add Sex;");
//        server.handleCommand("insert into x values('Tom', 55.55, 'TOM@gmail.com','M');");
        assertEquals("[OK]", server.handleCommand("alter table x drop Email;"));
    }

    @Test
    void testServer7() throws IOException {
        server.handleCommand("use db1    ;");
        server.handleCommand("ALTER TABLE x add test;");
//        assertEquals("[OK] Database deleted", server.handleCommand("DROP database test1;"));
    }


    @Test
    void testTokenizer1(){
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<String> methodOutputs = tokenizer.splitCommand("SELECT * FROM   123");
        String[] expectedOutputs = {"select", "*", "from", "123"};
        Assertions.assertArrayEquals(expectedOutputs, methodOutputs.toArray());
    }

    @Test
    void testTokenizer2(){ //empty command
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<String> methodOutputs = tokenizer.splitCommand("         ");
        String[] expectedOutputs = {};
        Assertions.assertArrayEquals(expectedOutputs, methodOutputs.toArray());
    }

    @Test
    void testParseUse1() throws IOException{
        Parser p = new Parser("Use db1;");
        assertEquals("Current directory: db1", p.parse());
    }

    @Test
    void testParseCreateTable1() throws IOException{
        Parser p = new Parser("CREATE TABLE marks (name, mark, pass);");
        assertEquals("OK", p.parse());
    }

    @Test
    void testParseCreateTable2() throws IOException{
        Parser p = new Parser("Create table x ();");
        assertEquals("Missing attribute names", p.parse());
    }

    @Test
    void testParseCreateTable3() throws IOException{
        Parser p = new Parser("Create table ! ();");
        assertEquals("Invalid table name", p.parse());
    }

    @Test
    void testParseCreateTable4() throws IOException{
        Parser p = new Parser("Create table x ;");
        assertEquals("OK", p.parse());
    }

    @Test
    void testParseCreateDB1() throws IOException{
        Parser p = new Parser("Create database db1 ;");
        assertEquals("OK", p.parse());
    }

    @Test
    void testParseCreateDB2() throws IOException{
        Parser p = new Parser("Create database db1");
        assertEquals("Missing ;", p.parse());
    }

    @Test
    void testParseCreateDB3() throws IOException{
        Parser p = new Parser("Create database !;");
        assertEquals("Invalid / missing database name", p.parse());
    }



    // Add more unit tests or integration tests here.
    // Unit tests would test individual methods or classes whereas integration tests are geared
    // towards a specific usecase (i.e. creating a table and inserting rows and asserting whether the
    // rows are actually inserted)

}
