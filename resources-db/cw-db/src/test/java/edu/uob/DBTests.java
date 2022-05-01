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

    /* Test create and use */
    @Test
    void testServer1() throws IOException {
        server.handleCommand("create  database test1 ;");
        assertEquals("[OK]: Current directory: test1", server.handleCommand("use test1;"));
    }

    @Test
    void testServer2() throws IOException {
        assertEquals("[ERROR]: Database test1 already exists", server.handleCommand("create database test1;"));
    }

    @Test
    void testServer3() throws IOException {
        assertEquals("[ERROR] Invalid query | Missing ;", server.handleCommand("create database test2 "));
    }

    @Test
    void testServer4() throws IOException {
        assertEquals("[OK]: Database test2 created", server.handleCommand("Create database test2;"));
    }

    @Test
    void testServer5() throws IOException {
        server.handleCommand("use test1;");
        assertEquals("[OK]: Table test1 created", server.handleCommand("CREATE table test1;"));
    }

    @Test
    void testServer6() throws IOException {
        server.handleCommand("use test1    ;");
        assertEquals("[OK]: Table test2 created", server.handleCommand("create tablE test2    (NAME,AGE,HEIGHT,SYMBOL);"));
    }

    @Test
    void testServer7() throws IOException {
        server.handleCommand("CREATE DATABASE test3;");
        server.handleCommand("USE test3     ;");
        server.handleCommand("CREATE TABLE test (name, mark, pass);");
        assertEquals("[OK]: Table deleted", server.handleCommand("drop table test;"));
    }

    @Test
    void testServer8() throws IOException {
        server.handleCommand("CREATE DATABASE test4;");
        server.handleCommand("USE test4     ;");
        assertEquals("[ERROR]: Table doesn't exist", server.handleCommand("drop table test;"));

    }

    @Test
    void testServer9() throws IOException {
        server.handleCommand("Create database test5   ;");
        server.handleCommand("USE test5 ;");
        assertEquals("[OK] Database deleted", server.handleCommand("DROP database test5;"));
    }

    @Test
    void testServer10() throws IOException {
        server.handleCommand("create dataBASE test6;");
        server.handleCommand("use test6;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        assertEquals("[OK]: Column testCol added", server.handleCommand("alter table marks add testCol;"));
    }

    @Test
    void testServer11() throws IOException {
        server.handleCommand("create dataBASE test7;");
        server.handleCommand("use test7;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        assertEquals("[OK]: Column mark dropped", server.handleCommand("alter table marks drop mark;"));
    }

    @Test
    void testServer12() throws IOException {
        server.handleCommand("create dataBASE test8;");
        server.handleCommand("use test8;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        assertEquals("[OK]: values inserted", server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);"));
    }

    @Test
    void testServer13() throws IOException {
        server.handleCommand("create dataBASE test9;");
        server.handleCommand("use test9;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertEquals("[OK]\n" +
                "name\tid\t\n" +
                "Steve\t1\t\n" +
                "Bob\t3\t\n" +
                "Clive\t4\t\n", server.handleCommand("SELECT name, id FROM marks WHERE name != 'Dave';"));
    }

    @Test
    void testServer14() throws IOException {
        server.handleCommand("create dataBASE test10;");
        server.handleCommand("use test10;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n", server.handleCommand("SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);"));
    }

    @Test
    void testServer15() throws IOException {
        server.handleCommand("create dataBASE test11;");
        server.handleCommand("use test11;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertEquals("[OK] Data updated", server.handleCommand("UPDATE marks SET mark = 0 WHERE name == 'Clive';"));
    }

    @Test
    void testServer16() throws IOException {
        server.handleCommand("create dataBASE test12;");
        server.handleCommand("use test12;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        assertEquals("[OK] Item(s) deleted", server.handleCommand("DELETE FROM marks WHERE name == 'Dave';"));
    }

    @Test
    void testServer17() throws IOException {
        server.handleCommand("create dataBASE test13;");
        server.handleCommand("use test13;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("CREATE TABLE coursework (task, grade);");
        server.handleCommand("INSERT INTO coursework VALUES ('OXO', 3);");
        server.handleCommand("INSERT INTO coursework VALUES ('DB', 1);");
        server.handleCommand("INSERT INTO coursework VALUES ('OXO', 4);");
        server.handleCommand("INSERT INTO coursework VALUES ('STAG', 2);");

        assertEquals("[OK]\n" +
                "id\ttask\tname\tmark\tpass\t\n" +
                "1\tOXO\tBob\t35\tFALSE\t\n" +
                "2\tDB\tSteve\t65\tTRUE\t\n" +
                "3\tOXO\tClive\t20\tFALSE\t\n" +
                "4\tSTAG\tDave\t55\tTRUE\t\n", server.handleCommand("JOIN coursework AND marks ON grade AND id;"));
    }

    @Test
    void testServer18() throws IOException {
        server.handleCommand("create dataBASE test14;");
        server.handleCommand("use test14;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("UPDATE marks SET mark = 0 WHERE name == 'Clive';");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n" +
                "1\tSteve\t65\tTRUE\t\n" +
                "2\tDave\t55\tTRUE\t\n" +
                "3\tBob\t35\tFALSE\t\n" +
                "4\tClive\t0\tFALSE\t\n", server.handleCommand("select * from marks;"));
    }

    @Test
    void testServer19() throws IOException {
        server.handleCommand("create dataBASE test15;");
        server.handleCommand("use test15;");
        server.handleCommand("CREATE TABLE people (name, age, email);");
        server.handleCommand("INSERT INTO people VALUES ('Bob', 24, 'bob@bob.net' );");
        server.handleCommand("INSERT INTO people VALUES ('Harry', 37, 'harry@harry.com' );");
        server.handleCommand("INSERT INTO people VALUES ('Chris', 20, 'chris@chris.ac.uk');");
        server.handleCommand("CREATE TABLE sheds (Name, height, PurchaserID);");
        server.handleCommand("INSERT INTO sheds VALUES ('Dorchester', 1800, 3 );");
        server.handleCommand("INSERT INTO sheds VALUES ('Plaza', 1200, 1 );");
        server.handleCommand("INSERT INTO sheds VALUES ('Excelsior', 1000, 2);");
        assertEquals("[OK]\n" +
                "id\tname\tage\temail\tName\theight\t\n" +
                "1\tBob\t24\tbob@bob.net\tPlaza\t1200\t\n" +
                "2\tHarry\t37\tharry@harry.com\tExcelsior\t1000\t\n" +
                "3\tChris\t20\tchris@chris.ac.uk\tDorchester\t1800\t\n", server.handleCommand("JOIN people AND sheds ON id AND PurchaserID;"));
    }

    @Test
    void testServer20() throws IOException {
        server.handleCommand("create dataBASE test16;");
        server.handleCommand("use test16;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("UPDATE marks SET mark = 0 WHERE name == 'Clive';");
        assertEquals("[OK]\n" +
                "name\tpass\t\n" +
                "Bob\tFALSE\t\n", server.handleCommand("SELECT name,pass FROM marks WHERE (pass == FALSE) AND (mark > 0);"));
    }

    @Test
    void testServer21() throws IOException {
        server.handleCommand("create dataBASE test17;");
        server.handleCommand("use test17;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n" +
                "4\tClive\t20\tFALSE\t\n" +
                "5\tLilian\t100\tFALSE\t\n", server.handleCommand("SELECT * FROM marks WHERE name LIKE 'li';"));
    }

    @Test
    void testServer22() throws IOException {
        server.handleCommand("create dataBASE test18;");
        server.handleCommand("use test18;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n" +
                "2\tDave\t55\tTRUE\t\n" +
                "3\tBob\t35\tFALSE\t\n", server.handleCommand("SELECT * FROM marks WHERE ((pass == FALSE) OR (mark > 35)) AND ((name == 'Dave') OR (name == 'Bob'));"));
    }

    @Test
    void testServer23() throws IOException {
        server.handleCommand("create dataBASE test19;");
        server.handleCommand("use test19;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        assertEquals("[ERROR]: Invalid query", server.handleCommand("SELECT * FROM marks pass == TRUE;"));
    }

    @Test
    void testServer24() throws IOException {
        server.handleCommand("create dataBASE test20;");
        server.handleCommand("use test20;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        assertEquals("[ERROR]: Table does not exist", server.handleCommand("SELECT * FROM crew;"));
    }

    @Test
    void testServer25() throws IOException {
        server.handleCommand("create dataBASE test21;");
        server.handleCommand("use test21;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        assertEquals("[ERROR] Invalid query | Missing ;", server.handleCommand("SELECT * FROM marks"));
    }

    @Test
    void testServer26() throws IOException {
        server.handleCommand("create dataBASE test22;");
        server.handleCommand("use test22;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        server.handleCommand("DELETE FROM marks WHERE mark<=55;");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n" +
                "1\tSteve\t65\tTRUE\t\n" +
                "5\tLilian\t100\tFALSE\t\n", server.handleCommand("SELECT * FROM marks;"));
    }

    @Test
    void testServer27() throws IOException {
        server.handleCommand("create dataBASE test23;");
        server.handleCommand("use test23;");
        server.handleCommand("CREATE TABLE marks (name, mark, pass);");
        server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        server.handleCommand("INSERT INTO marks VALUES ('Lilian', 100, FALSE);");
        server.handleCommand("DELETE FROM marks WHERE mark<=55;");
        assertEquals("[OK]\n" +
                "id\tname\tmark\tpass\t\n" +
                "1\tSteve\t65\tTRUE\t\n" +
                "5\tLilian\t100\tFALSE\t\n", server.handleCommand("SELECT * FROM marks;"));
    }

    @Test
    void testTokenizer1(){
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<String> methodOutputs = tokenizer.splitCommand("SELECT * FROM   123");
        String[] expectedOutputs = {"SELECT", "*", "FROM", "123"};
        Assertions.assertArrayEquals(expectedOutputs, methodOutputs.toArray());
    }

    @Test
    void testTokenizer2(){ //empty command
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<String> methodOutputs = tokenizer.splitCommand("         ");
        String[] expectedOutputs = {};
        Assertions.assertArrayEquals(expectedOutputs, methodOutputs.toArray());
    }

//    @Test
//    void testParseUse1() throws IOException{
//        Parser p = new Parser("Use db1;");
//        assertEquals("Current directory: db1", p.parse());
//    }
//
//    @Test
//    void testParseCreateTable1() throws IOException{
//        Parser p = new Parser("CREATE TABLE marks (name, mark, pass);");
//        assertEquals("OK", p.parse());
//    }
//
//    @Test
//    void testParseCreateTable2() throws IOException{
//        Parser p = new Parser("Create table x ();");
//        assertEquals("Missing attribute names", p.parse());
//    }
//
//    @Test
//    void testParseCreateTable3() throws IOException{
//        Parser p = new Parser("Create table ! ();");
//        assertEquals("Invalid table name", p.parse());
//    }
//
//    @Test
//    void testParseCreateTable4() throws IOException{
//        Parser p = new Parser("Create table x ;");
//        assertEquals("OK", p.parse());
//    }
//
//    @Test
//    void testParseCreateDB1() throws IOException{
//        Parser p = new Parser("Create database db1 ;");
//        assertEquals("OK", p.parse());
//    }
//
//    @Test
//    void testParseCreateDB2() throws IOException{
//        Parser p = new Parser("Create database db1");
//        assertEquals("Missing ;", p.parse());
//    }
//
//    @Test
//    void testParseCreateDB3() throws IOException{
//        Parser p = new Parser("Create database !;");
//        assertEquals("Invalid / missing database name", p.parse());
//    }
//


    // Add more unit tests or integration tests here.
    // Unit tests would test individual methods or classes whereas integration tests are geared
    // towards a specific usecase (i.e. creating a table and inserting rows and asserting whether the
    // rows are actually inserted)

}
