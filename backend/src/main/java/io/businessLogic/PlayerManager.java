package io.businessLogic;

import io.datarecords.Player;
import io.dataretreive.Dataretreiver;
import org.springframework.stereotype.Service;

@Service
public class PlayerManager {
    private Dataretreiver dataretreiver;

    public PlayerManager(Dataretreiver dataretreiver) {
        this.dataretreiver = dataretreiver;
    }
    public Player getPlayer(String username) {
        return dataretreiver.getPlayer(username);
    }

}
