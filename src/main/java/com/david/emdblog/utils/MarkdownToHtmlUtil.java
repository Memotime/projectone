package com.david.emdblog.utils;

import java.io.FileReader;
import java.io.IOException;
/**
 * @Author ：David
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */

import com.youbenzi.mdtool.tool.MDTool;
/**
 * markdown转html工具类
 *
 */
public class MarkdownToHtmlUtil {

    public static String mdToHtml(String markdownContent) throws IOException{
        //java.io.InputStream in = this.getClass().getResourceAsStream("markdown.md");
//        String html = null;
//        FileReader r = new FileReader("markdown.md");
//        char[] cbuf = new char[1024];
//        while( r.read(cbuf) != -1){
//            html = new String(cbuf);
//        }
//
//        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
//        html = pdp.markdownToHtml(html);
//        return html;
    	 return MDTool.markdown2Html(markdownContent);
    }
}
