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
    private int size = 6;//max number of bone

    public void init (ServletConfig config) throws ServletException
    {
            sContext = config.getServletContext();
            dominoService = new DominoService();
            sContext.setAttribute("allBones", dominoService.createAllBones(size));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String message;
        List<Bone> bones = new ArrayList<>();
        if("set".equalsIgnoreCase(request.getParameter("type"))){
            if (!"".equalsIgnoreCase(request.getParameter("count"))){
                int count = Integer.parseInt(request.getParameter("count"));
                bones = dominoService.getListBones(count,(List<Bone>)sContext.getAttribute("allBones"));
                message = "Set with " + count + " bones:";
            } else {
                message = "Enter number!";
            }
        } else{
            bones = dominoService.getListBones(dominoService.getRandomCount(size),(List<Bone>)sContext.getAttribute("allBones"));
            message = "Set with random bones (result = " + bones.size() + " ):";
        }
        sContext.setAttribute("setBones", bones);

        request.setAttribute("message", message);
        request.setAttribute("result", bones.toString());
        request.getRequestDispatcher("Create.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        List<Bone> bones = (List<Bone>)sContext.getAttribute("setBones");
        if(bones != null) {
            Long id_set = dominoService.insertSet(bones);
            sContext.setAttribute("id_set", id_set);
            request.setAttribute("current_set", bones.toString());
        }
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }
}
