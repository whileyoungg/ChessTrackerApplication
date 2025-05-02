package io.businessLogic;

import io.datarecords.Match;
import io.datarecords.Player;
import io.dataretreive.Dataretreiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PlayerManagerTest {
    @Mock
    Dataretreiver dataretreiver;

    Player player;
    Match match1;
    Match match2;
    Match match3;
    Match match4;
    Match match5;
    Match match6;
    PlayerManager playerManager;
    ArrayList<Match> matchList;
    @BeforeEach
    void setUp() {
        match1 = new Match("123","W","BLITZ", Instant.now(),"123",true,555,777);
        match2 = new Match("123","W","RAPID",Instant.now(),"123",true,555,777);
        match3 = new Match("123","W","BULLET",Instant.now(),"123",true,555,777);
        match4 = new Match("123","W","BULLET",Instant.now(),"123",true,555,777);
        match5 = new Match("123","W","RAPID",Instant.now(),"123",true,555,777);
        match6 = match1;

        dataretreiver = Mockito.mock(Dataretreiver.class);
        matchList = new ArrayList<>();
        matchList.add(match1);
        matchList.add(match2);
        matchList.add(match3);
        matchList.add(match4);
        matchList.add(match5);
        matchList.add(match6);
        playerManager = new PlayerManager(dataretreiver);
        player = new Player("","ocevn","UA", 999,999,999, 15,matchList);
    }
    @Test
    public void getPlayerTest(){
        when(dataretreiver.getPlayer("ocevn")).thenReturn(player);
        playerManager.getPlayer("ocevn");
        verify(dataretreiver, times(1)).getPlayer("ocevn");
        assertThat(dataretreiver.getPlayer("ocevn").rapidRating()).isEqualTo(999);
    }
    @Test
    public void getBlitzMatchesTest(){
        when(dataretreiver.getMatches("ocevn")).thenReturn(matchList);
        when(dataretreiver.getPlayer("ocevn")).thenReturn(player);
        assertThat(playerManager.getBlitzMatches("ocevn").size()).isEqualTo(2);
        assertThat(playerManager.getBlitzMatches("ocevn")).contains(match1,match6);
    }
    @Test
    public void getRapidMatchesTest(){
        when(dataretreiver.getMatches("ocevn")).thenReturn(matchList);
        when(dataretreiver.getPlayer("ocevn")).thenReturn(player);
        assertThat(playerManager.getRapidMatches("ocevn").size()).isEqualTo(2);
        assertThat(playerManager.getRapidMatches("ocevn")).contains(match2,match5);
    }
    @Test
    public void getBulletMatchesTest(){
        when(dataretreiver.getMatches("ocevn")).thenReturn(matchList);
        when(dataretreiver.getPlayer("ocevn")).thenReturn(player);
        assertThat(playerManager.getBulletMatches("ocevn").size()).isEqualTo(2);
        assertThat(playerManager.getBulletMatches("ocevn")).contains(match3,match4);
    }

}
