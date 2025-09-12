package io.businessLogic;

import io.datarecords.Match;
import io.datarecords.Player;
import io.dataretreive.Dataretreiver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerManager {
    private Dataretreiver dataretreiver;

    public PlayerManager(Dataretreiver dataretreiver) {
        this.dataretreiver = dataretreiver;
    }
    public Player getPlayer(String username) {
        return dataretreiver.getPlayer(username);
    }
    public List<Match> getBlitzMatches(String username) {
        return dataretreiver.getPlayer(username).recentGames()
                .stream()
                .filter((Match m) ->m.mode().equals("BLITZ"))
                .toList();
    }
    public List<Match> getRapidMatches(String username) {
        return dataretreiver.getPlayer(username).recentGames()
                .stream()
                .filter((Match m) ->m.mode().equals("RAPID"))
                .toList();
    }
    public List<Match> getBulletMatches(String username) {
        return dataretreiver.getPlayer(username).recentGames()
                .stream()
                .filter((Match m) ->m.mode().equals("BULLET"))
                .toList();
    }

}
