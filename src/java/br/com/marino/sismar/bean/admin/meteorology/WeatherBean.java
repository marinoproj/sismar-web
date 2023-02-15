package br.com.marino.sismar.bean.admin.meteorology;

import br.com.marino.sismar.entity.City;
import br.com.marino.sismar.entity.State;
import br.com.marino.sismar.entity.Weather;
import br.com.marino.sismar.util.Util;
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.http.JsonNode;
import io.github.openunirest.http.Unirest;
import java.io.Serializable;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

@ManagedBean(name = "WeatherBean")
@ViewScoped
public class WeatherBean implements Serializable {

    private String ip;
    private List<State> states;
    private List<City> cities;
    private State stateSelected;
    private City citySelected;
    private Weather weather;

    @PostConstruct
    public void init() {

        cities = new ArrayList<>();
        states = new ArrayList<>();

        HttpServletRequest request = (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext().getRequest();

        ip = Util.getIpAddr(request);
        readStates();
        readLocationByIP();

        try {
            reloadWeather();
        } catch (Exception ex) {
            weather = null;
        }

    }

    public String getFiltroSelected() {
        String filtro = "";
        if (stateSelected == null || citySelected == null) {
            filtro += "Não informado";
        } else {
            filtro += citySelected.getName() + " - " + stateSelected.getAbv();
        }
        return filtro;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public City getCitySelected() {
        return citySelected;
    }

    public void setCitySelected(City citySelected) {
        this.citySelected = citySelected;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getStateSelected() {
        return stateSelected;
    }

    public void setStateSelected(State stateSelected) {
        this.stateSelected = stateSelected;
    }

    private State getStateByRegion(String text) {
        State obj = null;
        for (State state : states) {
            if (state.getAbv().equalsIgnoreCase(text)) {
                obj = state;
                break;
            }
        }
        if (obj == null) {
            return getStateByRegion("SP");
        } else {
            return obj;
        }
    }

    private void setLocationUserDefault() {

        stateSelected = getStateByRegion("SP");

        try {
            readCities();
        } catch (Exception ex) {
            cities.clear();
            stateSelected = null;
            citySelected = null;
            return;
        }

        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);

        for (City city : cities) {
            if (instance.compare(city.getName(), "São Sebastião") == 0) {
                citySelected = city;
                break;
            }
        }

    }

    private void readLocationByIP() {

        stateSelected = null;
        citySelected = null;
        cities.clear();

        JSONObject obj;

        try {

            URL url = new URL("http://ip-api.com/json/" + ip);
            JSONTokener tokener = new JSONTokener(url.openStream());
            obj = new JSONObject(tokener);

        } catch (Exception ex) {
            setLocationUserDefault();
            return;
        }

        if (!obj.getString("status").equalsIgnoreCase("success")) {
            setLocationUserDefault();
            return;
        }

        for (State state : states) {
            if (state.getAbv().equalsIgnoreCase(obj.getString("region"))) {
                stateSelected = state;
                break;
            }
        }

        if (stateSelected == null) {
            setLocationUserDefault();
            return;
        }

        try {
            readCities();
        } catch (Exception ex) {
            cities.clear();
            stateSelected = null;
            citySelected = null;
            return;
        }

        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);

        for (City city : cities) {
            if (instance.compare(city.getName(), obj.getString("city")) == 0) {
                citySelected = city;
                break;
            }
        }

        if (citySelected == null) {
            setLocationUserDefault();
        }

    }

    private void readStates() {
        states.add(new State("AC", "Acre"));
        states.add(new State("AL", "Alagoas"));
        states.add(new State("AP", "Amapá"));
        states.add(new State("AM", "Amazonas"));
        states.add(new State("BA", "Bahia"));
        states.add(new State("CE", "Ceará"));
        states.add(new State("DF", "Distrito Federal"));
        states.add(new State("ES", "Espírito Santo"));
        states.add(new State("GO", "Goiás"));
        states.add(new State("MA", "Maranhão"));
        states.add(new State("MT", "Mato Grosso"));
        states.add(new State("MS", "Mato Grosso do Sul"));
        states.add(new State("MG", "Minas Gerais"));
        states.add(new State("PA", "Pará"));
        states.add(new State("PB", "Paraíba"));
        states.add(new State("PR", "Paraná"));
        states.add(new State("PE", "Pernambuco"));
        states.add(new State("PI", "Piauí"));
        states.add(new State("RJ", "Rio de Janeiro"));
        states.add(new State("RN", "Rio Grande do Norte"));
        states.add(new State("RS", "Rio Grande do Sul"));
        states.add(new State("RO", "Rondônia"));
        states.add(new State("RR", "Roraima"));
        states.add(new State("SC", "Santa Catarina"));
        states.add(new State("SP", "São Paulo"));
        states.add(new State("SE", "Sergipe"));
        states.add(new State("TO", "Tocantins"));
    }

    private void readCities() throws Exception {
        
        HttpResponse<JsonNode> request = Unirest.get("http://apiadvisor.climatempo.com.br/api/v1/locale/city?"
                + "state=" + stateSelected.getAbv() + "&token=" + Util.TOKEN).asJson();
        
        JSONArray array = request.getBody().getArray();

        cities.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            cities.add(new City(stateSelected,
                    obj.getInt("id"),
                    obj.getString("name")));
        }

    }

