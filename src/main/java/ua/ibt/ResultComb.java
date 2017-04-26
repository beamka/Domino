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
import java.util.Map;

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
        response.setContentType("text/html;charset=utf-8");
        List<Bone> bones = (List<Bone>) sContext.getAttribute("setBones");
        if(bones != null) {
            ArrayList<ArrayList<Bone>> allSequences = dominoService.getAllSequences(bones);
            String message;
            String combination = "";
            if ("max".equalsIgnoreCase(request.getParameter("type"))) {
                ArrayList<Bone> maxSequences = dominoService.getMaxLengthComb(allSequences);
                dominoService.insertComb(maxSequences, (Long) sContext.getAttribute("id_set"));

                combination = maxSequences.toString();
                message = "Max lenght of sequences is " + maxSequences.size() + " bones:";

            } else { //all combination
                int i = 0;
                for (ArrayList<Bone> oneComb : allSequences) {
                    dominoService.insertComb(oneComb, (Long) sContext.getAttribute("id_set"));
                    i++;
                    combination = combination + "№" + i + " = " + oneComb.toString() + "<br>";
                }
                message = "Found " + i + " possible sequences:";
            }

            request.setAttribute("current_set", bones);
            request.setAttribute("result", combination);
            request.setAttribute("message", message);
        }
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        List<Map<String, Object>> history =dominoService.showHistory();
        String result = "";
        for (Map<String, Object> row : history) {
            result = result + " # " + row.get("set") + " <br>";
            List<String> combs = (List<String>)row.get("comb");
            for(String comb : combs){
                result = result + "  ~~~ " + comb + "<br>";
            }
        }
        request.setAttribute("history", result);
        request.getRequestDispatcher("Result.jsp").forward(request, response);
    }
}
