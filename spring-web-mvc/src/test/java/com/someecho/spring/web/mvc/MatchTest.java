package com.someecho.spring.web.mvc;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author : linghan.ma
 * @Package com.someecho.spring.web.mvc
 * @Description:
 * @date Date : 2020年04月01日 9:14 PM
 **/
public class MatchTest {

    public static void main(String[] args) throws Throwable{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("aaa");
        Set<String> groupName = new HashSet<>();
        final String regex = "\\{#(\\S+?)\\}";
//        final String regex = "((\\{#(\\S+?)\\})|(\\(公司印章位置\\))|(\\(员工印章位置\\)))";
        File file = new File("/Users/malinghan/IdeaProjects/spring-in-action/spring-web-mvc/src/test/java/com/someecho/spring/web/mvc/aaa.txt");
        InputStreamReader inputStreamReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        String str = line;
        while (line!=null){
            System.out.println(str);
            line = bufferedReader.readLine();
            str = str + line;
        }
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            groupName.add(matcher.group(0));
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
        System.out.println("============================");
        System.out.println(groupName);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }


    @Test
    public void testMatch() throws Throwable{
        Set<String> groupName = new TreeSet<>();
        Set<String> label = new TreeSet<>();
//        final String regex = "\\{#(\\S+?)\\}";
        final String regex = "((\\{#(\\S+?)\\})|(\\(公司印章位置\\))|(\\(员工印章位置\\)))";
        File file = new File("/Users/malinghan/IdeaProjects/spring-in-action/spring-web-mvc/src/test/java/com/someecho/spring/web/mvc/aaa.txt");
        InputStreamReader inputStreamReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        String str = line;
        while (line!=null){
            line = bufferedReader.readLine();
            str = str + line;
        }
        System.out.println(str);
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            groupName.add(matcher.group(0));
            System.out.println("Full match: " + matcher.group(0));
            if(matcher.group(0).startsWith("{#")){
                label.add(matcher.group(0).substring(2,matcher.group(0).length()-1));
            }
            if(matcher.group(0).startsWith("(")){
                label.add(matcher.group(0).substring(1,matcher.group(0).length()-1));
            }
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                System.out.println("Group " + i + ": " + matcher.group(i));
//            }
        }
        System.out.println("============================");
        System.out.println(groupName.size());
        System.out.println("============================");
        System.out.println(label.size());

    }
}

