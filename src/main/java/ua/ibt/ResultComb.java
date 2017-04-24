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
 * Created by IRINA on 23.04.2017.
 */
@WebServlet("/getComb")
public class ResultComb extends HttpServlet {
    private DominoService dominoService;
    private ServletContext sContext;

    public void init (ServletConfig config) throws ServletException
    {
        sContext = config.getServletContext();
        dominoService = new DominoService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Start servlet ResultComb doGet");
        response.setContentType("text/html;charset=utf-8");
        List<Bone> bones = (List<Bone>) sContext.getAttribute("setBones");
        System.out.println("Bones" + bones);
        ArrayList<ArrayList<Bone>> allSequences = dominoService.getAllSequences(bones);
        System.out.println("allComb" + allSequences);
        String message;
        String combination = "";
        if ("max".equalsIgnoreCase(request.getParameter("type"))) {
            ArrayList<Bone>  maxSequences = dominoService.getMaxLenghtComb(allSequences);
            dominoService.insertComb(maxSequences,(Long)sContext.getAttribute("id_set"));

            System.out.println("maxSequences" + maxSequences);
            combination = maxSequences.toString();
            System.out.println("combination" + combination);
            message = "Max lenght of sequences is " + maxSequences.size() + " bones:";

        } else { //all combination
            int i = 0;
            for (ArrayList<Bone> oneComb : allSequences) {
                dominoService.insertComb(oneComb,(Long)sContext.getAttribute("id_set"));
                i++;
                combination = combination + "№" + i + " = " + oneComb.toString() + "<br>";
            }
            message = "Found " + i + " possible combinations:";
            System.out.println("message" + message);
            System.out.println("combination" + combination);
        }

        request.setAttribute("current_set", bones);
        request.setAttribute("result", combination);
        request.setAttribute("message", message);
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Start servlet ResultComb doPost");
        response.setContentType("text/html;charset=utf-8");
        List<Bone> bones = (List<Bone>)sContext.getAttribute("setBones");
        Long id_set = dominoService.insertSet(bones);
        sContext.setAttribute("id_set", id_set);
        request.setAttribute("current_set", bones.toString());
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }
}
