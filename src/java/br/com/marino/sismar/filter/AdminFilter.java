package br.com.marino.sismar.filter;

import br.com.marino.sismar.controller.SessaoPaginaController;
import br.com.marino.sismar.entity.SessaoPagina;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.session.SessionContext;
import br.com.marino.sismar.util.Util;
import java.io.IOException;
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

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletResponse t = ((HttpServletResponse) response);

        t.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        t.addHeader("Cache-Control", "post-check=0, pre-check=0");
        t.setHeader("Pragma", "no-cache");

        String page = ((HttpServletRequest) request).getRequestURI();

        UsuariosWeb user = null;
        HttpSession session = ((HttpServletRequest) request).getSession(true);

        if (session != null) {
            user = (UsuariosWeb) session.getAttribute("userLoggedIn");
        }

        String contextPath = ((HttpServletRequest) request).getContextPath();

        if (user == null) {
            ((HttpServletResponse) response).sendRedirect(contextPath
                    + "/faces/index.xhtml");

        } else if (user.getOpenSession() == null) {
            SessionContext.getInstance().closeSession();
            ((HttpServletResponse) response).sendRedirect(contextPath
                    + "/faces/index.xhtml");

        } else {

            String method = ((HttpServletRequest) request).getMethod();

            if (method.equalsIgnoreCase("GET")) {

                String url = page;

                String query = ((HttpServletRequest) request).getQueryString();
                if (query != null && !query.isEmpty()) {
                    url += "?" + query;
                }

                EntityManagerFactory factory = null;
                EntityManager manager = null;

                try {

                    factory = Persistence.createEntityManagerFactory("sismarPU");
                    manager = factory.createEntityManager();

                    manager.getTransaction().begin();

                    SessaoPagina sessaoPagina = new SessaoPagina();
                    sessaoPagina.setPagina(url);
                    SessaoPaginaController.insert(manager, user.getIdUsuario(), sessaoPagina);

                    manager.getTransaction().commit();

                } catch (Exception ex) {
                    if (manager != null) {
                        manager.getTransaction().rollback();
                    }

                } finally {
                    if (manager != null) {
                        manager.close();
                    }
                    if (factory != null) {
                        factory.close();
                    }
                }

            }

            switch (page) {

                // ais
                case "/sismar/faces/admin/ais/realtime.xhtml":
                    if (!Util.containsFeature(user, "ais_realtime")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/ais/track.xhtml":
                    if (!Util.containsFeature(user, "ais_track")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/ais/playback.xhtml":
                    if (!Util.containsFeature(user, "ais_playback")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;

                    
                // =============================================================
                // meteorologia
                case "/sismar/faces/admin/meteorology/weather.xhtml":
                    if (!Util.containsFeature(user, "meteorology_climatempo")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/meteorology/windy.xhtml":
                    if (!Util.containsFeature(user, "meteorology_windy")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/meteorology/meteorology.xhtml":
                    if (!Util.containsFeature(user, "meteorology_all")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                    
                    
                // =============================================================
                // eventos
                case "/sismar/faces/admin/events.xhtml":
                    if (!Util.containsFeature(user, "events")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                
                // =============================================================
                // configurações
                case "/sismar/faces/admin/configuration/users.xhtml":
                    if (!Util.containsFeature(user, "config_users")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }

                    break;
                case "/sismar/faces/admin/configuration/adduser.xhtml":
                    if (!Util.containsFeature(user, "config_users")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/configuration/edituser.xhtml":
                    if (!Util.containsFeature(user, "config_users")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/configuration/api.xhtml":
                    if (!Util.containsFeature(user, "config_api")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                
                    
                // =============================================================
                // administração
                case "/sismar/faces/admin/administration/clients.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_clients")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }

                    break;
                case "/sismar/faces/admin/administration/client.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_clients")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/administration/bercos.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_bercos")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/administration/addberco.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_bercos")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/administration/editberco.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_bercos")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;
                case "/sismar/faces/admin/administration/config.xhtml":
                    if ((user.getMaster() == null || !user.getMaster()) || !Util.containsFeature(user, "admin_configgerais")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;                 
                    
                // =============================================================
                // about
                case "/sismar/faces/admin/about.xhtml":
                    if (!Util.containsFeature(user, "about")) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/admin/notavailable.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                    break;

                default:
                    chain.doFilter(request, response);

            }

        }

    }

    @Override
    public void destroy() {
    }

}
