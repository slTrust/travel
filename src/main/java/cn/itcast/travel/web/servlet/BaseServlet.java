package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("base Servlet 的 service被执行了");
        // 通过 urL路径 和 反射 调用对应的 子类方法

        // 完成方法的分发
        /* 比如 访问的路径是 /user/add
           add就是 UserServlet里的 add方法
        */

        // 1. 获取请求路径
        String uri = req.getRequestURI(); //  /travel/user/add
        System.out.println("请求uri:" + uri);
        // 2. 获取方法名称
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println("方法名称：" + methodName);
        // 3. 获取方法对象

        // 问题 访问 /travel/user/add 这时候 this是谁？ 答案是 UserServlet
        // 谁调用我？我就是谁


        try {
            // 这样并不是最好,推荐把 对应类的方法写成 public修饰的
            // getDeclaredMethod 是忽略访问修饰符的限制
            // getMethod 无法访问修饰符 protected private 修饰的
            // Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
            // 4. 执行方法
            // 暴力反射(执行方法之前)
            // method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     *直接将传入的对象 序列化为json,并且写回客户端
      */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为 json
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
