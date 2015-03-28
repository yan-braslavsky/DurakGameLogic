package com.yan.durak.gamelogic.commands.custom.startgame;


import com.yan.durak.gamelogic.commands.BaseSessionCommand;
import com.yan.durak.gamelogic.commands.composite.PrepareGameSessionCommand;
import com.yan.durak.gamelogic.commands.composite.StartRoundCommand;
import com.yan.durak.gamelogic.commands.core.AddBotPlayerCommand;
import com.yan.durak.gamelogic.commands.custom.IdentifyNextRoundPlayersCommand;
import com.yan.durak.gamelogic.commands.hooks.notifiers.broadcast.RemoteClientsCardsMoveBroadcastHook;
import com.yan.durak.gamelogic.communication.connection.RemoteClientsManager;
import com.yan.durak.gamelogic.utils.LogUtils;

import java.io.IOException;

/**
 * Created by Yan-Home on 12/22/2014.
 * <p/>
 * This scenario starts a game with 3 bot players.
 */
public class ScriptedBotsWithRemoteListenerGameStartCommand extends BaseSessionCommand {

    private static final int BOT_PLAYERS_AMOUNT = 3;

    @Override
    public void execute() {

        //TODO : Create real logic for clients connection
        try {
            LogUtils.log("Waiting for remote clients to connect");
            RemoteClientsManager.getInstance().waitForClientToConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogUtils.log("Remote client connected  , trying to send remote client a message");

        //add hook to send all movement commands to remote clients
        getGameSession().addPostHook(new RemoteClientsCardsMoveBroadcastHook());

        //clear the game session and put discard and stock piles
        getGameSession().executeCommand(new PrepareGameSessionCommand());

        //add bot players
        for (int i = 0; i < BOT_PLAYERS_AMOUNT; i++) {
            //add bot player
            getGameSession().executeCommand(new AddBotPlayerCommand());
        }

        //define who attacks and who defends
        IdentifyNextRoundPlayersCommand identifyCommand = new IdentifyNextRoundPlayersCommand();
        getGameSession().executeCommand(identifyCommand);

        //let player attack and the next player by it to defend
        StartRoundCommand startRoundCommand = new StartRoundCommand();
        startRoundCommand.setRoundAttackingPlayerIndex(identifyCommand.getNextRoundAttackerPlayerIndex());
        startRoundCommand.setRoundDefendingPlayerIndex(identifyCommand.getNextRoundDefenderPlayerIndex());
        getGameSession().executeCommand(startRoundCommand);
    }
}