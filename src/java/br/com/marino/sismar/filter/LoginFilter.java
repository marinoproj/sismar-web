package br.com.marino.sismar.filter;

import br.com.marino.sismar.controller.UsuariosWebController;
import br.com.marino.sismar.controller.UsuariosWebSessaoController;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public String[] getAuth(String query){
        
        if (query == null || query.isEmpty()){
            return null;
        }
        
        String queryArray[] = query.split("&");
        
        System.out.println(Arrays.toString(queryArray));
        
        for(String q : queryArray){
            
            if (q == null || q.isEmpty()){
                continue;
            }
            
            if (!q.startsWith("auth=")){
                continue;
            }
            
            String auth = q.split("=")[1];            
            
            byte[] decodedBytes = Base64.getDecoder().decode(auth);
            String decodedString = new String(decodedBytes);

            if (!decodedString.contains(":")) {
                continue;
            }

            return decodedString.split(":");
            
        }
        
        return null;
        
    }
    
    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        UsuariosWeb user = null;
        HttpSession session = ((HttpServletRequest) request).getSession(true);

        if (session != null) {
            user = (UsuariosWeb) session.getAttribute("userLoggedIn");
        }

        if (user == null) {

            String []auth = getAuth(((HttpServletRequest) request).getQueryString());

            if (auth != null) {

                String loginAuth = auth[0];
                String passwordAuth = auth[1];

                EntityManagerFactory factory = null;
                EntityManager manager = null;

                String contextPath = ((HttpServletRequest) request)
                        .getContextPath();

                try {

                    factory = Persistence.createEntityManagerFactory("sismarPU");
                    manager = factory.createEntityManager();

                    user = UsuariosWebController
                            .getUserByLoginAndPassword(manager, loginAuth, Util.convertStringToMd5(passwordAuth));

                    if (user == null) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/");
                        return;
                    }

                    UsuariosWebSessao sessionOpen = user.getOpenSession();

                    if (sessionOpen == null) {

                        String ip = Util.getIpAddr(((HttpServletRequest) request));

                        UsuariosWebSessao sessionNew = new UsuariosWebSessao();
                        sessionNew.setCodUsuario(user);
                        sessionNew.setInicio(new Date());
                        sessionNew.setIp(ip);

                        manager.getTransaction().begin();
                        UsuariosWebSessaoController.insert(manager, sessionNew);
                        manager.getTransaction().commit();

                        user.addSession(sessionNew);

                    }

                    session.setAttribute("userLoggedIn", user);

                    if (Util.containsFeature(user, "ais_realtime")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/ais/realtime.xhtml?faces-redirect=true");
                    } else {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/home.xhtml?faces-redirect=true");
                    }

                } catch (Exception ex) {
                    manager.getTransaction().rollback();
                    ((HttpServletResponse) response).sendRedirect(contextPath
                            + "/faces/");

                } finally {
                    if (manager != null) {
                        manager.close();
                    }
                    if (factory != null) {
                        factory.close();
                    }

                }

            } else {
                chain.doFilter(request, response);

            }

        } else {
            String contextPath = ((HttpServletRequest) request)
                    .getContextPath();
            ((HttpServletResponse) response).sendRedirect(contextPath
                    + "/faces/admin/ais/realtime.xhtml?faces-redirect=true");
        }

    }

    @Override
    public void destroy() {
    }

}
