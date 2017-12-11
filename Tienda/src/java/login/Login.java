package login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entidades.Usuario;
import utilidades.UtilidadesClave;
import utilidades.UtilidadesBase;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import utilidades.UtilidadesCaptcha;

/**
 *
 * @author pepo
 */
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs Metodo que maneja la peticion
     * de inicio de sesion(bloqueo de usuario y errores)
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            for (Enumeration<String> atributos = request.getAttributeNames(); atributos.hasMoreElements();) {
                System.out.println("ATRIBUTOS: " + atributos.nextElement());
            }
            for (Enumeration<String> nombres = request.getParameterNames(); nombres.hasMoreElements();) {
                System.out.println("PARAMETRO: " + nombres.nextElement());
            }
           
                String remoteAddr = request.getRemoteAddr();
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey("6LfTkjwUAAAAANB9t8czqKIjCGkORjPKC_mXqk6m");
            String challenge = request.getParameter("recaptcha_challenge_field");
            String uresponse = request.getParameter("recaptcha_response_field");
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
            HttpSession sesion = request.getSession();
            response.setContentType("text/html;charset=UTF-8");
            Usuario user = new Usuario();
            user.setCorreo((String) request.getParameter("correo"));
            user.setClave((String) request.getParameter("clave"));
            int intento;
            if (sesion.getAttribute("intento") == null) {
                intento = 0;
            } else {
                intento = (int) sesion.getAttribute("intento");
            }
            String claveclaro = user.getClave();
            UtilidadesClave utility = new UtilidadesClave();
            UtilidadesBase base = new UtilidadesBase();
            user = base.IniciarSesion(user);
            boolean igualdad = false;
            if (user.getClave() != null) {
                igualdad = utility.check(claveclaro, user.getClave());
            }
             if (!reCaptchaResponse.isValid()) 
                 igualdad=false;
            if (igualdad) {
                sesion.invalidate();
                sesion = request.getSession();
                sesion.setAttribute("usuario", user);
                String nextJSP = "/tienda.jsp";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("error", "Datos incorrectos");
                            if (!reCaptchaResponse.isValid()) {
                                request.setAttribute("error", "Complete el captcha correctamente");
                                }
                if (user.getCorreo() != null && reCaptchaResponse.isValid()) {
                    intento++;
                }
                if (intento >= 4) {
                    intento = 0;
                    base.bloquearUsuario(user);
                    request.setAttribute("error", "Su usuario ha sido bloqueado");
                }
                System.out.println("INTENTO: " + intento);
                sesion.setAttribute("intento", intento);
                String nextJSP = "/index.jsp";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
                dispatcher.forward(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
