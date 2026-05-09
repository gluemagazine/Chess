package chess;

import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;
    private HashSet<ChessPosition> blackPieces;
    private HashSet<ChessPosition> whitePieces;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;



    public ChessGame() {
        turn = TeamColor.WHITE;
        blackPieces = new HashSet<>();
        whitePieces = new HashSet<>();
        ChessBoard newBoard = new ChessBoard();
        newBoard.resetBoard();
        setBoard(newBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    public Collection<ChessMove> getThreatened(ChessPosition startPosition, TeamColor opponent){
        return null;
    }
    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> possible = piece.pieceMoves(board,startPosition);
        Collection<ChessMove> actual = new ArrayList<>();


        for(var move : possible){
            actual.add(move);
        }


        return actual;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard prev = board.clone();
        ChessPosition start = move.getStartPosition();
        ChessPiece piece = board.getPiece(start);
        Collection<ChessMove> valid = validMoves(start);

        if (piece.getTeamColor() != turn){
            throw new InvalidMoveException(String.format("It is not %s's turn",turn));
        }

        boolean isValid = false;
        for(var thing : valid){
            if (thing.equals(move)){
                isValid = true;
                break;
            }
        }
        if(!isValid){
            throw new InvalidMoveException(String.format("%s is not a valid move",move));
        }
        board.setPiece(move.getEndPosition(),piece);
        board.setPiece(move.getStartPosition(),null);

        if(isInCheck(turn)){
            board = prev;
            throw new InvalidMoveException(String.format("%s is not a valid move",move));
        }

        if(turn == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
            whitePieces.remove(move.getStartPosition());
            whitePieces.add(move.getEndPosition());
            blackPieces.remove(move.getEndPosition());
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                whiteKing = move.getEndPosition();
            }
        }
        else{
            setTeamTurn(TeamColor.WHITE);
            blackPieces.remove(move.getStartPosition());
            blackPieces.add(move.getEndPosition());
            whitePieces.remove(move.getEndPosition());
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                blackKing = move.getEndPosition();
            }
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        return false;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        return false;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        return false;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;

        blackPieces.clear();
        whitePieces.clear();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece at = board.getPiece(new ChessPosition(i,j));
                if (at == null){
                    continue;
                }
                if (at.getTeamColor() == TeamColor.WHITE){
                    whitePieces.add(new ChessPosition(i,j));
                    if(at.getPieceType() == ChessPiece.PieceType.KING){
                        whiteKing = new ChessPosition(i,j);
                    }
                }
                else{
                    blackPieces.add(new ChessPosition(i,j));
                    if(at.getPieceType() == ChessPiece.PieceType.KING){
                        blackKing = new ChessPosition(i,j);
                    }
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                '}';
    }
}
