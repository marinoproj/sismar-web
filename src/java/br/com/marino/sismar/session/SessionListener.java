package br.com.marino.sismar.session;

import br.com.marino.sismar.controller.SessaoPaginaController;
import br.com.marino.sismar.controller.UsuariosWebSessaoController;
import br.com.marino.sismar.entity.SessaoPagina;
import br.com.marino.sismar.entity.UsuariosWeb;
import br.com.marino.sismar.entity.UsuariosWebSessao;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {

        System.out.println("Sessão criada " + event.getSession().getId());

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        System.out.println("caiu aqui para encerrar");
        
        UsuariosWeb user = (UsuariosWeb) event.getSession().getAttribute("userLoggedIn");
        
        UsuariosWebSessao session = user.getOpenSession();

        if (session != null) {

            session.setTermino(new Date(event.getSession().getLastAccessedTime()));

            EntityManagerFactory factory = null;
            EntityManager manager = null;

            try {

                factory = Persistence.createEntityManagerFactory("sismarPU");
                manager = factory.createEntityManager();

                manager.getTransaction().begin();
                
                UsuariosWebSessaoController.edit(manager, session);
                
                SessaoPagina old = SessaoPaginaController.getOpenSessaoPaginaByCodUsuariosWebSessao(manager, session.getCodUsuariosWebSessao());
                if (old != null) {
                    old.setSaida(session.getTermino());
                    SessaoPaginaController.edit(manager, old);
                }                
                
                manager.getTransaction().commit();

            } catch (Exception ex) {
                manager.getTransaction().rollback();

            } finally {
                if (manager != null) {
                    manager.close();
                }
                if (factory != null) {
                    factory.close();
                }

            }

        }        

        String ultimoAcesso = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date(event.getSession().getLastAccessedTime()));
        System.out.println("Sessão expirada " + event.getSession().getId() + ". Ultimo Acesso = " + ultimoAcesso);

    }

}
