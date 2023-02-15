package br.com.marino.sismar.api;

import br.com.marino.sismar.util.Util;
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.http.JsonNode;
import io.github.openunirest.http.Unirest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/weather")
public class WeatherApi {

    
    @GET
    @Path("/{city}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getWeatherByCity(@PathParam("city") int city) throws Exception {

        HttpResponse<JsonNode> request = Unirest.get("http://apiadvisor.climatempo.com.br/api/v1/weather/locale/"
                + city + "/current?token=" + Util.TOKEN).asJson();
        
        JSONObject obj = request.getBody().getObject();

        if (obj.has("error") && obj.getBoolean("error")) {
            return obj.toString();
        }

        obj = obj.getJSONObject("data");

        return obj.toString();

    }

}
