package com.ddy.dianmai.ops.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName FormatTimestampTag.java
 * @Description long类型时间格式化
 *
 * @author 1904
 * @version V1.0 
 * @Date 2015年7月23日 上午10:43:59
 */
@SuppressWarnings("all")
public class FormatTimestampTag extends TagSupport {
    public static String DTM_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private long value;
    private String pattern;

    @Override
    public int doStartTag() throws JspException {

        if (pattern == null || pattern.isEmpty()) pattern = DTM_FORMAT;

        try {
            JspWriter out = pageContext.getOut();
            String text = new SimpleDateFormat(pattern).format(new Date(value));
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
