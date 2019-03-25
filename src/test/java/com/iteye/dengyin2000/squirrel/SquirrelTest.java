package com.iteye.dengyin2000.squirrel;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

public class SquirrelTest {

    //the first {%} is the search result's title
    //the second {%} is the search result's link
    //the third {%} is the search result's snapshot link
    public static final String PATTERN = "&nbsp;-&nbsp;</span>{%}</div><div class=\"f13\">{*}data-tools=\"{&quot;title&quot;:&quot;{%}&quot;,&quot;url&quot;:&quot;{%}&quot;}\">{*}{'rsv_snapshot':'1'}\" href=\"{%}\" target=\"_blank\" class=\"m\">百度快照";

    @Test
    public void extractSearchResult() throws IOException {
        String text = readText("baidu_search_result.html");
        List<List<String>> results = Squirrel.extract(PATTERN, text);
        assertEquals("results should be 7", 7, results.size());

        List<String> firstSearchResult = results.get(0);

        assertEquals("first search result content", "这个手册将通过一个简单的 <em>Java</em> 项目向大家介绍如何使用 <em>Gradle</em> 构建 <em>Java</em> 项目。我们将要做什么?我们将在这篇文档航中创建一个简单的 <em>Java</em> 项目,然后...", firstSearchResult.get(0));


        assertEquals("first search result title", "使用Gradle 构建 Java 项目 - Heart_K - 博客园", firstSearchResult.get(1));

        assertEquals("first search result link", "http://www.baidu.com/link?url=IgYDHLXBmh7OSZoaSff-ouUAnUJ2dONnFtEiWwkRUpcLaUPDJLt0V0cGnxQojX-zdkYAIjTICZN59s2aIpZsl_", firstSearchResult.get(2));

        assertEquals("first search result snapshot link", "http://cache.baiducontent.com/c?m=9d78d513d99601fc0eb3c3690d678f355d0794247c89df4f3992d1098465285c5a23a6fe302267558f95223a54ee5e5c9da16b2d2a5977e4db8e9f4aabe2c9747a9565297716834118d819b8cb317f877fce&amp;p=8378ce0f85cc43ff57e6903a5340&amp;newp=8b2a971b8e9c5df108e29f7a1b6492695d0fc20e3cd4d201298ffe0cc4241a1a1a3aecbf23271202d8c37b650ba4495ce9f531743d0834f1f689df08d2ecce7e71d078282e&amp;user=baidu&amp;fm=sc&amp;query=gradle+java&amp;qid=9df7405300082ca1&amp;p1=1", firstSearchResult.get(3));
    }

    public String readText(String filename) throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
