package com.yan.durak.gamelogic.commands.hooks.notifiers.broadcast;


import com.yan.durak.gamelogic.commands.custom.PlayerThrowInRequestCommand;
import com.yan.durak.gamelogic.commands.hooks.CommandHook;
import com.yan.durak.gamelogic.communication.connection.SocketClient;
import com.yan.durak.gamelogic.communication.protocol.messages.PlayerTakesActionMessage;
import com.yan.durak.gamelogic.player.Player;
import com.yan.durak.gamelogic.player.RemotePlayer;

/**
 * Created by Yan-Home on 12/24/2014.
 */
public class PlayerActionThrowInBroadcastHook implements CommandHook<PlayerThrowInRequestCommand> {

    @Override
    public Class<PlayerThrowInRequestCommand> getHookTriggerCommandClass() {
        return PlayerThrowInRequestCommand.class;
    }

    @Override
    public void onHookTrigger(PlayerThrowInRequestCommand hookCommand) {

        //create json string from the message
        String jsonMsg = new PlayerTakesActionMessage(hookCommand.getThrowingInPlayerIndex(), PlayerTakesActionMessage.PlayerAction.THROW_IN).toJsonString();

        for (Player player : hookCommand.getGameSession().getPlayers()) {
            if (player instanceof RemotePlayer) {
                RemotePlayer remotePlayer = (RemotePlayer) player;
                SocketClient client = remotePlayer.getSocketClient();
                client.sendMessage(jsonMsg);
            }
        }
    }
}
