package com.game.puzzle.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/twitter")
@RestController
public class ControllerCommon {

    @Autowired
    ServiceCommon service;

    @RequestMapping(value = "/mutual", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JsonNode getStoredGame(@RequestParam(value = "u1") String username1 ,@RequestParam(value = "u2") String username2) {
        try {
            return service.getMutualFriends(username1,username2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



}