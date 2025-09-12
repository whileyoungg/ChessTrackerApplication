package io.controller;


import io.businessLogic.PlayerManager;
import io.datarecords.Match;
import io.datarecords.Player;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:5173")  // Allow Svelte dev server to access
public class PlayerController {

    private final PlayerManager playerManager;

    public PlayerController(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @GetMapping("/{username}")
    public Player getPlayer(@PathVariable String username) {
        return playerManager.getPlayer(username);
    }
    @GetMapping("/{username}/rapid")
    public List<Match> getRapidMatches(@PathVariable String username) {
        return playerManager.getRapidMatches(username);
    }
    @GetMapping("/{username}/blitz")
    public List<Match> getBlitzMatches(@PathVariable String username) {
        return playerManager.getBlitzMatches(username);
    }
    @GetMapping("/{username}/bullet")
    public List<Match> getBulletMatches(@PathVariable String username) {
        return playerManager.getBulletMatches(username);
    }

}
