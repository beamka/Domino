package ua.ibt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IRINA on 22.04.2017.
 */
@WebServlet("/getBones")
public class CreateSet extends HttpServlet {
    private DominoService dominoService;
    private ServletContext sContext;

    public void init (ServletConfig config) throws ServletException
    {
        sContext = config.getServletContext();
        dominoService = new DominoService();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Start servlet CreateSet doPost");
        response.setContentType("text/html;charset=utf-8");
        String message;
        List<Bone> bones = new ArrayList<>();
        if("set".equalsIgnoreCase(request.getParameter("type"))){
            if (!"".equalsIgnoreCase(request.getParameter("count"))){
                int count = Integer.parseInt(request.getParameter("count"));
                bones = dominoService.getBones(count);
                message = "Set with " + count + " bones:";
            } else {
                message = "Enter number!";
            }
        } else{
            bones = dominoService.getBones();
            message = "Set with random bones (result = " + bones.size() + " ):";
        }
        sContext.setAttribute("bones", bones);

        request.setAttribute("message", message);
        request.setAttribute("result", bones.toString());
        request.getRequestDispatcher("Create.jsp").forward(request, response);
    }
}
