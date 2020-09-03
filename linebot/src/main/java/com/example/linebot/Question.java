package com.example.linebot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.*;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Question {
    private static final String URL = "jdbc:h2:file:~/h2db/lineBot-database;Mode=PostgreSQL;AUTO_SERVER=TRUE;;MV_STORE=false";
    private static final String USER_NAME = "b2191050";
    private static final String USER_PASS = "b2191050";
    public String kamoku;

    public Question(String kamoku) {
        this.kamoku = kamoku;
    }

    @EventMapping
    public List<PreExam> selectPreExams(int questionNumber) throws Exception{
        List<PreExam> returning = new ArrayList<PreExam>();
        String sql;
        switch (kamoku){
            case "英単語":
                sql = "select * from 英単語 where questionNumber = ?";
                break;
            case "社会":
                sql = "select * from 社会 where questionNumber = ?";
                break;
            default:
                sql = "select * from 化学 where questionNumber = ?";
                break;
        }
        //String sql = "select * from 英単語 where questionNumber = ?";

        try(Connection conn = DriverManager.getConnection(URL,USER_NAME,USER_PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,questionNumber);
            ResultSet results = stmt.executeQuery();
            while (results.next()){
                var col0 = results.getInt("questionNumber");
                var col1 = results.getString("monndai");
                var col2 = results.getString("sentaku1");
                var col3 = results.getString("sentaku2");
                var col4 = results.getString("sentaku3");
                var col5 = results.getString("sentaku4");
                var col6 = results.getInt("answer");
                var col7 = results.getString("kaisetu");
                var preExam = new PreExam(col0,col1,col2,col3,col4,col5,col6,col7);
                returning.add(preExam);
            }
        }
        return returning;
    }
}