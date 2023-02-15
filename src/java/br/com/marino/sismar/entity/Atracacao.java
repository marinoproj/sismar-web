package br.com.marino.sismar.entity;

import br.com.marino.sismar.enums.LadoAtracacaoEnum;
import br.com.marino.sismar.enums.StatusOperacaoEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "atracacoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atracacao.findAll", query = "SELECT o FROM Atracacao o")
    , @NamedQuery(name = "Atracacao.findByCodAtracacao", query = "SELECT o FROM Atracacao o WHERE o.codAtracacao = :codAtracacao")
    , @NamedQuery(name = "Atracacao.findByDataInicio", query = "SELECT o FROM Atracacao o WHERE o.dataInicio = :dataInicio")
    , @NamedQuery(name = "Atracacao.findByDataDocagem", query = "SELECT o FROM Atracacao o WHERE o.dataDocagem = :dataDocagem")
    , @NamedQuery(name = "Atracacao.findByDataPartida", query = "SELECT o FROM Atracacao o WHERE o.dataPartida = :dataPartida")
    , @NamedQuery(name = "Atracacao.findByVelocidadeMaximaDireita", query = "SELECT o FROM Atracacao o WHERE o.velocidadeMaximaDireita = :velocidadeMaximaDireita")
    , @NamedQuery(name = "Atracacao.findByVelocidadeMaximaEsquerda", query = "SELECT o FROM Atracacao o WHERE o.velocidadeMaximaEsquerda = :velocidadeMaximaEsquerda")
    , @NamedQuery(name = "Atracacao.findByDistanciaVelMaxDireita", query = "SELECT o FROM Atracacao o WHERE o.distanciaVelMaxDireita = :distanciaVelMaxDireita")
    , @NamedQuery(name = "Atracacao.findByDistanciaVelMaxEsquerda", query = "SELECT o FROM Atracacao o WHERE o.distanciaVelMaxEsquerda = :distanciaVelMaxEsquerda")
    , @NamedQuery(name = "Atracacao.findByAnguloMaximo", query = "SELECT o FROM Atracacao o WHERE o.anguloMaximo = :anguloMaximo")
    , @NamedQuery(name = "Atracacao.findByVelocidadeToqueDireita", query = "SELECT o FROM Atracacao o WHERE o.velocidadeToqueDireita = :velocidadeToqueDireita")
    , @NamedQuery(name = "Atracacao.findByVelocidadeToqueEsquerda", query = "SELECT o FROM Atracacao o WHERE o.velocidadeToqueEsquerda = :velocidadeToqueEsquerda")
    , @NamedQuery(name = "Atracacao.findByAnguloToque", query = "SELECT o FROM Atracacao o WHERE o.anguloToque = :anguloToque")
    , @NamedQuery(name = "Atracacao.findByMaiorPressaoDefensaDireita", query = "SELECT o FROM Atracacao o WHERE o.maiorPressaoDefensaDireita = :maiorPressaoDefensaDireita")
    , @NamedQuery(name = "Atracacao.findByMaiorPressaoDefensaEsquerda", query = "SELECT o FROM Atracacao o WHERE o.maiorPressaoDefensaEsquerda = :maiorPressaoDefensaEsquerda")
    , @NamedQuery(name = "Atracacao.findByStatusOperacao", query = "SELECT o FROM Atracacao o WHERE o.statusOperacao = :statusOperacao")
    , @NamedQuery(name = "Atracacao.findByLado", query = "SELECT o FROM Atracacao o WHERE o.lado = :lado")
    , @NamedQuery(name = "Atracacao.findByDataEncerrado", query = "SELECT o FROM Atracacao o WHERE o.dataEncerrado = :dataEncerrado")})
