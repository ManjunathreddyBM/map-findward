package com.bbmp.map.Controller;

import com.bbmp.map.service.GeoLocationFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class MapController {

    private final GeoLocationFinder geoLocationFinder;

    public MapController(GeoLocationFinder geoLocationFinder) {
        this.geoLocationFinder = geoLocationFinder;
    }

    @GetMapping
    public String showMap() {
        return "index";
    }
    @GetMapping(value = "/map")
    public String showMap1() {
        return "map";
    }

    @PostMapping("/find-ward")
    @ResponseBody
    public HashMap<String , String> findWardByLatLng(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
          ) {

        // Get the ward name based on latitude and longitude
        return geoLocationFinder.findWard(latitude, longitude);
/*
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("wardName", wardName);*/

        // Return the same Thymeleaf view with the ward details
        //return ResponseEntity.ok(wardName);
       // return "map"; // assuming your HTML file is named map.html
    }

}
