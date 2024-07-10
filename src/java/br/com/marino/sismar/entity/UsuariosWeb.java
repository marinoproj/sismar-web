package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "usuarios_web")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosWeb.findAll", query = "SELECT u FROM UsuariosWeb u")
    , @NamedQuery(name = "UsuariosWeb.findByIdUsuario", query = "SELECT u FROM UsuariosWeb u WHERE u.idUsuario = :idUsuario")
    , @NamedQuery(name = "UsuariosWeb.findByNome", query = "SELECT u FROM UsuariosWeb u WHERE u.nome = :nome")
    , @NamedQuery(name = "UsuariosWeb.findByLogin", query = "SELECT u FROM UsuariosWeb u WHERE u.login = :login")
    , @NamedQuery(name = "UsuariosWeb.findBySenha", query = "SELECT u FROM UsuariosWeb u WHERE u.senha = :senha")
    , @NamedQuery(name = "UsuariosWeb.findByTelasHabilitadas", query = "SELECT u FROM UsuariosWeb u WHERE u.telasHabilitadas = :telasHabilitadas")
    , @NamedQuery(name = "UsuariosWeb.findByLoginAndSenha", query = "SELECT u FROM UsuariosWeb u WHERE u.login = :login AND u.senha = :senha")
    //, @NamedQuery(name = "UsuariosWeb.findByAuthenticationToken", query = "SELECT u FROM UsuariosWeb u WHERE u.authenticationToken = :authenticationToken")
    , @NamedQuery(name = "UsuariosWeb.findByToken", query = "SELECT u FROM UsuariosWeb u WHERE u.token = :token")})
public class UsuariosWeb implements Serializable, Cloneable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<Operacao> operacaoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<Etapas> etapasList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<Eventos> eventosList;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 200)
    @Column(name = "email")
    private String email;
    
    @Size(max = 200)
    @Column(name = "telefone")
    private String telefone;
    
    @Size(max = 200)
    @Column(name = "cpfcnpj")
    private String cpfcnpj;
    
    @Size(max = 200)
    @Column(name = "empresa")
    private String empresa;
    
    @Size(max = 200)
    @Column(name = "setor")
    private String setor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    @OrderBy("inicio ASC")
    private List<UsuariosWebSessao> usuariosWebSessaoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<Monitorar> monitorarList;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;

    @Basic(optional = false)
    @Column(name = "login")
    private String login;

    @Basic(optional = false)
    @Column(name = "senha")
    private String senha;

    @Basic(optional = false)
    @Column(name = "telasHabilitadas")
    private String telasHabilitadas;

    @Basic(optional = true)
    @Column(name = "token")
    private String token;
    
    @Basic(optional = true)
    @Column(name = "tempoAtualizacao")
    private Integer tempoAtualizacao; // em segundos (tempo de atualização)
    
    @Basic(optional = true)
    @Column(name = "tempoInatividade")
    private Integer tempoInatividade; // em minutos (tempo de inatividade)
    
    @Basic(optional = true)
    @Column(name = "tempoMaxPlayback")
    private Integer tempoMaxPlayback; // em minutos (tempo maximo de playback)
    
    @Basic(optional = true)
    @Column(name = "timeZone")
    private String timeZone = "America/Sao_Paulo";
    
    @Basic(optional = true)
    @Column(name = "master")
    private Boolean master;
    
    @Basic(optional = true)
    @Column(name = "paginaInicial")
    private String paginaInicial;
    
    @Basic(optional = true)
    @Column(name = "ativo")
    private Boolean ativo;
    
    public UsuariosWeb() {
    }

    public UsuariosWeb(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public UsuariosWeb(Integer idUsuario, String nome, String login, String senha, String telasHabilitadas) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.telasHabilitadas = telasHabilitadas;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelasHabilitadas() {
        return telasHabilitadas;
    }

    public void setTelasHabilitadas(String telasHabilitadas) {
        this.telasHabilitadas = telasHabilitadas;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTempoAtualizacao() {
        return tempoAtualizacao;
    }

    public void setTempoAtualizacao(Integer tempoAtualizacao) {
        this.tempoAtualizacao = tempoAtualizacao;
    }

    public Integer getTempoInatividade() {
        return tempoInatividade;
    }

    public void setTempoInatividade(Integer tempoInatividade) {
        this.tempoInatividade = tempoInatividade;
    }

    public Integer getTempoMaxPlayback() {
        return tempoMaxPlayback;
    }

    public void setTempoMaxPlayback(Integer tempoMaxPlayback) {
        this.tempoMaxPlayback = tempoMaxPlayback;
    }

    public List<Eventos> getEventosList() {
        return eventosList;
    }

    public void setEventosList(List<Eventos> eventosList) {
        this.eventosList = eventosList;
    }    

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public UsuariosWeb clone() throws CloneNotSupportedException {
        return (UsuariosWeb) super.clone();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UsuariosWeb)) {
            return false;
        }
        UsuariosWeb other = (UsuariosWeb) object;
        return !((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario)));
    }

    @Override
    public String toString() {
        return "UsuariosWeb{" + "idUsuario=" + idUsuario + ", nome=" + nome
                + ", login=" + login + ", senha=" + senha + ", telasHabilitadas=" + telasHabilitadas + '}';
    }

    @XmlTransient
    public List<Monitorar> getMonitorarList() {
        return monitorarList;
    }

    public void setMonitorarList(List<Monitorar> monitorarList) {
        this.monitorarList = monitorarList;
    }

    @XmlTransient
    public List<UsuariosWebSessao> getUsuariosWebSessaoList() {
        return usuariosWebSessaoList;
    }

    public void setUsuariosWebSessaoList(List<UsuariosWebSessao> usuariosWebSessaoList) {
        this.usuariosWebSessaoList = usuariosWebSessaoList;
    }

    public void addSession(UsuariosWebSessao sessao) {
        if (getUsuariosWebSessaoList() == null) {
            usuariosWebSessaoList = new ArrayList<>();
        }
        usuariosWebSessaoList.add(sessao);
    }

    public UsuariosWebSessao getOpenSession() {
        try {
            if (getUsuariosWebSessaoList() == null) {
                return null;
            }
            if (getUsuariosWebSessaoList().isEmpty()) {
                return null;
            }
            for (UsuariosWebSessao session : usuariosWebSessaoList) {
                if (session.getTermino() == null) {
                    return session;
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public UsuariosWebSessao getLastSession() {
        if (getUsuariosWebSessaoList() == null) {
            return null;
        }
        if (usuariosWebSessaoList.isEmpty()) {
            return null;
        }
        return usuariosWebSessaoList.get(usuariosWebSessaoList.size() - 1);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @XmlTransient
    public List<Operacao> getOperacaoList() {
        return operacaoList;
    }

    public void setOperacaoList(List<Operacao> operacaoList) {
        this.operacaoList = operacaoList;
    }

    @XmlTransient
    public List<Etapas> getEtapasList() {
        return etapasList;
    }

    public void setEtapasList(List<Etapas> etapasList) {
        this.etapasList = etapasList;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getPaginaInicial() {
        return paginaInicial;
    }

    public void setPaginaInicial(String paginaInicial) {
        this.paginaInicial = paginaInicial;
    }   

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }    
    
}
