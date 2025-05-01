package io.controller;


import io.businessLogic.PlayerManager;
import io.datarecords.Player;
import org.springframework.web.bind.annotation.*;

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
}