public class Atracacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codAtracacao")
    private Integer codAtracacao;

    @Basic(optional = false)
    @Column(name = "dataInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Column(name = "dataDocagem")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDocagem;

    @Column(name = "dataPartida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPartida;

    @Column(name = "velocidadeMaximaDireita")
    private Double velocidadeMaximaDireita;

    @Column(name = "velocidadeMaximaEsquerda")
    private Double velocidadeMaximaEsquerda;

    @Column(name = "distanciaVelMaxDireita")
    private Double distanciaVelMaxDireita;

    @Column(name = "distanciaVelMaxEsquerda")
    private Double distanciaVelMaxEsquerda;

    @Column(name = "anguloMaximo")
    private Double anguloMaximo;

    @Column(name = "velocidadeToqueDireita")
    private Double velocidadeToqueDireita;

    @Column(name = "velocidadeToqueEsquerda")
    private Double velocidadeToqueEsquerda;

    @Column(name = "anguloToque")
    private Double anguloToque;

    @Column(name = "maiorPressaoDefensaDireita")
    private Double maiorPressaoDefensaDireita;

    @Column(name = "maiorPressaoDefensaEsquerda")
    private Double maiorPressaoDefensaEsquerda;

    @Basic(optional = false)
    @Column(name = "statusOperacao")
    @Enumerated(EnumType.ORDINAL)
    private StatusOperacaoEnum statusOperacao;

    @Column(name = "lado")
    @Enumerated(EnumType.ORDINAL)
    private LadoAtracacaoEnum lado;

    @Column(name = "dataEncerrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEncerrado;    

    @JoinColumn(name = "codNavio", referencedColumnName = "codNavio")
    @ManyToOne(optional = false)
    private Navio codNavio;    
    
    @JoinColumn(name = "codCliente", referencedColumnName = "cod")
    @ManyToOne(optional = true)
    private Clientes codCliente;
    
    @JoinColumn(name = "codBerco", referencedColumnName = "codBerco")
    @ManyToOne(optional = true)
    private Berco codBerco;
    
    @JoinColumn(name = "codPratico", referencedColumnName = "codPratico")
    @ManyToOne(optional = true)
    private Pratico codPratico;
    
    @JoinColumn(name = "codGiaont", referencedColumnName = "codGiaont")
    @ManyToOne(optional = true)
    private Giaont codGiaont;

    public Atracacao() {
    }

    public Date getDataDocagem() {
        return dataDocagem;
    }

    public void setDataDocagem(Date dataDocagem) {
        this.dataDocagem = dataDocagem;
    }

    public Date getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(Date dataPartida) {
        this.dataPartida = dataPartida;
    }

    public Double getVelocidadeMaximaDireita() {
        return velocidadeMaximaDireita;
    }

    public void setVelocidadeMaximaDireita(Double velocidadeMaximaDireita) {
        this.velocidadeMaximaDireita = velocidadeMaximaDireita;
    }

    public Double getVelocidadeMaximaEsquerda() {
        return velocidadeMaximaEsquerda;
    }

    public void setVelocidadeMaximaEsquerda(Double velocidadeMaximaEsquerda) {
        this.velocidadeMaximaEsquerda = velocidadeMaximaEsquerda;
    }

    public Double getDistanciaVelMaxDireita() {
        return distanciaVelMaxDireita;
    }

    public void setDistanciaVelMaxDireita(Double distanciaVelMaxDireita) {
        this.distanciaVelMaxDireita = distanciaVelMaxDireita;
    }

    public Double getDistanciaVelMaxEsquerda() {
        return distanciaVelMaxEsquerda;
    }

    public void setDistanciaVelMaxEsquerda(Double distanciaVelMaxEsquerda) {
        this.distanciaVelMaxEsquerda = distanciaVelMaxEsquerda;
    }

    public Double getAnguloMaximo() {
        return anguloMaximo;
    }

    public void setAnguloMaximo(Double anguloMaximo) {
        this.anguloMaximo = anguloMaximo;
    }

    public Double getVelocidadeToqueDireita() {
        return velocidadeToqueDireita;
    }

    public void setVelocidadeToqueDireita(Double velocidadeToqueDireita) {
        this.velocidadeToqueDireita = velocidadeToqueDireita;
    }

    public Double getVelocidadeToqueEsquerda() {
        return velocidadeToqueEsquerda;
    }

    public void setVelocidadeToqueEsquerda(Double velocidadeToqueEsquerda) {
        this.velocidadeToqueEsquerda = velocidadeToqueEsquerda;
    }

    public Double getAnguloToque() {
        return anguloToque;
    }

    public void setAnguloToque(Double anguloToque) {
        this.anguloToque = anguloToque;
    }

    public Double getMaiorPressaoDefensaDireita() {
        return maiorPressaoDefensaDireita;
    }

    public void setMaiorPressaoDefensaDireita(Double maiorPressaoDefensaDireita) {
        this.maiorPressaoDefensaDireita = maiorPressaoDefensaDireita;
    }

    public Double getMaiorPressaoDefensaEsquerda() {
        return maiorPressaoDefensaEsquerda;
    }

    public void setMaiorPressaoDefensaEsquerda(Double maiorPressaoDefensaEsquerda) {
        this.maiorPressaoDefensaEsquerda = maiorPressaoDefensaEsquerda;
    }

    public LadoAtracacaoEnum getLado() {
        return lado;
    }

    public void setLado(LadoAtracacaoEnum lado) {
        this.lado = lado;
    }

    public Date getDataEncerrado() {
        return dataEncerrado;
    }

    public void setDataEncerrado(Date dataEncerrado) {
        this.dataEncerrado = dataEncerrado;
    }    

    public Navio getCodNavio() {
        return codNavio;
    }

    public void setCodNavio(Navio codNavio) {
        this.codNavio = codNavio;
    }

    public Integer getCodAtracacao() {
        return codAtracacao;
    }

    public void setCodAtracacao(Integer codAtracacao) {
        this.codAtracacao = codAtracacao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }    

    public StatusOperacaoEnum getStatusOperacao() {
        return statusOperacao;
    }

    public void setStatusOperacao(StatusOperacaoEnum statusOperacao) {
        this.statusOperacao = statusOperacao;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public Berco getCodBerco() {
        return codBerco;
    }

    public void setCodBerco(Berco codBerco) {
        this.codBerco = codBerco;
    }

    public Pratico getCodPratico() {
        return codPratico;
    }

    public void setCodPratico(Pratico codPratico) {
        this.codPratico = codPratico;
    }

    public Giaont getCodGiaont() {
        return codGiaont;
    }

    public void setCodGiaont(Giaont codGiaont) {
        this.codGiaont = codGiaont;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAtracacao != null ? codAtracacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Atracacao)) {
            return false;
        }
        Atracacao other = (Atracacao) object;
        return !((this.codAtracacao == null && other.codAtracacao != null)
                || (this.codAtracacao != null && !this.codAtracacao.equals(other.codAtracacao)));
    }
    
}