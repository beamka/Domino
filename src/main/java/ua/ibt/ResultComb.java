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
        List<Bone> bones = (List<Bone>) sContext.getAttribute("bones");
        System.out.println("bones" + bones);
        ArrayList<ArrayList<Bone>> allComb = dominoService.getComb(bones);
        System.out.println("allComb" + allComb);
        String message;
        String combination = "";
        if ("max".equalsIgnoreCase(request.getParameter("type"))) {
            ArrayList<Bone>  maxLenght = dominoService.getMaxLenghtComb(allComb);
            System.out.println("maxLenght" + maxLenght);
            combination = maxLenght.toString();
            System.out.println("combination" + combination);
            message = "Max lenght of combination is " + maxLenght.size() + " bones:";

        } else { //all combination
            int i = 0;
            for (ArrayList<Bone> oneComb : allComb) {
                i++;
                combination = combination + "№" + i + " = " + oneComb.toString() + "<br>";
            }
            message = "Found " + i + " possible combinations:";
            System.out.println("message" + message);
            System.out.println("combination" + combination);
        }
        request.setAttribute("result", combination);
        request.setAttribute("message", message);
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }
}
