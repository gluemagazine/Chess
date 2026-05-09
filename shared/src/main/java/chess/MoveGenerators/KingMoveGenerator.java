package chess.MoveGenerators;

import chess.*;

import java.util.Collection;

public class KingMoveGenerator extends MoveGenerator{
    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1},{-1,-1},{-1,1},{1,-1},{1,1}};
        return getMovesFromDirs(board,pos,dirs,1);
    }

    @Override
    public Collection<ChessMove> getTheoreticalMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1},{-1,-1},{-1,1},{1,-1},{1,1}};
        return getTheoreticalFromDirs(board,pos,dirs,1);
    }
}
