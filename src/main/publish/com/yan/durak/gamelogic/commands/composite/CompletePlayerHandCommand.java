package com.yan.durak.gamelogic.commands.composite;


import com.yan.durak.gamelogic.cards.Pile;
import com.yan.durak.gamelogic.commands.BaseSessionCommand;

/**
 * Created by Yan-Home on 12/23/2014.
 */
public class CompletePlayerHandCommand extends BaseSessionCommand {

    private int mPlayerIndex;

    @Override
    public void execute() {
        int indexOfStockPile = getGameSession().getPilesStack().indexOf(getGameSession().findPileByTag(Pile.PileTags.STOCK_PILE_TAG));
        CompletePileToAmountOfCards moveTopCardsFromPileToPilePlayerOne = new CompletePileToAmountOfCards();
        moveTopCardsFromPileToPilePlayerOne.setFromPileIndex(indexOfStockPile);
        moveTopCardsFromPileToPilePlayerOne.setToPileIndex(getGameSession().getPlayers().get(mPlayerIndex).getPileIndex());
        moveTopCardsFromPileToPilePlayerOne.setCardsAmount(getGameSession().getGameRules().getAmountOfCardsInPlayerHands());
        getGameSession().executeCommand(moveTopCardsFromPileToPilePlayerOne);
    }

    public void setPlayerIndex(int playerIndex) {
        mPlayerIndex = playerIndex;
    }
}
