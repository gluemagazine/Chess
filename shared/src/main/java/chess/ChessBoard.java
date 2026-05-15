package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    private ChessPosition blackEnPasant;
    private ChessPosition whiteEnPasant;
    public ChessBoard() {
        blackEnPasant = new ChessPosition(-1,-1);
        whiteEnPasant = new ChessPosition(-1,-1);
    }

    public boolean isValid(ChessPosition pos){
        if (pos.getRow()-1 >= 8 || pos.getColumn()-1 >=8){
            return false;
        }
        return pos.getRow()-1 >= 0 && pos.getColumn()-1 >= 0;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (isValid(position)){
            squares[position.getRow()-1][position.getColumn()-1] = piece;
        }
    }

    /**
     * Overrides a chess position on the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void setPiece(ChessPosition position, ChessPiece piece) {
        if (isValid(position)){
            squares[position.getRow()-1][position.getColumn()-1] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(isValid(position)){
            return squares[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    private ChessPiece generatePiece(ChessGame.TeamColor color,ChessPiece.PieceType type){
        return new ChessPiece(color, type);
    }

    public void resetBoard() {
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;

        for (int i = 0; i < 8; i++){

            squares[1][i] = generatePiece(white, ChessPiece.PieceType.PAWN);
            squares[6][i] = generatePiece(black, ChessPiece.PieceType.PAWN);
        }

        squares[0][0] = generatePiece(white, ChessPiece.PieceType.ROOK);
        squares[0][7] = generatePiece(white, ChessPiece.PieceType.ROOK);
        squares[7][0] = generatePiece(black, ChessPiece.PieceType.ROOK);
        squares[7][7] = generatePiece(black, ChessPiece.PieceType.ROOK);
        squares[0][1] = generatePiece(white, ChessPiece.PieceType.KNIGHT);
        squares[0][6] = generatePiece(white, ChessPiece.PieceType.KNIGHT);
        squares[7][1] = generatePiece(black, ChessPiece.PieceType.KNIGHT);
        squares[7][6] = generatePiece(black, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = generatePiece(white, ChessPiece.PieceType.BISHOP);
        squares[0][5] = generatePiece(white, ChessPiece.PieceType.BISHOP);
        squares[7][2] = generatePiece(black, ChessPiece.PieceType.BISHOP);
        squares[7][5] = generatePiece(black, ChessPiece.PieceType.BISHOP);
        squares[0][3] = generatePiece(white, ChessPiece.PieceType.QUEEN);
        squares[0][4] = generatePiece(white, ChessPiece.PieceType.KING);
        squares[7][3] = generatePiece(black, ChessPiece.PieceType.QUEEN);
        squares[7][4] = generatePiece(black, ChessPiece.PieceType.KING);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder thing = new StringBuilder();
        thing.append("Board: \n");
        for(int i = 8; i > 0; i--){
            for(ChessPiece piece : squares[i-1]){
                if (piece == null){
                    thing.append("| ");
                }
                else{
                    thing.append(String.format("|%s",piece));
                }
            }
            thing.append("|\n");
        }
        return thing.toString();
    }

    @Override
    public ChessBoard clone() throws CloneNotSupportedException {
//        ChessBoard chessBoard = (ChessBoard) super.clone();
        ChessBoard copy = new ChessBoard();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++){
                copy.addPiece(new ChessPosition(i+1,j+1),getPiece(new ChessPosition(i+1,j+1)));
            }
        }
        copy.blackEnPasant = blackEnPasant;
        copy.whiteEnPasant = whiteEnPasant;
        return copy;
    }

    public ChessPosition getBlackEnPasant() {
        return blackEnPasant;
    }

    public void setBlackEnPasant(ChessPosition newPesants) {
        blackEnPasant = newPesants;
        System.out.println("black: " + newPesants);

    }

    public ChessPosition getWhiteEnPasant() {
        return whiteEnPasant;
    }

    public void setWhiteEnPasant(ChessPosition newPesants) {
        whiteEnPasant = newPesants;
        System.out.println("white: " + newPesants);
    }
}
