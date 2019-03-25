package com.iteye.dengyin2000.squirrel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a util class, which is used for extracting string from a huge large
 * text.
 *
 * it's easier than using regex expression. Please see unit tests for usage.
 *
 * User: denny
 * Date: Feb 25, 2009
 * Time: 2:36:40 PM
 */
public class Squirrel {

    //it can be any chars
    public static final String OMMIT = "{*}";
    //it's the extracted chars
    public static final String FETCH = "{%}";

    public static List<List<String>> extract(String pattern, String content) {
        if (content == null || content.trim().length() == 0) {
            return Collections.EMPTY_LIST;
        }
        if (pattern == null || pattern.trim().length() == 0) {
            return Collections.EMPTY_LIST;
        }
        pattern = pattern.trim();
        if (pattern.startsWith(OMMIT) || pattern.startsWith(FETCH) || pattern.endsWith(OMMIT) || pattern.endsWith(FETCH)) {
            throw new RuntimeException("pattern error.");
        }

        List<String> pList = buildTokenList(pattern);

        int tokenCount = 0;
        for (String token : pList) {
            if (FETCH.equals(token)) {
                tokenCount ++;
            }
        }

        List<List<String>> rt = new ArrayList<List<String>>();
        int cursor = 0;

        out:while (cursor < content.length() - 1) {
            // -1 eat, 0, ommit, 1 fetch
            int stat = -1;
            List<String> row = new ArrayList<String>();
            for (String token : pList) {
                if (OMMIT.equals(token)) {
                    stat = 0;
                    continue;
                } else if (FETCH.equals(token)) {
                    stat = 1;
                    continue;
                }
                int index = content.indexOf(token, cursor);
                if (index == -1) {
                    break out;
                } else {
                    switch (stat) {
                        case -1:
                            cursor = index + token.length();
                            break;
                        case 0:
                            cursor = index + token.length();
                            break;
                        case 1:
                            row.add(content.substring(cursor, index));
                            cursor = index + token.length();
                            stat = -1;
                            break;
                    }
                }
            }

            if (row.size() == tokenCount) {
                rt.add(row);
            }else{
                break;
            }
        }


        return rt;
    }

    private static List<String> buildTokenList(String pattern) {
        List<String> rt = new ArrayList<String>();
        int cursor = 0;
        do {
            int ommitIndex = pattern.indexOf(OMMIT, cursor);
            int fetchIndex = pattern.indexOf(FETCH, cursor);
            if (ommitIndex == -1 && fetchIndex == -1) {
                rt.add(pattern.substring(cursor));
                return rt;
            }

            if (ommitIndex != -1 && fetchIndex != -1) {
                if (ommitIndex < fetchIndex) {
                    rt.add(pattern.substring(cursor, ommitIndex));
                    rt.add(OMMIT);
                    cursor = ommitIndex + 3;
                } else {
                    rt.add(pattern.substring(cursor, fetchIndex));
                    rt.add(FETCH);
                    cursor = fetchIndex + 3;
                }
            }else{

                if (ommitIndex == -1) {
                    rt.add(pattern.substring(cursor, fetchIndex));
                    rt.add(FETCH);
                    cursor = fetchIndex + 3;
                } else {
                    rt.add(pattern.substring(cursor, ommitIndex));
                    rt.add(OMMIT);
                    cursor = ommitIndex + 3;
                }
            }

        } while (true);
    }
}