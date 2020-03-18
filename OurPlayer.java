/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.ourplayer;

import java.util.List;
import java.util.logging.Logger;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

@SuppressWarnings("Duplicates")
public class OurPlayer extends Player {


    @Override
    public String getName() {
        return "Iwo Naglik 136774 Bartosz Przybył 136785";
    }


    @Override
    public Move nextMove(Board b) {
        Logger log = Logger.getLogger(OurPlayer.class.getName());
        List<Move> moves = b.getMovesFor(getColor());
        final int MAX = Integer.MAX_VALUE;
        final int MIN = Integer.MIN_VALUE;
        int[] val = minmax(3, true, moves, MIN, MAX, b.clone(), getColor());
        return moves.get(val[1]);
    }

    private static int[] minmax(int depth, boolean max_player, List<Move> moves, int alpha, int beta, Board board, Color color) {
        Logger log = Logger.getLogger(OurPlayer.class.getName()); //java.util.logging.Logger
        int[] out = new int[2];

        if (depth == 0) {
            out[0] = calculateQuality(board, color);
            return out;
        }

        if (board.getWinner(color) == color) {
            out[0] = 1000;
            return out;
        }
        else if (board.getWinner(getOpponent(color)) == getOpponent(color)) {
            out[0] = -2000;
            return out;
        }

        if (max_player) {
            out[0] = Integer.MIN_VALUE;
            for (int i = 0; i < moves.size(); i++) {
                board.doMove(moves.get(i));
                int[] values = minmax(depth - 1, false, board.getMovesFor(getOpponent(color)), alpha, beta, board.clone(), color);
                board.undoMove(moves.get(i));
                if (values[0] > out[0]) out[1] = i;
                out[0] = Math.max(out[0], values[0]);
                alpha = Math.max(alpha, out[0]);
                if (beta <= alpha) break;
            }
        } else {
            out[0] = Integer.MAX_VALUE;
            for (int i = 0; i < moves.size(); i++) {
                board.doMove(moves.get(i));
                int[] values = minmax(depth - 1, true, board.getMovesFor(color), alpha, beta, board.clone(), color);
                board.undoMove(moves.get(i));
                if (values[0] < out[0]) out[1] = i;
                out[0] = Math.min(out[0], values[0]);
                beta = Math.min(beta, out[0]);
                if (beta <= alpha) break;
            }
        }

        return out;
    }

    private static int calculateQuality(Board board, Color myColor) {
        Logger log = Logger.getLogger(OurPlayer.class.getName());
        int quality = 0;
        int row_mine = 0;
        int row_opponent = 0;
        if (board.getWinner(myColor) == myColor) quality += 1000;
        else if (board.getWinner(getOpponent(myColor)) == getOpponent(myColor)) quality -= 2000;
        else {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (board.getState(i, j) == getOpponent(myColor) && board.getState(i, j + 1) == getOpponent(myColor)) {
                        quality -= row_opponent + 1;
                        row_opponent++;
                    } else row_opponent = 0;

                    if(board.getState(i, j) == myColor && board.getState(i, j+1) == myColor) {
                        quality += row_mine + 0.5;
                        row_mine += 0.5;
                    } else row_mine = 0;
                }
            }

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (board.getState(j, i) == getOpponent(myColor) && board.getState(j, i + 1) == getOpponent(myColor)) {
                        quality -= row_opponent + 1;
                        row_opponent++;
                    } else row_opponent = 0;

                    if(board.getState(j, i) == myColor && board.getState(j, i+1) == myColor) {
                        quality += row_mine + 0.5;
                        row_mine += 0.5;
                    } else row_mine = 0;
                }
            }
        }

        //log.info(String.valueOf(quality));
        return quality;
    }
}

