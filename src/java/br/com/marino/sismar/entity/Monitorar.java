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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Leonardo
 */
@Entity
@Table(name = "monitorar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Monitorar.findAll", query = "SELECT m FROM Monitorar m")
    , @NamedQuery(name = "Monitorar.findByCodMonitorar", query = "SELECT m FROM Monitorar m WHERE m.codMonitorar = :codMonitorar")
    , @NamedQuery(name = "Monitorar.findByVariavel", query = "SELECT m FROM Monitorar m WHERE m.variavel = :variavel")})
public class Monitorar implements Serializable {
    
    public static final int ARRIVAL_DATE = 1, DEPARTURE_DATE = 2, PERMANENCE_TIME = 3, NAVIGATION_SPEED = 4;
    
    @JoinTable(name = "monitorar_poin", joinColumns = {
        @JoinColumn(name = "codMonitorar", referencedColumnName = "codMonitorar")}, inverseJoinColumns = {
        @JoinColumn(name = "codPoin", referencedColumnName = "codPoin")})
    @ManyToMany
    private List<Poin> poinList;
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "codMonitorar")
    private Integer codMonitorar;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "variavel")
    private int variavel;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "mensagem")
    private String mensagem;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMonitorar", orphanRemoval = true)
    @OrderBy("ordem ASC")
    private List<MonitorarRegra> monitorarRegraList;
    
    @JoinColumn(name = "codUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private UsuariosWeb codUsuario;
    
    public Monitorar() {
    }
    
    public Monitorar(Integer codMonitorar) {
        this.codMonitorar = codMonitorar;
    }
    
    public Monitorar(Integer codMonitorar, int variavel, String mensagem) {
        this.codMonitorar = codMonitorar;
        this.variavel = variavel;
        this.mensagem = mensagem;
    }
    
    public Integer getCodMonitorar() {
        return codMonitorar;
    }
    
    public void setCodMonitorar(Integer codMonitorar) {
        this.codMonitorar = codMonitorar;
    }
    
    public int getVariavel() {
        return variavel;
    }
    
    public void setVariavel(int variavel) {
        this.variavel = variavel;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    @XmlTransient
    public List<MonitorarRegra> getMonitorarRegraList() {
        return monitorarRegraList;
    }
    
    public void setMonitorarRegraList(List<MonitorarRegra> monitorarRegraList) {
        this.monitorarRegraList = monitorarRegraList;
    }
    
    public UsuariosWeb getCodUsuario() {
        return codUsuario;
    }
    
    public void setCodUsuario(UsuariosWeb codUsuario) {
        this.codUsuario = codUsuario;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMonitorar != null ? codMonitorar.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Monitorar)) {
            return false;
        }
        Monitorar other = (Monitorar) object;
        if ((this.codMonitorar == null && other.codMonitorar != null) || (this.codMonitorar != null && !this.codMonitorar.equals(other.codMonitorar))) {
            return false;
        }
        return true;
    }
    
    public String getTypeMonitor() {
        
        switch (variavel) {
            case ARRIVAL_DATE:
                return "Chegada de Embarcação";
            case DEPARTURE_DATE:
                return "Saída de Embarcação";
            case PERMANENCE_TIME:
                return "Tempo de Permanência (h)";
            case NAVIGATION_SPEED:
                return "Velocidade de Navegação (kn)";
            default:
                return "";
        }
        
    }
    
    public void removeRule(int index) {
        monitorarRegraList.remove(index);        
        int inputLength = monitorarRegraList.size();
        MonitorarRegra temp;
        boolean is_sorted;
        
        for (int i = 0; i < inputLength; i++) {            
            is_sorted = true;            
            for (int j = 1; j < (inputLength - i); j++) {                
                if (monitorarRegraList.get(j - 1).getOrdem() > monitorarRegraList.get(j).getOrdem()) {
                    temp = monitorarRegraList.get(j - 1);
                    monitorarRegraList.set(j - 1, monitorarRegraList.get(j));
                    monitorarRegraList.set(j, temp);
                    is_sorted = false;
                }                
            }            
            if (is_sorted) {
                break;
            }            
        }        
        for (int i = 0; i < inputLength; i++) {           
            monitorarRegraList.get(i).setOrdem(i + 1);            
        }        
    }
    
    public void addRule(MonitorarRegra rule) {
        if (monitorarRegraList == null) {
            monitorarRegraList = new ArrayList<>();
        }        
        rule.setOrdem(monitorarRegraList.size() + 1);
        monitorarRegraList.add(rule);
    }
    
    public void removeAllRules() {
        if (monitorarRegraList != null && !monitorarRegraList.isEmpty()) {
            monitorarRegraList.clear();
        }
    }
    
    public boolean variableIsArrivalDate() {
        return variavel == Monitorar.ARRIVAL_DATE;
    }

    public boolean variableIsExitDate() {
        return variavel == Monitorar.DEPARTURE_DATE;
    }

    public boolean variableIsPermanenceTime() {
        return variavel == Monitorar.PERMANENCE_TIME;
    }

    public boolean variableIsNavigationSpeed() {
        return variavel == Monitorar.NAVIGATION_SPEED;
    }
    
    @XmlTransient
    public List<Poin> getPoinList() {
        return poinList;
    }
    
    public void setPoinList(List<Poin> poinList) {
        this.poinList = poinList;
    }
    
}