    public void updateCities() {

        if (stateSelected == null) {
            cities.clear();
            citySelected = null;
            return;
        }

        try {
            readCities();
            citySelected = null;
        } catch (Exception ex) {
            cities.clear();
            citySelected = null;
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Não foi possível buscar as cidades!"));
        }

    }

    @PreDestroy
    public void destroy() {

    }

    private void reloadWeather() throws Exception {

        if (stateSelected == null || citySelected == null) {
            return;
        }        
        
        HttpResponse<JsonNode> request = Unirest.get("http://apiadvisor.climatempo.com.br/api/v1/weather/locale/"
                + citySelected.getId() + "/current?token=" + Util.TOKEN).asJson();
        
        JSONObject obj = request.getBody().getObject();

        if (obj.has("error") && obj.getBoolean("error")) {
            throw new Exception(obj.getString("detail"));
        }

        obj = obj.getJSONObject("data");        

        Weather weatherNew = new Weather();
        weatherNew.setCity(citySelected);
        weatherNew.setTemperature(obj.getDouble("temperature"));
        weatherNew.setWindDirection(obj.getString("wind_direction"));
        weatherNew.setWindVelocity(obj.getDouble("wind_velocity"));
        weatherNew.setHumidity(obj.getDouble("humidity"));
        weatherNew.setCondition(obj.getString("condition"));
        weatherNew.setPressure(obj.getDouble("pressure"));
        weatherNew.setIcon(obj.getString("icon"));
        weatherNew.setSensation(obj.getDouble("sensation"));
        weatherNew.setDate(Util.stringToDate(obj.getString("date"), "yyyy-MM-dd HH:mm:ss"));

        weather = weatherNew;

    }

    public boolean isDisplayWeather() {
        return weather != null;
    }

    public void displayWeather() {

        if (stateSelected == null || citySelected == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Preencha o formulário corretamente. Todos os campos são obrigatórios"));
            if (weather != null) {
                stateSelected = weather.getCity().getState();
                citySelected = weather.getCity();
            }
            return;
        }

        try {
            reloadWeather();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atenção",
                            "Não foi possível buscar as informações climáticas da região informada!"));
            if (weather != null) {
                stateSelected = weather.getCity().getState();
                citySelected = weather.getCity();
            }
        }

    }

    public String getTemperature() {
        if (weather == null) {
            return "";
        }
        return getValueFormatted(weather.getTemperature()) + "º";
    }

    public String getCondition() {
        if (weather == null) {
            return "";
        }
        return weather.getCondition();
    }

    public String getSensation() {
        if (weather == null) {
            return "";
        }
        return getValueFormatted(weather.getSensation()) + " º";
    }

    public String getHumidity() {
        if (weather == null) {
            return "";
        }
        return getValueFormatted(weather.getHumidity()) + " %";
    }

    public String getPressure() {
        if (weather == null) {
            return "";
        }
        return getValueFormatted(weather.getPressure()) + " hPA";
    }

    public String getWind() {
        if (weather == null) {
            return "";
        }
        return getValueFormatted(weather.getWindVelocity()) + " Km/h";
    }

    public String getIcon() {
        if (weather == null) {
            return "";
        }
        return "img/climatempo/" + weather.getIcon() + ".jpg";
    }

    public String getDateLastUpdate() {
        if (weather == null) {
            return "";
        }
        return "Atualizado " + Util.getStringDateLastUpdate(weather.getDate(), new Date());
    }

    public String getValueFormatted(Double value) {
        if (value == null) {
            return "";
        }
        return Util.converterValueWithoutDecimals(value);
    }

}
